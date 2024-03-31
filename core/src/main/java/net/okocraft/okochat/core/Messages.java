/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package net.okocraft.okochat.core;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import com.github.siroshun09.configapi.core.node.MapNode;
import com.github.siroshun09.configapi.format.yaml.YamlFormat;
import net.kyori.adventure.text.Component;
import net.okocraft.okochat.core.util.ClickableFormat;
import net.okocraft.okochat.core.util.KeywordReplacer;
import net.okocraft.okochat.core.util.Utility;


/**
 * プラグインのメッセージリソース管理クラス
 * @author ucchy
 */
public class Messages {

    private static MapNode resources;
    private static File _messageFolder;
    private static File _jar;

    /**
     * Jarファイル内から直接 messages_en.yml をdefaultMessagesとしてロードし、
     * langに対応するメッセージをファイルからロードする。
     * @param messagesFolder メッセージ格納フォルダ
     * @param jar jarファイル（JTestからの実行時はnullを指定可）
     * @param lang デフォルト言語
     */
    public static void initialize(File messagesFolder, File jar, String lang) {

        _jar = jar;
        _messageFolder = messagesFolder;
        if (!_messageFolder.exists()) {
            _messageFolder.mkdirs();
        }

        // コンフィグフォルダにメッセージファイルがまだ無いなら、コピーしておく
        for (String filename : new String[] {
                "messages_en.yml", "messages_ja.yml" }) {
            File file = new File(_messageFolder, filename);
            if (!file.exists()) {
                Utility.copyFileFromJar(_jar, file, filename, true);
            }
        }

        // デフォルトメッセージを、jarファイル内からロードする
        MapNode defaultMessages = null;
        if ( _jar != null ) {
            try (JarFile jarFile = new JarFile(_jar)) {

                ZipEntry zipEntry = jarFile.getEntry(String.format("messages_%s.yml", lang));
                if (zipEntry == null) {
                    zipEntry = jarFile.getEntry("messages_en.yml");
                }

                defaultMessages = YamlFormat.DEFAULT.load(jarFile.getInputStream(zipEntry));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 対応する言語のメッセージをロードする
        File file = new File(_messageFolder, String.format("messages_%s.yml", lang));
        if (!file.exists()) {
            file = new File(_messageFolder, "messages_en.yml");
        }

        try {
            resources = YamlFormat.DEFAULT.load(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (defaultMessages != null) {
            for (var entry : defaultMessages.value().entrySet()) {
                resources.setIfAbsent(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 指定された言語でリロードを行う。
     * @param lang 言語
     */
    public static void reload(String lang) {
        initialize(_jar, _messageFolder, lang);
    }

    // ここから下は自動生成メソッドです。変更をしないでください。

    // === Auto-generated methods area start. ===

    /**
     * &f[%color%%channel%&f]&7%player% さんがチャンネルに参加しました。
     */
    public static Component joinMessage(Object color, Object channel, Object player) {
        String msg = resources.getStringOrNull("joinMessage");
        if ( msg == null ) return Component.empty();
        ClickableFormat cf = ClickableFormat.makeChannelClickableMessage(msg, channel.toString());
        cf.replace("%color%", color.toString());
        cf.replace("%channel%", channel.toString());
        cf.replace("%player%", player.toString());
        return cf.makeTextComponent();
    }

    /**
     * &f[%color%%channel%&f]&7%player% さんがチャンネルから退出しました。
     */
    public static Component quitMessage(Object color, Object channel, Object player) {
        String msg = resources.getStringOrNull("quitMessage");
        if ( msg == null ) return Component.empty();
        ClickableFormat cf = ClickableFormat.makeChannelClickableMessage(msg, channel.toString());
        cf.replace("%color%", color.toString());
        cf.replace("%channel%", channel.toString());
        cf.replace("%player%", player.toString());
        return cf.makeTextComponent();
    }

    /**
     * &f[%color%%channel%&f]&7チャンネルが削除されました。
     */
    public static Component breakupMessage(Object color, Object channel) {
        String msg = resources.getStringOrNull("breakupMessage");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%color%", color.toString());
        kr.replace("%channel%", channel.toString());
        return kr.toComponent();
    }

    /**
     * &f[%color%%channel%&f]&7%player% さんをチャンネルからBANしました。
     */
    public static Component banMessage(Object color, Object channel, Object player) {
        String msg = resources.getStringOrNull("banMessage");
        if ( msg == null ) return Component.empty();
        ClickableFormat cf = ClickableFormat.makeChannelClickableMessage(msg, channel.toString());
        cf.replace("%color%", color.toString());
        cf.replace("%channel%", channel.toString());
        cf.replace("%player%", player.toString());
        return cf.makeTextComponent();
    }

    /**
     * &f[%color%%channel%&f]&7%player% さんをチャンネルからキックしました。
     */
    public static Component kickMessage(Object color, Object channel, Object player) {
        String msg = resources.getStringOrNull("kickMessage");
        if ( msg == null ) return Component.empty();
        ClickableFormat cf = ClickableFormat.makeChannelClickableMessage(msg, channel.toString());
        cf.replace("%color%", color.toString());
        cf.replace("%channel%", channel.toString());
        cf.replace("%player%", player.toString());
        return cf.makeTextComponent();
    }

    /**
     * &f[%color%%channel%&f]&7%player% さんをチャンネルからMuteしました。
     */
    public static Component muteMessage(Object color, Object channel, Object player) {
        String msg = resources.getStringOrNull("muteMessage");
        if ( msg == null ) return Component.empty();
        ClickableFormat cf = ClickableFormat.makeChannelClickableMessage(msg, channel.toString());
        cf.replace("%color%", color.toString());
        cf.replace("%channel%", channel.toString());
        cf.replace("%player%", player.toString());
        return cf.makeTextComponent();
    }

    /**
     * &f[%color%%channel%&f]&7NGワード発言により、%player% さんをチャンネルから自動BANしました。
     */
    public static Component banNGWordMessage(Object color, Object channel, Object player) {
        String msg = resources.getStringOrNull("banNGWordMessage");
        if ( msg == null ) return Component.empty();
        ClickableFormat cf = ClickableFormat.makeChannelClickableMessage(msg, channel.toString());
        cf.replace("%color%", color.toString());
        cf.replace("%channel%", channel.toString());
        cf.replace("%player%", player.toString());
        return cf.makeTextComponent();
    }

    /**
     * &f[%color%%channel%&f]&7NGワード発言により、%player% さんをチャンネルから自動キックしました。
     */
    public static Component kickNGWordMessage(Object color, Object channel, Object player) {
        String msg = resources.getStringOrNull("kickNGWordMessage");
        if ( msg == null ) return Component.empty();
        ClickableFormat cf = ClickableFormat.makeChannelClickableMessage(msg, channel.toString());
        cf.replace("%color%", color.toString());
        cf.replace("%channel%", channel.toString());
        cf.replace("%player%", player.toString());
        return cf.makeTextComponent();
    }

    /**
     * &f[%color%%channel%&f]&7NGワード発言により、%player% さんをチャンネルから自動Muteしました。
     */
    public static Component muteNGWordMessage(Object color, Object channel, Object player) {
        String msg = resources.getStringOrNull("muteNGWordMessage");
        if ( msg == null ) return Component.empty();
        ClickableFormat cf = ClickableFormat.makeChannelClickableMessage(msg, channel.toString());
        cf.replace("%color%", color.toString());
        cf.replace("%channel%", channel.toString());
        cf.replace("%player%", player.toString());
        return cf.makeTextComponent();
    }

    /**
     * &f[%color%%channel%&f]&7%player% さんを期限 %minutes% 分でチャンネルからBANしました。
     */
    public static Component banWithExpireMessage(Object color, Object channel, Object player, Object minutes) {
        String msg = resources.getStringOrNull("banWithExpireMessage");
        if ( msg == null ) return Component.empty();
        ClickableFormat cf = ClickableFormat.makeChannelClickableMessage(msg, channel.toString());
        cf.replace("%color%", color.toString());
        cf.replace("%channel%", channel.toString());
        cf.replace("%player%", player.toString());
        cf.replace("%minutes%", minutes.toString());
        return cf.makeTextComponent();
    }

    /**
     * &f[%color%%channel%&f]&7%player% さんを期限 %minutes% 分でチャンネルからMuteしました。
     */
    public static Component muteWithExpireMessage(Object color, Object channel, Object player, Object minutes) {
        String msg = resources.getStringOrNull("muteWithExpireMessage");
        if ( msg == null ) return Component.empty();
        ClickableFormat cf = ClickableFormat.makeChannelClickableMessage(msg, channel.toString());
        cf.replace("%color%", color.toString());
        cf.replace("%channel%", channel.toString());
        cf.replace("%player%", player.toString());
        cf.replace("%minutes%", minutes.toString());
        return cf.makeTextComponent();
    }

    /**
     * &f[%color%%channel%&f]&7%player% さんのBANが解除されました。
     */
    public static Component pardonMessage(Object color, Object channel, Object player) {
        String msg = resources.getStringOrNull("pardonMessage");
        if ( msg == null ) return Component.empty();
        ClickableFormat cf = ClickableFormat.makeChannelClickableMessage(msg, channel.toString());
        cf.replace("%color%", color.toString());
        cf.replace("%channel%", channel.toString());
        cf.replace("%player%", player.toString());
        return cf.makeTextComponent();
    }

    /**
     * &f[%color%%channel%&f]&7%player% さんのMuteが解除されました。
     */
    public static Component unmuteMessage(Object color, Object channel, Object player) {
        String msg = resources.getStringOrNull("unmuteMessage");
        if ( msg == null ) return Component.empty();
        ClickableFormat cf = ClickableFormat.makeChannelClickableMessage(msg, channel.toString());
        cf.replace("%color%", color.toString());
        cf.replace("%channel%", channel.toString());
        cf.replace("%player%", player.toString());
        return cf.makeTextComponent();
    }

    /**
     * &f[%color%%channel%&f]&7%player% さんの期限付きBANが解除されました。
     */
    public static Component expiredBanMessage(Object color, Object channel, Object player) {
        String msg = resources.getStringOrNull("expiredBanMessage");
        if ( msg == null ) return Component.empty();
        ClickableFormat cf = ClickableFormat.makeChannelClickableMessage(msg, channel.toString());
        cf.replace("%color%", color.toString());
        cf.replace("%channel%", channel.toString());
        cf.replace("%player%", player.toString());
        return cf.makeTextComponent();
    }

    /**
     * &f[%color%%channel%&f]&7%player% さんの期限付きMuteが解除されました。
     */
    public static Component expiredMuteMessage(Object color, Object channel, Object player) {
        String msg = resources.getStringOrNull("expiredMuteMessage");
        if ( msg == null ) return Component.empty();
        ClickableFormat cf = ClickableFormat.makeChannelClickableMessage(msg, channel.toString());
        cf.replace("%color%", color.toString());
        cf.replace("%channel%", channel.toString());
        cf.replace("%player%", player.toString());
        return cf.makeTextComponent();
    }

    /**
     * &f[%color%%channel%&f]&7%player% さんがチャンネルのモデレーターになりました。
     */
    public static Component addModeratorMessage(Object color, Object channel, Object player) {
        String msg = resources.getStringOrNull("addModeratorMessage");
        if ( msg == null ) return Component.empty();
        ClickableFormat cf = ClickableFormat.makeChannelClickableMessage(msg, channel.toString());
        cf.replace("%color%", color.toString());
        cf.replace("%channel%", channel.toString());
        cf.replace("%player%", player.toString());
        return cf.makeTextComponent();
    }

    /**
     * &f[%color%%channel%&f]&7%player% さんがチャンネルのモデレーターから外れました。
     */
    public static Component removeModeratorMessage(Object color, Object channel, Object player) {
        String msg = resources.getStringOrNull("removeModeratorMessage");
        if ( msg == null ) return Component.empty();
        ClickableFormat cf = ClickableFormat.makeChannelClickableMessage(msg, channel.toString());
        cf.replace("%color%", color.toString());
        cf.replace("%channel%", channel.toString());
        cf.replace("%player%", player.toString());
        return cf.makeTextComponent();
    }

    /**
     * &f[%color%%channel%&f]&7あなたの発言は、誰にも届きませんでした。
     */
    public static Component noRecipientMessage(Object color, Object channel) {
        String msg = resources.getStringOrNull("noRecipientMessage");
        if ( msg == null ) return Component.empty();
        ClickableFormat cf = ClickableFormat.makeChannelClickableMessage(msg, channel.toString());
        cf.replace("%color%", color.toString());
        cf.replace("%channel%", channel.toString());
        return cf.makeTextComponent();
    }

    /**
     * &7---------- &bチャンネルリスト &7----------
     */
    public static Component listFirstLine() {
        String msg = resources.getStringOrNull("listFirstLine");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * &7---------- &bチャンネルリスト&7(&c%page%&7/&c%max%&7) ----------
     */
    public static Component listFirstLinePaging(Object page, Object max) {
        String msg = resources.getStringOrNull("listFirstLinePaging");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%page%", page.toString());
        kr.replace("%max%", max.toString());
        return kr.toComponent();
    }

    /**
     * &7----------------------------------
     */
    public static Component listEndLine() {
        String msg = resources.getStringOrNull("listEndLine");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * &7| &f%channel%&7(&c%online%&7/&c%total%&7) &a%topic%
     */
    public static Component listFormat(Object channel, Object online, Object total, Object topic) {
        String msg = resources.getStringOrNull("listFormat");
        if ( msg == null ) return Component.empty();
        ClickableFormat cf = ClickableFormat.makeChannelClickableMessage(msg, channel.toString());
        cf.replace("%channel%", channel.toString());
        cf.replace("%online%", online.toString());
        cf.replace("%total%", total.toString());
        cf.replace("%topic%", topic.toString());
        return cf.makeTextComponent();
    }

    /**
     * &7| 
     */
    public static Component listPlainPrefix() {
        String msg = resources.getStringOrNull("listPlainPrefix");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * &7---------- &bチャンネル情報 &7----------
     */
    public static Component channelInfoFirstLine() {
        String msg = resources.getStringOrNull("channelInfoFirstLine");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * &7| 
     */
    public static Component channelInfoPrefix() {
        String msg = resources.getStringOrNull("channelInfoPrefix");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * &7| &cチャンネル別名：&f
     */
    public static Component channelInfoAlias() {
        String msg = resources.getStringOrNull("channelInfoAlias");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * &7| &cグローバルチャンネル
     */
    public static Component channelInfoGlobal() {
        String msg = resources.getStringOrNull("channelInfoGlobal");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * &7| &cブロードキャストチャンネル
     */
    public static Component channelInfoBroadcast() {
        String msg = resources.getStringOrNull("channelInfoBroadcast");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * &7| &cシークレットチャンネル
     */
    public static Component channelInfoSecret() {
        String msg = resources.getStringOrNull("channelInfoSecret");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * &7| &cパスワード設定あり
     */
    public static Component channelInfoPassword() {
        String msg = resources.getStringOrNull("channelInfoPassword");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * &7| &cワールドチャット
     */
    public static Component channelInfoWorldChat() {
        String msg = resources.getStringOrNull("channelInfoWorldChat");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * &7| &c範囲チャット：%block% ブロック
     */
    public static Component channelInfoRangeChat(Object block) {
        String msg = resources.getStringOrNull("channelInfoRangeChat");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%block%", block.toString());
        return kr.toComponent();
    }

    /**
     * &7| &cフォーマット設定：
     */
    public static Component channelInfoFormat() {
        String msg = resources.getStringOrNull("channelInfoFormat");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * &7| &cBANリスト：
     */
    public static Component channelInfoBanned() {
        String msg = resources.getStringOrNull("channelInfoBanned");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * &7| &cMuteリスト：
     */
    public static Component channelInfoMuted() {
        String msg = resources.getStringOrNull("channelInfoMuted");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * &7----- &b参加中のチャット &7-----
     */
    public static Component motdFirstLine() {
        String msg = resources.getStringOrNull("motdFirstLine");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * &7----- &b非表示にしているチャット &7-----
     */
    public static Component hideChannelFirstLine() {
        String msg = resources.getStringOrNull("hideChannelFirstLine");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * &7----- &b非表示にしているプレイヤー &7-----
     */
    public static Component hidePlayerFirstLine() {
        String msg = resources.getStringOrNull("hidePlayerFirstLine");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * &7----- &b%channel%の発言ログ &7-----
     */
    public static Component logDisplayFirstLine(Object channel) {
        String msg = resources.getStringOrNull("logDisplayFirstLine");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        return kr.toComponent();
    }

    /**
     * &7----------------------------------
     */
    public static Component logDisplayEndLine() {
        String msg = resources.getStringOrNull("logDisplayEndLine");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * &7| &c%date%&7, &f%player%&7: &f%message%
     */
    public static Component logDisplayFormat(Object date, Object player, Object message) {
        String msg = resources.getStringOrNull("logDisplayFormat");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%date%", date.toString());
        kr.replace("%player%", player.toString());
        kr.replace("%message%", message.toString());
        return kr.toComponent();
    }

    /**
     * &f[&aLC&f]
     */
    public static Component infoPrefix() {
        String msg = resources.getStringOrNull("infoPrefix");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * &f[&cLC&f]
     */
    public static Component errorPrefix() {
        String msg = resources.getStringOrNull("errorPrefix");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * チャンネル %channel% に参加しました。
     */
    public static Component cmdmsgJoin(Object channel) {
        String msg = resources.getStringOrNull("cmdmsgJoin");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * デフォルトの発言先を %channel% に設定しました。
     */
    public static Component cmdmsgSet(Object channel) {
        String msg = resources.getStringOrNull("cmdmsgSet");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * トピック: &a%topic%
     */
    public static Component cmdmsgSetTopic(Object topic) {
        String msg = resources.getStringOrNull("cmdmsgSetTopic");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%topic%", topic.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * <注意> 現在このチャンネルを非表示に設定しています。
     */
    public static Component cmdmsgSetHide() {
        String msg = resources.getStringOrNull("cmdmsgSetHide");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * チャンネル %channel% から退出しました。
     */
    public static Component cmdmsgLeave(Object channel) {
        String msg = resources.getStringOrNull("cmdmsgLeave");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %player% さんを、チャンネル %channel% に招待しました。
     */
    public static Component cmdmsgInvite(Object player, Object channel) {
        String msg = resources.getStringOrNull("cmdmsgInvite");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%player%", player.toString());
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %player% さんから、チャンネル %channel% に招待されました。
     */
    public static Component cmdmsgInvited1(Object player, Object channel) {
        String msg = resources.getStringOrNull("cmdmsgInvited1");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%player%", player.toString());
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 入室するには /ch accept、拒否するには /ch deny を実行してください。
     */
    public static Component cmdmsgInvited2() {
        String msg = resources.getStringOrNull("cmdmsgInvited2");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 招待を拒否しました。
     */
    public static Component cmdmsgDeny() {
        String msg = resources.getStringOrNull("cmdmsgDeny");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 招待が拒否されました。
     */
    public static Component cmdmsgDenyed() {
        String msg = resources.getStringOrNull("cmdmsgDenyed");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %player% さんを、チャンネル %channel% からキックしました。
     */
    public static Component cmdmsgKick(Object player, Object channel) {
        String msg = resources.getStringOrNull("cmdmsgKick");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%player%", player.toString());
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * チャンネル %channel% からキックされました。
     */
    public static Component cmdmsgKicked(Object channel) {
        String msg = resources.getStringOrNull("cmdmsgKicked");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %player% さんを、チャンネル %channel% からBANしました。
     */
    public static Component cmdmsgBan(Object player, Object channel) {
        String msg = resources.getStringOrNull("cmdmsgBan");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%player%", player.toString());
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %player% さんを、チャンネル %channel% から期限 %minutes% 分でBANしました。
     */
    public static Component cmdmsgBanWithExpire(Object player, Object channel, Object minutes) {
        String msg = resources.getStringOrNull("cmdmsgBanWithExpire");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%player%", player.toString());
        kr.replace("%channel%", channel.toString());
        kr.replace("%minutes%", minutes.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * チャンネル %channel% からBANされました。
     */
    public static Component cmdmsgBanned(Object channel) {
        String msg = resources.getStringOrNull("cmdmsgBanned");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %player% さんの、チャンネル %channel% のBANを解除しました。
     */
    public static Component cmdmsgPardon(Object player, Object channel) {
        String msg = resources.getStringOrNull("cmdmsgPardon");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%player%", player.toString());
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * チャンネル %channel% のBANが解除されました。
     */
    public static Component cmdmsgPardoned(Object channel) {
        String msg = resources.getStringOrNull("cmdmsgPardoned");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %player% さんを、チャンネル %channel% でMuteしました。
     */
    public static Component cmdmsgMute(Object player, Object channel) {
        String msg = resources.getStringOrNull("cmdmsgMute");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%player%", player.toString());
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %player% さんを、チャンネル %channel% から期限 %minutes% 分でMuteしました。
     */
    public static Component cmdmsgMuteWithExpire(Object player, Object channel, Object minutes) {
        String msg = resources.getStringOrNull("cmdmsgMuteWithExpire");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%player%", player.toString());
        kr.replace("%channel%", channel.toString());
        kr.replace("%minutes%", minutes.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * チャンネル %channel% からMuteされました。
     */
    public static Component cmdmsgMuted(Object channel) {
        String msg = resources.getStringOrNull("cmdmsgMuted");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %player% さんの、チャンネル %channel% のMuteを解除しました。
     */
    public static Component cmdmsgUnmute(Object player, Object channel) {
        String msg = resources.getStringOrNull("cmdmsgUnmute");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%player%", player.toString());
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * チャンネル %channel% のMuteが解除されました。
     */
    public static Component cmdmsgUnmuted(Object channel) {
        String msg = resources.getStringOrNull("cmdmsgUnmuted");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * チャンネル %channel% を非表示に設定しました。
     */
    public static Component cmdmsgHided(Object channel) {
        String msg = resources.getStringOrNull("cmdmsgHided");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * プレイヤー %player% を非表示に設定しました。
     */
    public static Component cmdmsgHidedPlayer(Object player) {
        String msg = resources.getStringOrNull("cmdmsgHidedPlayer");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%player%", player.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * チャンネル %channel% を表示に設定しました。
     */
    public static Component cmdmsgUnhided(Object channel) {
        String msg = resources.getStringOrNull("cmdmsgUnhided");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * プレイヤー %channel% を表示に設定しました。
     */
    public static Component cmdmsgUnhidedPlayer(Object channel) {
        String msg = resources.getStringOrNull("cmdmsgUnhidedPlayer");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * LunaChatの設定を再読み込みしました。
     */
    public static Component cmdmsgReload() {
        String msg = resources.getStringOrNull("cmdmsgReload");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * チャンネル %channel% を新規作成しました。
     */
    public static Component cmdmsgCreate(Object channel) {
        String msg = resources.getStringOrNull("cmdmsgCreate");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * チャンネル %channel% を削除しました。
     */
    public static Component cmdmsgRemove(Object channel) {
        String msg = resources.getStringOrNull("cmdmsgRemove");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * メッセージフォーマットを %format% に設定しました。
     */
    public static Component cmdmsgFormat(Object format) {
        String msg = resources.getStringOrNull("cmdmsgFormat");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%format%", format.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %player% さんをチャンネル %channel% のモデレーターに設定しました。
     */
    public static Component cmdmsgModerator(Object player, Object channel) {
        String msg = resources.getStringOrNull("cmdmsgModerator");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%player%", player.toString());
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %player% さんをチャンネル %channel% のモデレーターから外しました。
     */
    public static Component cmdmsgModeratorMinus(Object player, Object channel) {
        String msg = resources.getStringOrNull("cmdmsgModeratorMinus");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%player%", player.toString());
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %key% を %value% と覚えました。
     */
    public static Component cmdmsgDictionaryAdd(Object key, Object value) {
        String msg = resources.getStringOrNull("cmdmsgDictionaryAdd");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%key%", key.toString());
        kr.replace("%value%", value.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %key% を忘れました。
     */
    public static Component cmdmsgDictionaryRemove(Object key) {
        String msg = resources.getStringOrNull("cmdmsgDictionaryRemove");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%key%", key.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %key% を %value% に設定しました。
     */
    public static Component cmdmsgOption(Object key, Object value) {
        String msg = resources.getStringOrNull("cmdmsgOption");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%key%", key.toString());
        kr.replace("%value%", value.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * テンプレート %index% を、%value% に設定しました。
     */
    public static Component cmdmsgTemplate(Object index, Object value) {
        String msg = resources.getStringOrNull("cmdmsgTemplate");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%index%", index.toString());
        kr.replace("%value%", value.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * テンプレート %index% を削除しました。
     */
    public static Component cmdmsgTemplateRemove(Object index) {
        String msg = resources.getStringOrNull("cmdmsgTemplateRemove");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%index%", index.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %player% さんの発言先を %channel% に設定しました。
     */
    public static Component cmdmsgSetDefault(Object player, Object channel) {
        String msg = resources.getStringOrNull("cmdmsgSetDefault");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%player%", player.toString());
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * Your chat's Japanize conversion was turned %value%.
     */
    public static Component cmdmsgPlayerJapanize(Object value) {
        String msg = resources.getStringOrNull("cmdmsgPlayerJapanize");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%value%", value.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %player% さんのJapanize変換を %value% にしました。
     */
    public static Component cmdmsgPlayerJapanizeOther(Object player, Object value) {
        String msg = resources.getStringOrNull("cmdmsgPlayerJapanizeOther");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%player%", player.toString());
        kr.replace("%value%", value.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %inviter%の現在の会話相手 : %invited%
     */
    public static Component cmdmsgReplyInviter(Object inviter, Object invited) {
        String msg = resources.getStringOrNull("cmdmsgReplyInviter");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%inviter%", inviter.toString());
        kr.replace("%invited%", invited.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %inviter%の現在の会話相手 : 相手がいません。
     */
    public static Component cmdmsgReplyInviterNone(Object inviter) {
        String msg = resources.getStringOrNull("cmdmsgReplyInviterNone");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%inviter%", inviter.toString());
        kr.prefix(resources.getString("infoPrefix", ""));
        return kr.toComponent();
    }

    /**
     * このコマンドはゲーム内からしか実行できません。
     */
    public static Component errmsgIngame() {
        String msg = resources.getStringOrNull("errmsgIngame");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * コマンドの指定が正しくありません。
     */
    public static Component errmsgCommand() {
        String msg = resources.getStringOrNull("errmsgCommand");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 指定されたチャンネルが存在しません。
     */
    public static Component errmsgNotExist() {
        String msg = resources.getStringOrNull("errmsgNotExist");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 指定されたチャンネルもプレイヤーも存在しません。
     */
    public static Component errmsgNotExistChannelAndPlayer() {
        String msg = resources.getStringOrNull("errmsgNotExistChannelAndPlayer");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 指定されたチャンネルが存在しないか、チャンネルが指定されませんでした。
     */
    public static Component errmsgNotExistOrNotSpecified() {
        String msg = resources.getStringOrNull("errmsgNotExistOrNotSpecified");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 指定されたチャンネル名が既に存在します。
     */
    public static Component errmsgExist() {
        String msg = resources.getStringOrNull("errmsgExist");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 指定されたチャンネルに参加していません。
     */
    public static Component errmsgNomember() {
        String msg = resources.getStringOrNull("errmsgNomember");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 指定されたプレイヤーはチャンネルに参加していません。
     */
    public static Component errmsgNomemberOther() {
        String msg = resources.getStringOrNull("errmsgNomemberOther");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 指定されたプレイヤー %player% が見つかりません。
     */
    public static Component errmsgNotfoundPlayer(Object player) {
        String msg = resources.getStringOrNull("errmsgNotfoundPlayer");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%player%", player.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 招待を受けたプレイヤーではありません。
     */
    public static Component errmsgNotInvited() {
        String msg = resources.getStringOrNull("errmsgNotInvited");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * チャンネルが無くなってしまったため、参加できませんでした。
     */
    public static Component errmsgNotfoundChannel() {
        String msg = resources.getStringOrNull("errmsgNotfoundChannel");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 招待された %player% さんは、既にチャンネルに参加しています。
     */
    public static Component errmsgInvitedAlreadyExist(Object player) {
        String msg = resources.getStringOrNull("errmsgInvitedAlreadyExist");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%player%", player.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 既にチャンネルに参加しています。
     */
    public static Component errmsgInvitedAlreadyJoin() {
        String msg = resources.getStringOrNull("errmsgInvitedAlreadyJoin");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 現在チャンネルに参加していません。
     */
    public static Component errmsgNoJoin() {
        String msg = resources.getStringOrNull("errmsgNoJoin");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * あなたはこのチャンネルからBANされています。
     */
    public static Component errmsgBanned() {
        String msg = resources.getStringOrNull("errmsgBanned");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * あなたはこのチャンネルからMuteされているため、発言できません。
     */
    public static Component errmsgMuted() {
        String msg = resources.getStringOrNull("errmsgMuted");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 指定されたプレイヤーは既にBANリストに含まれています。
     */
    public static Component errmsgAlreadyBanned() {
        String msg = resources.getStringOrNull("errmsgAlreadyBanned");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 指定されたプレイヤーは既にMuteリストに含まれています。
     */
    public static Component errmsgAlreadyMuted() {
        String msg = resources.getStringOrNull("errmsgAlreadyMuted");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * このチャンネルは既に非表示になっています。
     */
    public static Component errmsgAlreadyHided() {
        String msg = resources.getStringOrNull("errmsgAlreadyHided");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * このプレイヤーは既に非表示になっています。
     */
    public static Component errmsgAlreadyHidedPlayer() {
        String msg = resources.getStringOrNull("errmsgAlreadyHidedPlayer");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * このチャンネルは非表示になっていません。
     */
    public static Component errmsgAlreadyUnhided() {
        String msg = resources.getStringOrNull("errmsgAlreadyUnhided");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * このプレイヤーは非表示になっていません。
     */
    public static Component errmsgAlreadyUnhidedPlayer() {
        String msg = resources.getStringOrNull("errmsgAlreadyUnhidedPlayer");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 個人チャットチャンネルには参加できません。
     */
    public static Component errmsgCannotJoinPersonal() {
        String msg = resources.getStringOrNull("errmsgCannotJoinPersonal");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * あなたはモデレーターではないため、そのコマンドを実行できません。
     */
    public static Component errmsgNotModerator() {
        String msg = resources.getStringOrNull("errmsgNotModerator");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 指定されたプレイヤーはBANリストに含まれていません。
     */
    public static Component errmsgNotBanned() {
        String msg = resources.getStringOrNull("errmsgNotBanned");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 指定されたプレイヤーはMuteリストに含まれていません。
     */
    public static Component errmsgNotMuted() {
        String msg = resources.getStringOrNull("errmsgNotMuted");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 有効なオプション指定が1つもありませんでした。
     */
    public static Component errmsgInvalidOptions() {
        String msg = resources.getStringOrNull("errmsgInvalidOptions");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * このチャンネルはパスワードが設定されているため入れません。
     */
    public static Component errmsgPassword1() {
        String msg = resources.getStringOrNull("errmsgPassword1");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * パスワードを指定して、チャンネルに入ってください。
     */
    public static Component errmsgPassword2() {
        String msg = resources.getStringOrNull("errmsgPassword2");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * /ch (channel) (password)
     */
    public static Component errmsgPassword3() {
        String msg = resources.getStringOrNull("errmsgPassword3");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * パスワードが正しくないため、チャンネルに入れません。
     */
    public static Component errmsgPasswordNotmatch() {
        String msg = resources.getStringOrNull("errmsgPasswordNotmatch");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 権限 "%permission%" が無いため、実行できません。
     */
    public static Component errmsgPermission(Object permission) {
        String msg = resources.getStringOrNull("errmsgPermission");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%permission%", permission.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * チャンネル %channel% はグローバルチャンネルなので、退出できません。
     */
    public static Component errmsgCannotLeaveGlobal(Object channel) {
        String msg = resources.getStringOrNull("errmsgCannotLeaveGlobal");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * チャンネル %channel% はグローバルチャンネルなので、キックできません。
     */
    public static Component errmsgCannotKickGlobal(Object channel) {
        String msg = resources.getStringOrNull("errmsgCannotKickGlobal");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * チャンネル %channel% はグローバルチャンネルなので、BANできません。
     */
    public static Component errmsgCannotBANGlobal(Object channel) {
        String msg = resources.getStringOrNull("errmsgCannotBANGlobal");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * チャンネル %channel% はグローバルチャンネルなので、削除できません。
     */
    public static Component errmsgCannotRemoveGlobal(Object channel) {
        String msg = resources.getStringOrNull("errmsgCannotRemoveGlobal");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * チャンネル %channel% はグローバルチャンネルなので、モデレーターを設定できません。
     */
    public static Component errmsgCannotModeratorGlobal(Object channel) {
        String msg = resources.getStringOrNull("errmsgCannotModeratorGlobal");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * チャンネル %channel% は強制参加チャンネルなので、退出できません。
     */
    public static Component errmsgCannotLeaveForceJoin(Object channel) {
        String msg = resources.getStringOrNull("errmsgCannotLeaveForceJoin");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * あなたが受信したプライベートメッセージがありません。
     */
    public static Component errmsgNotfoundPM() {
        String msg = resources.getStringOrNull("errmsgNotfoundPM");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 自分自身にプライベートメッセージを送ることはできません。
     */
    public static Component errmsgCannotSendPMSelf() {
        String msg = resources.getStringOrNull("errmsgCannotSendPMSelf");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %channel% はチャンネル名に使用できない文字を含んでいます。
     */
    public static Component errmsgCannotUseForChannel(Object channel) {
        String msg = resources.getStringOrNull("errmsgCannotUseForChannel");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %channel% は短すぎてチャンネル名に使用できません。%min% 文字以上にしてください。
     */
    public static Component errmsgCannotUseForChannelTooShort(Object channel, Object min) {
        String msg = resources.getStringOrNull("errmsgCannotUseForChannelTooShort");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.replace("%min%", min.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %channel% は長すぎてチャンネル名に使用できません。%max% 文字以下にしてください。
     */
    public static Component errmsgCannotUseForChannelTooLong(Object channel, Object max) {
        String msg = resources.getStringOrNull("errmsgCannotUseForChannelTooLong");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        kr.replace("%max%", max.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %word% はグローバルチャンネル名に使用できない文字を含んでいます。
     */
    public static Component errmsgCannotUseForGlobal(Object word) {
        String msg = resources.getStringOrNull("errmsgCannotUseForGlobal");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%word%", word.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %value% はカラーコードとして正しくありません。
     */
    public static Component errmsgInvalidColorCode(Object value) {
        String msg = resources.getStringOrNull("errmsgInvalidColorCode");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%value%", value.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * テンプレート番号は、0から9までの数字を指定してください。
     */
    public static Component errmsgInvalidTemplateNumber() {
        String msg = resources.getStringOrNull("errmsgInvalidTemplateNumber");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 説明文は %max% 文字以下にしてください。
     */
    public static Component errmsgToolongDescription(Object max) {
        String msg = resources.getStringOrNull("errmsgToolongDescription");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%max%", max.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * チャンネル別名は %max% 文字以下にしてください。
     */
    public static Component errmsgToolongAlias(Object max) {
        String msg = resources.getStringOrNull("errmsgToolongAlias");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%max%", max.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * パスワードは %max% 文字以下にしてください。
     */
    public static Component errmsgToolongPassword(Object max) {
        String msg = resources.getStringOrNull("errmsgToolongPassword");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%max%", max.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %key% は true/false で指定してください。
     */
    public static Component errmsgInvalidBooleanOption(Object key) {
        String msg = resources.getStringOrNull("errmsgInvalidBooleanOption");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%key%", key.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * range に正しくない値が指定されました。
     */
    public static Component errmsgInvalidRangeOption() {
        String msg = resources.getStringOrNull("errmsgInvalidRangeOption");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * %key% に指定された %value% は、Japanize変換タイプとして正しくありません。
     */
    public static Component errmsgInvalidJapanizeOption(Object key, Object value) {
        String msg = resources.getStringOrNull("errmsgInvalidJapanizeOption");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%key%", key.toString());
        kr.replace("%value%", value.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * このチャンネルはグローバルチャンネルのため、ブロードキャストをオフにできません。
     */
    public static Component errmsgCannotOffGlobalBroadcast() {
        String msg = resources.getStringOrNull("errmsgCannotOffGlobalBroadcast");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 必須キーワード %key% が指定されていません。
     */
    public static Component errmsgFormatConstraint(Object key) {
        String msg = resources.getStringOrNull("errmsgFormatConstraint");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%key%", key.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * BAN期限(分)の指定が正しくありません。1 から 43200 の間の数値を指定してください。
     */
    public static Component errmsgInvalidBanExpireParameter() {
        String msg = resources.getStringOrNull("errmsgInvalidBanExpireParameter");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * Mute期限(分)の指定が正しくありません。1 から 43200 の間の数値を指定してください。
     */
    public static Component errmsgInvalidMuteExpireParameter() {
        String msg = resources.getStringOrNull("errmsgInvalidMuteExpireParameter");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 自分の発言を非表示にすることはできません。
     */
    public static Component errmsgCannotHideSelf() {
        String msg = resources.getStringOrNull("errmsgCannotHideSelf");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 指定されたチャンネル別名 %aliase% は、チャンネル %channel% と重複するので設定できません。
     */
    public static Component errmsgDuplicatedAlias(Object aliase, Object channel) {
        String msg = resources.getStringOrNull("errmsgDuplicatedAlias");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%aliase%", aliase.toString());
        kr.replace("%channel%", channel.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * 権限がありません&7(%permission%)
     */
    public static Component errmsgNotPermission(Object permission) {
        String msg = resources.getStringOrNull("errmsgNotPermission");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%permission%", permission.toString());
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * このサーバーでは、チャンネルチャットは動作しません。
     */
    public static Component errmsgChannelChatDisabled() {
        String msg = resources.getStringOrNull("errmsgChannelChatDisabled");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.prefix(resources.getString("errorPrefix", ""));
        return kr.toComponent();
    }

    /**
     * &6/%label% join (channel) &7- チャンネルに参加します。
     */
    public static Component usageJoin(Object label) {
        String msg = resources.getStringOrNull("usageJoin");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% leave &7- 参加しているチャンネルから退出します。
     */
    public static Component usageLeave(Object label) {
        String msg = resources.getStringOrNull("usageLeave");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% list &7- チャンネルのリストを表示します。
     */
    public static Component usageList(Object label) {
        String msg = resources.getStringOrNull("usageList");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% invite (name) &7- 指定したプレイヤーをチャンネルチャットに招待します。
     */
    public static Component usageInvite(Object label) {
        String msg = resources.getStringOrNull("usageInvite");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% accept &7- 招待を受けてチャンネルチャットに入室します。
     */
    public static Component usageAccept(Object label) {
        String msg = resources.getStringOrNull("usageAccept");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% deny &7- 招待を拒否します。
     */
    public static Component usageDeny(Object label) {
        String msg = resources.getStringOrNull("usageDeny");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% kick (name) &7- 指定したプレイヤーをチャンネルチャットからキックします。
     */
    public static Component usageKick(Object label) {
        String msg = resources.getStringOrNull("usageKick");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% ban (name) &7- 指定したプレイヤーをチャンネルチャットからBANします。
     */
    public static Component usageBan(Object label) {
        String msg = resources.getStringOrNull("usageBan");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% ban (name) [minutes] &7- 指定したプレイヤーを指定した分の間、BANします。
     */
    public static Component usageBan2(Object label) {
        String msg = resources.getStringOrNull("usageBan2");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% pardon (name) &7- 指定したプレイヤーのBANを解除します。
     */
    public static Component usagePardon(Object label) {
        String msg = resources.getStringOrNull("usagePardon");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% mute (name) &7- 指定したプレイヤーのチャンネルでの発言権を剥奪します。
     */
    public static Component usageMute(Object label) {
        String msg = resources.getStringOrNull("usageMute");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% mute (name) [minutes] &7- 指定したプレイヤーを指定した分の間、発言権剥奪します。
     */
    public static Component usageMute2(Object label) {
        String msg = resources.getStringOrNull("usageMute2");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% unmute (name) &7- 指定したプレイヤーのチャンネルでの発言権剥奪を解除します。
     */
    public static Component usageUnmute(Object label) {
        String msg = resources.getStringOrNull("usageUnmute");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% hide [channel] &7- 指定したチャンネルの発言内容を非表示にします。
     */
    public static Component usageHide(Object label) {
        String msg = resources.getStringOrNull("usageHide");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% hide (player) &7- 指定したプレイヤーの発言内容を非表示にします。
     */
    public static Component usageHidePlayer(Object label) {
        String msg = resources.getStringOrNull("usageHidePlayer");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% unhide [channel] &7- 指定したチャンネルの発言内容を非表示から表示に戻します。
     */
    public static Component usageUnhide(Object label) {
        String msg = resources.getStringOrNull("usageUnhide");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% unhide (player) &7- 指定したプレイヤーの発言内容を非表示から表示に戻します。
     */
    public static Component usageUnhidePlayer(Object label) {
        String msg = resources.getStringOrNull("usageUnhidePlayer");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% info [channel] &7- チャンネルの情報を表示します。
     */
    public static Component usageInfo(Object label) {
        String msg = resources.getStringOrNull("usageInfo");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% log [channel] [p=player] [f=filter] [d=date] [r] &7- チャンネルの発言ログを表示します。
     */
    public static Component usageLog(Object label) {
        String msg = resources.getStringOrNull("usageLog");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% create (channel) [description] &7- チャンネルを作成します。
     */
    public static Component usageCreate(Object label) {
        String msg = resources.getStringOrNull("usageCreate");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% remove [channel] &7- チャンネルを削除します。
     */
    public static Component usageRemove(Object label) {
        String msg = resources.getStringOrNull("usageRemove");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% format [channel] (format...) &7- チャンネルのメッセージフォーマットを設定します。
     */
    public static Component usageFormat(Object label) {
        String msg = resources.getStringOrNull("usageFormat");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% moderator [channel] (player) &7- チャンネルのモデレーターを指定したプレイヤーに設定します。
     */
    public static Component usageModerator(Object label) {
        String msg = resources.getStringOrNull("usageModerator");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% mod [channel] (player) &7- チャンネルのモデレーターを指定したプレイヤーに設定します。
     */
    public static Component usageMod(Object label) {
        String msg = resources.getStringOrNull("usageMod");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% dictionary (add (word) (value)|remove (word)) &7- Japanize変換辞書に新しいワードを登録したり、指定したワードを削除したりします。
     */
    public static Component usageDictionary(Object label) {
        String msg = resources.getStringOrNull("usageDictionary");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% dic (add (word) (value)|remove (word)) &7- Japanize変換辞書に新しいワードを登録したり、指定したワードを削除したりします。
     */
    public static Component usageDic(Object label) {
        String msg = resources.getStringOrNull("usageDic");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% option [channel] (key=value...) &7- チャンネルのオプションを設定します。
     */
    public static Component usageOption(Object label) {
        String msg = resources.getStringOrNull("usageOption");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% template (number) (template...) &7- メッセージフォーマットのテンプレートを登録します。
     */
    public static Component usageTemplate(Object label) {
        String msg = resources.getStringOrNull("usageTemplate");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% check &7- モデレーターがいないチャンネルを一覧します。
     */
    public static Component usageCheck1(Object label) {
        String msg = resources.getStringOrNull("usageCheck1");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% check remove &7- /ch check で一覧されたチャンネルを全て削除します。
     */
    public static Component usageCheck2(Object label) {
        String msg = resources.getStringOrNull("usageCheck2");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% reload &7- config.ymlの再読み込みをします。
     */
    public static Component usageReload(Object label) {
        String msg = resources.getStringOrNull("usageReload");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% help [user|mod|admin] [page] &7- ヘルプを表示します。
     */
    public static Component usageHelp(Object label) {
        String msg = resources.getStringOrNull("usageHelp");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% set default (player) [channel] &7- 指定したプレイヤーの発言先チャンネルを、指定したチャンネルに設定します。
     */
    public static Component usageSet1(Object label) {
        String msg = resources.getStringOrNull("usageSet1");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% (name) [message] &7- 指定したプレイヤーとの個人チャットを開始します。
     */
    public static Component usageMessage(Object label) {
        String msg = resources.getStringOrNull("usageMessage");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% [message] &7- 受信した個人チャットに返信します。
     */
    public static Component usageReply(Object label) {
        String msg = resources.getStringOrNull("usageReply");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% on|off &7- Turn on/off the Japanize conversion of your chat.
     */
    public static Component usageJapanize(Object label) {
        String msg = resources.getStringOrNull("usageJapanize");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &6/%label% (player) on|off &7- Turn on/off the Japanize conversion of other player's chat.
     */
    public static Component usageJapanizeOther(Object label) {
        String msg = resources.getStringOrNull("usageJapanizeOther");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        return kr.toComponent();
    }

    /**
     * &e----- &6LunaChat %type% command (&c%num%&6/&c%max%&6) &e-----
     */
    public static Component usageTop(Object type, Object num, Object max) {
        String msg = resources.getStringOrNull("usageTop");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%type%", type.toString());
        kr.replace("%num%", num.toString());
        kr.replace("%max%", max.toString());
        return kr.toComponent();
    }

    /**
     * &e-----------------------------------------
     */
    public static Component usageFoot() {
        String msg = resources.getStringOrNull("usageFoot");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        return kr.toComponent();
    }

    /**
     * &6次のページを見るには、&c/%label% help %type% %next%&6 と実行してください。
     */
    public static Component usageNoticeNextPage(Object label, Object type, Object next) {
        String msg = resources.getStringOrNull("usageNoticeNextPage");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%label%", label.toString());
        kr.replace("%type%", type.toString());
        kr.replace("%next%", next.toString());
        return kr.toComponent();
    }

    /**
     * 発言先を%channel%にする
     */
    public static Component hoverChannelName(Object channel) {
        String msg = resources.getStringOrNull("hoverChannelName");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%channel%", channel.toString());
        return kr.toComponent();
    }

    /**
     * %player%にプライベートメッセージを送る
     */
    public static Component hoverPlayerName(Object player) {
        String msg = resources.getStringOrNull("hoverPlayerName");
        if ( msg == null ) return Component.empty();
        KeywordReplacer kr = new KeywordReplacer(msg);
        kr.replace("%player%", player.toString());
        return kr.toComponent();
    }
    // === Auto-generated methods area end. ===
}
