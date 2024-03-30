/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package net.okocraft.okochat.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.logging.Level;

import org.bstats.bungeecord.Metrics;

import net.okocraft.okochat.core.bridge.BungeePermsBridge;
import net.okocraft.okochat.core.bridge.LuckPermsBridge;
import net.okocraft.okochat.core.bungee.BungeeEventListener;
import net.okocraft.okochat.core.bungee.BungeeEventSender;
import net.okocraft.okochat.core.bungee.JapanizeCommandBungee;
import net.okocraft.okochat.core.bungee.LunaChatCommandBungee;
import net.okocraft.okochat.core.bungee.MessageCommandBungee;
import net.okocraft.okochat.core.bungee.ReplyCommandBungee;
import net.okocraft.okochat.core.channel.ChannelManager;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * LunaChatのBungeeCord実装
 * @author ucchy
 */
public class LunaChatBungee extends Plugin implements PluginInterface {

    private static LunaChatBungee instance;

    private HashMap<String, String> history;
    private LunaChatConfig config;
    private ChannelManager manager;
    private UUIDCacheData uuidCacheData;
    private LunaChatLogger normalChatLogger;

    private BungeePermsBridge bungeeperms;
    private LuckPermsBridge luckperms;

    /**
     * プラグインが有効化されたときに呼び出されるメソッド
     * @see net.md_5.bungee.api.plugin.Plugin#onEnable()
     */
    @Override
    public void onEnable() {

        LunaChat.setPlugin(this);
        LunaChat.setMode(LunaChatMode.BUNGEE);

        // Metrics
        Metrics metrics = new Metrics(this, 7936);
        metrics.addCustomChart(new org.bstats.charts.DrilldownPie(
                "minecraft_server_version", new Callable<Map<String, Map<String, Integer>>>() {
            public Map<String, Map<String, Integer>> call() throws Exception {
                Map<String, Map<String, Integer>> map = new HashMap<>();
                Map<String, Integer> sub = new HashMap<>();
                sub.put(getProxy().getVersion(), 1);
                map.put(getProxy().getName(), sub);
                return map;
            }
        }));

        // 初期化
        config = new LunaChatConfig(getDataFolder(), getFile());
        uuidCacheData = new UUIDCacheData(getDataFolder());
        Messages.initialize(new File(getDataFolder(), "messages"), getFile(), config.getLang());
        history = new HashMap<String, String>();

        manager = new ChannelManager();
        normalChatLogger = new LunaChatLogger("==normalchat");

        // チャンネルチャット無効なら、デフォルト発言先をクリアする
        if ( !config.isEnableChannelChat() ) {
            manager.removeAllDefaultChannels();
        }

        // BungeePermsのロード
        Plugin temp = getProxy().getPluginManager().getPlugin("BungeePerms");
        if ( temp != null ) {
            bungeeperms = BungeePermsBridge.load(temp);
        }

        // LuckPermsのロード
        temp = getProxy().getPluginManager().getPlugin("LuckPerms");
        if ( temp != null ) {
            luckperms = LuckPermsBridge.load(temp);
        }

        // コマンド登録
        getProxy().getPluginManager().registerCommand(this,
                new LunaChatCommandBungee("lunachat", "", "lc", "ch"));
        getProxy().getPluginManager().registerCommand(this,
                new MessageCommandBungee("tell", "", "msg", "message", "t", "w"));
        getProxy().getPluginManager().registerCommand(this,
                new ReplyCommandBungee("reply", "", "r"));
        getProxy().getPluginManager().registerCommand(this,
                new JapanizeCommandBungee("japanize", "", "jp"));

        // リスナー登録
        getProxy().getPluginManager().registerListener(this, new BungeeEventListener(this));

        // イベント実行クラスの登録
        LunaChat.setEventSender(new BungeeEventSender());

        // プラグインチャンネル登録
        getProxy().registerChannel(LunaChat.PMC_MESSAGE);
    }

