/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3;

import java.io.File;

import com.github.ucchyocean.lc3.event.EventSenderInterface;

/**
 * LunaChat
 * @author ucchy
 */
public class LunaChat {

    private static PluginInterface instance;
    private static EventSenderInterface esender;

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

    // LunaChatのイベント実行クラスを取得する
    static void setEventSender(EventSenderInterface eventSender) {
        esender = eventSender;
    }

    /**
     * LunaChatのイベント実行クラスを取得する
     * @return イベント実行クラス
     */
    public static EventSenderInterface getEventSender() {
        return esender;
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
     * LunaChatの通常チャットロガーを取得する
     * @return 通常チャットロガー
     */
    public static LunaChatLogger getNormalChatLogger() {
        return instance.getNormalChatLogger();
    }

    /**
     * LunaChatのUUIDキャッシュを取得する
     * @return UUIDキャッシュ
     */
    public static UUIDCacheData getUUIDCacheData() {
        return instance.getUUIDCacheData();
    }

    /**
     * LunaChatで非同期タスクを実行する
     * @param task 実行するタスク
     */
    public static void runAsyncTask(Runnable task) {
        instance.runAsyncTask(task);
    }
}