/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package net.okocraft.okochat.core.channel;

import java.util.ArrayList;
import java.util.List;

import net.okocraft.okochat.core.LunaChat;
import net.okocraft.okochat.core.LunaChatAPI;
import net.okocraft.okochat.core.LunaChatBukkit;
import net.okocraft.okochat.core.LunaChatConfig;
import net.okocraft.okochat.core.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import net.okocraft.okochat.core.bridge.DynmapBridge;
import net.okocraft.okochat.core.member.ChannelMember;
import net.okocraft.okochat.core.member.ChannelMemberBukkit;
import net.okocraft.okochat.core.util.ClickableFormat;
import net.okocraft.okochat.core.util.UtilityBukkit;

import net.md_5.bungee.api.chat.BaseComponent;

/**
 * チャンネルの実装クラス
 * @author ucchy
 */
public class BukkitChannel extends Channel {

    /**
     * コンストラクタ
     * @param name チャンネル名
     */
    protected BukkitChannel(String name) {
        super(name);
    }

    /**
     * メッセージを表示します。指定したプレイヤーの発言として処理されます。
     * @param player プレイヤー（ワールドチャット、範囲チャットの場合は必須です）
     * @param message メッセージ
     * @param format フォーマット
     * @param sendDynmap dynmapへ送信するかどうか
     */
    @Override
    protected void sendMessage(
            ChannelMember player, String message,
            @Nullable ClickableFormat format, boolean sendDynmap) {

        LunaChatConfig config = LunaChat.getConfig();

        String originalMessage = new String(message);

        // 受信者を設定する
        List<ChannelMember> recipients = new ArrayList<ChannelMember>();
        boolean sendNoRecipientMessage = false;

        if ( isBroadcastChannel() ) {
            // ブロードキャストチャンネル

            for ( Player p : Bukkit.getOnlinePlayers() ) {
                ChannelMember cp = ChannelMember.getChannelMember(p);
                if ( !getHided().contains(cp) ) {
                    recipients.add(cp);
                }
            }
        } else {
            // 通常チャンネル

            for ( ChannelMember mem : getMembers() ) {
                if ( mem != null && mem.isOnline() && !getHided().contains(mem) ) {
                    recipients.add(mem);
                }
            }
        }

        if ( isWorldRange() && player.isOnline() && player.getWorldName() != null ) {
            // ワールドチャットや範囲チャットの場合は、範囲外のプレイヤーをrecipientsから抜く

            List<ChannelMember> recipientsNew = new ArrayList<>();

            if ( getChatRange() > 0 ) {
                // 範囲チャット

                @Nullable Location origin = ((ChannelMemberBukkit)player).getLocation();
                for ( ChannelMember recipient : recipients ) {
                    @Nullable Location target = ((ChannelMemberBukkit)recipient).getLocation();
                    if ( origin != null && target != null &&
                            origin.getWorld().equals(target.getWorld()) &&
                            origin.distance(target) <= getChatRange() ) {
                        recipientsNew.add(recipient);
                    }
                }

            } else {
                // ワールドチャット

                @Nullable World w = ((ChannelMemberBukkit)player).getWorld();
                for ( ChannelMember recipient : recipients ) {
                    @Nullable World target = ((ChannelMemberBukkit)recipient).getWorld();
                    if ( w != null && target != null && w.equals(target) ) {
                        recipientsNew.add(recipient);
                    }
                }
            }

            recipients = recipientsNew;

            // 受信者が自分以外いない場合は、メッセージを表示する
            if (Messages.noRecipientMessage("", "").length > 0 && (
                    recipients.size() == 0 ||
                    (recipients.size() == 1 &&
                     recipients.get(0).getName().equals(player.getName()) ) ) ) {
                sendNoRecipientMessage = true;
            }
        }

        // opListenAllChannel 設定がある場合は、
        // パーミッション lunachat-admin.listen-all-channels を持つプレイヤーを
        // 受信者に加える。
        if ( config.isOpListenAllChannel() ) {
            for ( Player p : Bukkit.getOnlinePlayers() ) {
                ChannelMember cp = ChannelMember.getChannelMember(p);
                if ( cp.hasPermission("lunachat-admin.listen-all-channels")
                        && !recipients.contains(cp) ) {
                    recipients.add(cp);
                }
            }
        }

        // hideされている場合は、受信対象者から抜く。
        LunaChatAPI api = LunaChat.getAPI();
        for ( ChannelMember cp : api.getHidelist(player) )  {
            if ( recipients.contains(cp) ) {
                recipients.remove(cp);
            }
        }

        // LunaChatChannelMessageEvent イベントコール
        String name = (player != null) ? player.getDisplayName() : "<null>";

        // 通常ブロードキャストなら、設定に応じてdynmapへ送信する
        DynmapBridge dynmap = LunaChatBukkit.getInstance().getDynmap();
        if ( config.isSendBroadcastChannelChatToDynmap() &&
                sendDynmap &&
                dynmap != null &&
                isBroadcastChannel() &&
                !isWorldRange() ) {

            String msg = config.isSendFormattedMessageToDynmap() ? message : originalMessage;
            if ( player != null && player instanceof ChannelMemberBukkit
                    && ((ChannelMemberBukkit)player).getPlayer() != null ) {
                dynmap.chat(((ChannelMemberBukkit)player).getPlayer(), msg);
            } else {
                dynmap.broadcast(msg);
            }
        }

        // 送信する
        if ( format != null ) {
            format.replace("%msg", message);
            BaseComponent[] comps = format.makeTextComponent();
            for ( ChannelMember p : recipients ) {
                p.sendMessage(comps);
            }
            message = format.toLegacyText();
        } else {
            for ( ChannelMember p : recipients ) {
                p.sendMessage(message);
            }
        }

        // 設定に応じて、コンソールに出力する
        if ( config.isDisplayChatOnConsole() ) {
            Bukkit.getLogger().info(message);
        }

        // 受信者が自分以外いない場合は、メッセージを表示する
        if ( sendNoRecipientMessage ) {
            player.sendMessage(Messages.noRecipientMessage(getColorCode(), getName()));
        }

        // ロギング
        log(originalMessage, name);
    }

