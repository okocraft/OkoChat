/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3.channel;

import com.github.ucchyocean.lc3.LunaChat;
import com.github.ucchyocean.lc3.LunaChatConfig;
import com.github.ucchyocean.lc3.LunaChatVelocity;
import com.github.ucchyocean.lc3.event.EventResult;
import com.github.ucchyocean.lc3.member.ChannelMember;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.okocraft.okochat.api.OkoChat;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.Objects;

/**
 * チャンネルのBungee実装クラス
 *
 * @author ucchy
 */
public class VelocityChannel extends Channel {

    private static final Pattern DEFAULT_URL_PATTERN = Pattern.compile("(?:(https?)://)?([-\\w_.]+\\.\\w{2,})(/\\S*)?"); // copied from LegacyComponentSerializerImpl
    private static final Pattern PATH_SPLITTER = Pattern.compile("/", Pattern.LITERAL);
    private static final LegacyComponentSerializer WITH_URL_LEGACY_SERIALIZER = LegacyComponentSerializer.legacySection().toBuilder().extractUrls().build();

    /**
     * コンストラクタ
     *
     * @param name チャンネル名
     */
    protected VelocityChannel(String name) {
        super(name);
    }

    /**
     * メッセージを表示します。指定したプレイヤーの発言として処理されます。
     *
     * @param player     プレイヤー（ワールドチャット、範囲チャットの場合は必須です）
     * @param message    メッセージ
     * @param format     フォーマット
     * @param sendDynmap dynmapへ送信するかどうか
     */
    @Override
    protected void sendMessage(ChannelMember player, String message) {

        LunaChatConfig config = LunaChat.getConfig();

        String originalMessage = new String(message);

        // 受信者を設定する
        List<ChannelMember> recipients = new ArrayList<ChannelMember>();

        if (isBroadcastChannel()) {
            // ブロードキャストチャンネル

            // NOTE: BungeeChannelは範囲チャットやワールドチャットをサポートしない

            // 通常ブロードキャスト（全員へ送信）
            for (Player p : LunaChatVelocity.getInstance().server.getAllPlayers()) {
                ChannelMember cp = ChannelMember.getChannelMember(p);
                if (!getHided().contains(cp)) {
                    recipients.add(cp);
                }
            }

        } else {
            // 通常チャンネル

            for (ChannelMember mem : getMembers()) {
                if (mem != null && mem.isOnline() && !getHided().contains(mem)) {
                    recipients.add(mem);
                }
            }
        }

        // opListenAllChannel 設定がある場合は、
        // パーミッション lunachat-admin.listen-all-channels を持つプレイヤーを
        // 受信者に加える。
        if (config.isOpListenAllChannel()) {
            for (Player p : LunaChatVelocity.getInstance().server.getAllPlayers()) {
                ChannelMember cp = ChannelMember.getChannelMember(p);
                if (cp.hasPermission("lunachat-admin.listen-all-channels")
                    && !recipients.contains(cp)) {
                    recipients.add(cp);
                }
            }
        }

        // hideされている場合は、受信対象者から抜く。
        recipients.stream()
                .filter(Objects::nonNull)
                .filter(recipient -> OkoChat.api().hideListProvider().getByUUID(recipient.identity().uuid()).isHidden(player))
                .toList()
                .forEach(recipients::remove);

        // フォーマットがある場合は置き換える

        // LunaChatChannelMessageEvent イベントコール
        String name = (player != null) ? player.getDisplayName() : "<null>";
        EventResult result = LunaChat.getEventSender().sendLunaChatChannelMessageEvent(
                getName(), player, message, recipients, name, originalMessage);
        message = result.getMessage();
        recipients = result.getRecipients();

        message = DEFAULT_URL_PATTERN.matcher(message).replaceAll(matchResult -> {
            String url = matchResult.group();
            try {
                //noinspection ResultOfMethodCallIgnored
                URI.create(matchResult.group());
                return url;
            } catch (IllegalArgumentException e) {
                StringBuilder builder = new StringBuilder();

                String protocol = matchResult.group(1);
                if (protocol != null && !protocol.isEmpty()) {
                    builder.append(protocol).append("://");
                }

                String domain = matchResult.group(2);
                if (domain != null && !domain.isEmpty()) {
                    builder.append(URLEncoder.encode(domain, StandardCharsets.UTF_8));
                }

                String path = matchResult.group(3);
                if (path != null && !path.isEmpty()) {
                    String[] split = PATH_SPLITTER.split(path);
                    for (int i = 0, splitLength = split.length; i < splitLength; i++) {
                        if (i > 0) {
                            builder.append("/");
                        }
                        builder.append(URLEncoder.encode(split[i], StandardCharsets.UTF_8));
                    }

                    if (path.endsWith("/")) {
                        builder.append("/");
                    }
                }

                return builder.toString();
            }
        });

        Component formattedMessage = this.compiledFormat.render(new LegacyChannelChatContext(
                this.getName(),
                player,
                new LegacySenderContext(player),
                message,
                WITH_URL_LEGACY_SERIALIZER.deserialize(message)
        ));

        for (ChannelMember p : recipients) {
            p.sendMessage(formattedMessage);
        }

        // 設定に応じて、コンソールに出力する
        if (config.isDisplayChatOnConsole()) {
            OkoChat.logger().info(WITH_URL_LEGACY_SERIALIZER.serialize(formattedMessage));
        }

        // ロギング
        log(originalMessage, name);
    }

    /**
     * チャンネルのオンライン人数を返す
     *
     * @return オンライン人数
     * @see Channel#getOnlineNum()
     */
    @Override
    public int getOnlineNum() {

        // ブロードキャストチャンネルならサーバー接続人数を返す
        if (isBroadcastChannel()) {
            return LunaChatVelocity.getInstance().server.getPlayerCount();
        }

        return super.getOnlineNum();
    }

    /**
     * チャンネルの総参加人数を返す
     *
     * @return 総参加人数
     * @see Channel#getTotalNum()
     */
    @Override
    public int getTotalNum() {

        // ブロードキャストチャンネルならサーバー接続人数を返す
        if (isBroadcastChannel()) {
            return LunaChatVelocity.getInstance().server.getPlayerCount();
        }

        return super.getTotalNum();
    }

    /**
     * チャンネルのメンバーを返す
     *
     * @return チャンネルのメンバー
     * @see Channel#getMembers()
     */
    @Override
    public List<ChannelMember> getMembers() {

        // ブロードキャストチャンネルなら、
        // 現在サーバーに接続している全プレイヤーをメンバーとして返す
        if (isBroadcastChannel()) {
            List<ChannelMember> mem = new ArrayList<ChannelMember>();
            for (Player p : LunaChatVelocity.getInstance().server.getAllPlayers()) {
                mem.add(ChannelMember.getChannelMember(p));
            }
            return mem;
        }

        return super.getMembers();
    }

    /**
     * ログを記録する
     *
     * @param name    発言者
     * @param message 記録するメッセージ
     */
    @Override
    protected void log(String message, String name) {

        // LunaChatのチャットログへ記録
        LunaChatConfig config = LunaChat.getConfig();
        if (config.isLoggingChat() && logger != null) {
            logger.log(message, name);
        }

        // TODO ログ記録プラグイン連携を検討する
    }
}
