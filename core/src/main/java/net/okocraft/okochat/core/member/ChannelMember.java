/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package net.okocraft.okochat.core.member;

import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.TriState;

/**
 * チャンネルメンバーの抽象クラス
 * @author ucchy
 */
@Deprecated(forRemoval = true)
public interface ChannelMember {

    /**
     * プレイヤー名を返す
     * @return プレイヤー名
     */
    String getName();

    /**
     * プレイヤーのUUIDを返す
     * @return プレイヤーのUUID
     */
    UUID getUniqueId();

    /**
     * メッセージを送る
     * @param message 送るメッセージ
     */
    void sendMessage(String message);

    /**
     * メッセージを送る
     * @param message 送るメッセージ
     */
    void sendMessage(Component message);

    /**
     * 指定されたパーミッションノードの権限を持っているかどうかを取得する
     * @param node パーミッションノード
     * @return 権限を持っているかどうか
     */
    boolean hasPermission(String node);

    // isPermissionSet(node) && !hasPermission(node) = checkPermission().toBooleanOrElse(true) = default allowed
    // isPermissionSet(node) && hasPermission(node) = checkPermission().toBooleanOrElse(false) = default disallowed
    TriState checkPermission(String node); // TODO: enhanced permission checking by Permissible#permissionValue in Paper or PermissionSubject#getPermissionValue in Velocity

}
