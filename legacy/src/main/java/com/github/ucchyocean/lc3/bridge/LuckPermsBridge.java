/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3.bridge;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import java.util.UUID;

/**
 * LuckPerms連携クラス
 * @author ucchy
 */
public class LuckPermsBridge {

    private LuckPerms api;

    // コンストラクタは外から利用不可
    private LuckPermsBridge(LuckPerms api) {
        this.api = api;
    }

    /**
     * LuckPermsをロードする
     * @return ロードされたLuckPermsBridge
     */
    public static LuckPermsBridge load() {
        // ロードされているならLuckPermsBridgeのインスタンスを作成して返す
        return new LuckPermsBridge(LuckPermsProvider.get());
    }

    /**
     * プレイヤーのprefixを取得します。
     * @param uniqueId プレイヤーのUUID
     * @return プレイヤーのprefix
     */
    public String getPlayerPrefix(UUID uniqueId) {
        User user = api.getUserManager().getUser(uniqueId);
        String prefix = user.getCachedData().getMetaData().getPrefix();
        return (prefix != null) ? prefix : "";
    }

    /**
     * プレイヤーのsuffixを取得します。
     * @param uniqueId プレイヤーのUUID
     * @return プレイヤーのsuffix
     */
    public String getPlayerSuffix(UUID uniqueId) {
        User user = api.getUserManager().getUser(uniqueId);
        String suffix = user.getCachedData().getMetaData().getSuffix();
        return (suffix != null) ? suffix : "";
    }
}