    /**
     * チャンネルのオンライン人数を返す
     * @return オンライン人数
     * @see Channel#getOnlineNum()
     */
    @Override
    public int getOnlineNum() {

        // ブロードキャストチャンネルならサーバー接続人数を返す
        if ( isBroadcastChannel() ) {
            return UtilityBukkit.getOnlinePlayersCount();
        }

        return super.getOnlineNum();
    }

    /**
     * チャンネルの総参加人数を返す
     * @return 総参加人数
     * @see Channel#getTotalNum()
     */
    @Override
    public int getTotalNum() {

        // ブロードキャストチャンネルならサーバー接続人数を返す
        if ( isBroadcastChannel() ) {
            return UtilityBukkit.getOnlinePlayersCount();
        }

        return super.getTotalNum();
    }

    /**
     * チャンネルのメンバーを返す
     * @return チャンネルのメンバー
     * @see Channel#getMembers()
     */
    @Override
    public List<ChannelMember> getMembers() {

        // ブロードキャストチャンネルなら、
        // 現在サーバーに接続している全プレイヤーをメンバーとして返す
        if ( isBroadcastChannel() ) {
            List<ChannelMember> mem = new ArrayList<ChannelMember>();
            for ( Player p : Bukkit.getOnlinePlayers() ) {
                mem.add(ChannelMember.getChannelMember(p));
            }
            return mem;
        }

        return super.getMembers();
    }

    /**
     * ログを記録する
     * @param name 発言者
     * @param message 記録するメッセージ
     */
    @Override
    protected void log(String message, String name) {

        // LunaChatのチャットログへ記録
        LunaChatConfig config = LunaChat.getConfig();
        if ( config.isLoggingChat() && logger != null ) {
            logger.log(message, name);
        }

        // TODO ログ記録プラグイン連携を検討する
//        // Hawkeye Reloaded のチャットログへ記録
//        if ( config.isLoggingChatToHawkEye() && LunaChat.getInstance().getHawkEye() != null
//                && player != null && player.getLocation() != null ) {
//            LunaChat.getInstance().getHawkEye().writeLog(name, player.getLocation(),
//                    "channel(" + getName() + ")-" + Utility.stripColor(message));
//        }
//
//        // Prism のチャットログへ記録
//        if ( config.isLoggingChatToPrism() && LunaChat.getInstance().getPrism() != null
//                && player != null && player.getPlayer() != null ) {
//            LunaChat.getInstance().getPrism().writeLog(player.getPlayer(),
//                    "channel(" + getName() + ")-" + Utility.stripColor(message));
//        }
    }
}
