/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package net.okocraft.okochat.core;

import java.io.File;

import net.okocraft.okochat.core.event.LunaChatEventCallerProvider;

/**
 * LunaChat
 * @author ucchy
 */
public class LunaChat {

    /** Bukkit→BungeeCord チャット発言内容の転送に使用するプラグインメッセージチャンネル名 */
    public static final String PMC_MESSAGE = "lunachat:message";

    private static PluginInterface instance;

    // LunaChatに実行元プラグインクラスを設定する
    static void setPlugin(PluginInterface plugin) {
        instance = plugin;
    }

    /**
     * LunaChatのプラグインクラスを取得する
     * @return プラグインクラス、BukkitモードならLunaChatBukkit、BungeeCordモードならLunaChatBungee
     */
    public static PluginInterface getPlugin() {
        return instance;
    }

    public static LunaChatEventCallerProvider getEventService() { // FIXME: this should be here? For now, as an alternative to #getEventSender.
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * LunaChatのデータ格納フォルダを取得する
     * @return データ格納フォルダ
     */
    public static File getDataFolder() {
        return instance.getDataFolder();
    }

    /**
     * LunaChatのJarファイルを取得する
     * @return Jarファイル
     */
    public static File getPluginJarFile() {
        return instance.getPluginJarFile();
    }

    /**
     * LunaChatのコンフィグを取得する
     * @return コンフィグ
     */
    public static LunaChatConfig getConfig() {
        return instance.getLunaChatConfig();
    }

    /**
     * LunaChatのAPIを取得する
     * @return API
     */
    public static LunaChatAPI getAPI() {
        return instance.getLunaChatAPI();
    }

    /**
     * LunaChatで非同期タスクを実行する
     * @param task 実行するタスク
     */
    public static void runAsyncTask(Runnable task) {
        instance.runAsyncTask(task);
    }
}