/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package net.okocraft.okochat.core;

import java.io.File;
import java.util.Set;
import org.slf4j.Logger;

/**
 * プラグインインターフェイス
 * @author ucchy
 */
public interface PluginInterface {

    /**
     * このプラグインのJarファイル自身を示すFileクラスを返す。
     * @return Jarファイル
     */
    public File getPluginJarFile();

    /**
     * LunaChatConfigを取得する
     * @return LunaChatConfig
     */
    public LunaChatConfig getLunaChatConfig();

    /**
     * LunaChatAPIを取得する
     * @return LunaChatAPI
     */
    public LunaChatAPI getLunaChatAPI();

    /**
     * プラグインのデータ格納フォルダを取得する
     * @return データ格納フォルダ
     */
    public File getDataFolder();

    /**
     * オンラインのプレイヤー名一覧を取得する
     * @return オンラインのプレイヤー名一覧
     */
    public Set<String> getOnlinePlayerNames();
    // okocraft start - add name stream
    java.util.stream.Stream<String> getOnlinePlayerNameStream();
    // okocraft end

    /**
     * UUIDキャッシュデータを取得する
     * @return UUIDキャッシュデータ
     */
    public UUIDCacheData getUUIDCacheData();

    /**
     * 非同期タスクを実行する
     * @param task タスク
     */
    public void runAsyncTask(Runnable task);
}
