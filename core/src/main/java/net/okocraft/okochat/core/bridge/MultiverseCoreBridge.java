/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package net.okocraft.okochat.core.bridge;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.Core;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

/**
 * MultiverseCore連携クラス
 * @author ucchy
 */
public class MultiverseCoreBridge {

    /** MultiverseCore API クラス */
    private Core mvc;

    /** コンストラクタは使用不可 */
    private MultiverseCoreBridge() {
    }

    /**
     * MultiverseCore-apiをロードする
     * @param plugin MultiverseCoreのプラグインインスタンス
     * @param ロードしたかどうか
     */
    public static MultiverseCoreBridge load(Plugin plugin) {

        if ( plugin instanceof MultiverseCore ) {
            MultiverseCoreBridge bridge = new MultiverseCoreBridge();
            bridge.mvc = (Core)plugin;
            return bridge;
        } else {
            return null;
        }
    }

    /**
     * 指定されたワールドのエイリアス名を取得する
     * @param worldName ワールド名
     * @return エイリアス名、取得できない場合はnullが返される
     */
    public String getWorldAlias(String worldName) {

        MultiverseWorld mvworld = mvc.getMVWorldManager().getMVWorld(worldName);
        if ( mvworld != null ) {
            if ( mvworld.getAlias().length() > 0 ) {
                return mvworld.getAlias();
            } else {
                return mvworld.getName();
            }
        } else {
            return null;
        }
    }

    /**
     * 指定されたワールドのエイリアス名を取得する
     * @param world ワールド
     * @return エイリアス名、取得できない場合はnullが返される
     */
    public String getWorldAlias(World world) {

        MultiverseWorld mvworld = mvc.getMVWorldManager().getMVWorld(world);
        if ( mvworld != null ) {
            if ( mvworld.getAlias().length() > 0 ) {
                return mvworld.getAlias();
            } else {
                return mvworld.getName();
            }
        } else {
            return null;
        }
    }
}
