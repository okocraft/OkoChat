/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package net.okocraft.okochat.core.bukkit.event;

import com.github.ucchyocean.lc3.LunaChat;
import com.github.ucchyocean.lc3.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * 基底イベントクラス
 * @author ucchy
 */
public abstract class LunaChatBukkitBaseEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    protected String channelName;

    /**
     * コンストラクタ
     * @param channelName チャンネル名
     */
    public LunaChatBukkitBaseEvent(String channelName) {
        super(!Bukkit.isPrimaryThread());
        this.channelName = channelName;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * チャンネル名を取得する
     * @return チャンネル名
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * チャンネルを取得する
     * @return チャンネル
     */
    public Channel getChannel() {
        return LunaChat.getAPI().getChannel(channelName);
    }
}