    /**
     * LunaChatのインスタンスを返す
     * @return LunaChat
     */
    public static LunaChatBungee getInstance() {
        if ( instance == null ) {
            instance = (LunaChatBungee)ProxyServer.getInstance().getPluginManager().getPlugin("LunaChat");
        }
        return instance;
    }

    /**
     * コンフィグを返す
     * @return コンフィグ
     */
    public LunaChatConfig getConfig() {
        return config;
    }

    /**
     * プライベートメッセージの受信履歴を記録する
     * @param reciever 受信者
     * @param sender 送信者
     */
    protected void putHistory(String reciever, String sender) {
        history.put(reciever, sender);
    }

    /**
     * プライベートメッセージの受信履歴を取得する
     * @param reciever 受信者
     * @return 送信者
     */
    protected String getHistory(String reciever) {
        return history.get(reciever);
    }

    /**
     * このプラグインのJarファイル自身を示すFileクラスを返す。
     * @return Jarファイル
     * @see PluginInterface#getPluginJarFile()
     */
    @Override
    public File getPluginJarFile() {
        return getFile();
    }

    /**
     * LunaChatConfigを取得する
     * @return LunaChatConfig
     * @see PluginInterface#getLunaChatConfig()
     */
    @Override
    public LunaChatConfig getLunaChatConfig() {
        return config;
    }

    /**
     * LunaChatAPIを取得する
     * @return LunaChatAPI
     * @see PluginInterface#getLunaChatAPI()
     */
    @Override
    public LunaChatAPI getLunaChatAPI() {
        return manager;
    }

    /**
     * 通常チャット用のロガーを返す
     * @return normalChatLogger
     */
    @Override
    public LunaChatLogger getNormalChatLogger() {
        return normalChatLogger;
    }

    /**
     * オンラインのプレイヤー名一覧を取得する
     * @return オンラインのプレイヤー名一覧
     */
    @Override
    public Set<String> getOnlinePlayerNames() {
        Set<String> list = new TreeSet<>();
        for ( ProxiedPlayer p : ProxyServer.getInstance().getPlayers() ) {
            list.add(p.getName());
        }
        return list;
    }
    // okocraft start - add name stream
    @Override
    public java.util.stream.Stream<String> getOnlinePlayerNameStream() {
        return ProxyServer.getInstance().getPlayers().stream()
                .map(ProxiedPlayer::getName)
                .sorted(java.util.Comparator.comparing(String::length).reversed());
    }
    // okocraft end

    /**
     * このプラグインのログを記録する
     * @param level ログレベル
     * @param msg ログメッセージ
     */
    @Override
    public void log(Level level, String msg) {
        getLogger().log(level, msg);
    }

    /**
     * UUIDキャッシュデータを取得する
     * @return UUIDキャッシュデータ
     * @see PluginInterface#getUUIDCacheData()
     */
    @Override
    public UUIDCacheData getUUIDCacheData() {
        return uuidCacheData;
    }

    /**
     * 非同期タスクを実行する
     * @param task タスク
     * @see PluginInterface#runAsyncTask(java.lang.Runnable)
     */
    @Override
    public void runAsyncTask(Runnable task) {
        ProxyServer.getInstance().getScheduler().runAsync(this, task);
    }

    /**
     * BungeePerms連携クラスを取得する
     * @return BungeePerms連携クラス
     */
    public BungeePermsBridge getBungeePerms() {
        return bungeeperms;
    }

    /**
     * LuckPerms連携クラスを取得する
     * @return LuckPerms連携クラス
     */
    public LuckPermsBridge getLuckPerms() {
        return luckperms;
    }
    // okocraft start - Set the default setting of japanize to false for those who do not use Japanese as a client language
    public boolean isUsingJapanese(String playerName) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
        return player != null && java.util.Locale.JAPANESE.equals(player.getLocale());
    }
    // okocraft end
}
