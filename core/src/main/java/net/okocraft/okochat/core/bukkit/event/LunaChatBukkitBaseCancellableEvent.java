/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package net.okocraft.okochat.core.bukkit.event;

import org.bukkit.event.Cancellable;

/**
 * 基底のキャンセル可能イベントクラス
 * @author ucchy
 */
public class LunaChatBukkitBaseCancellableEvent extends LunaChatBukkitBaseEvent implements Cancellable {

    private boolean isCancelled;

    /**
     * コンストラクタ
     * @param channelName チャンネル名
     */
    public LunaChatBukkitBaseCancellableEvent(String channelName) {
        super(channelName);
    }

    /**
     * イベントがキャンセルされたかどうかをかえす
     * @see org.bukkit.event.Cancellable#isCancelled()
     */
    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    /**
     * イベントをキャンセルするかどうかを設定する
     * @see org.bukkit.event.Cancellable#setCancelled(boolean)
     */
    @Override
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
}
