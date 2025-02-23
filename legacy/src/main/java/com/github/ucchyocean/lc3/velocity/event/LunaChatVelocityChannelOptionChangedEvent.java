/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3.velocity.event;

import com.github.ucchyocean.lc3.member.ChannelMember;

import java.util.HashMap;
import java.util.Map;

/**
 * オプション変更イベント
 * @author ucchy
 */
public class LunaChatVelocityChannelOptionChangedEvent extends LunaChatVelocityBaseCancellableEvent {

    private ChannelMember member;
    private Map<String, String> options;

    public LunaChatVelocityChannelOptionChangedEvent(String channelName,
                                                     ChannelMember member, Map<String, String> options) {
        super(channelName);
        this.member = member;
        this.options = options;
    }

    /**
     * 変更後のオプションリストをかえす
     * @return オプションリスト
     */
    public Map<String, String> getOptions() {
        return options;
    }

    /**
     * オプションリストを上書き設定する
     * @param options オプションリスト
     */
    public void setOptions(HashMap<String, String> options) {
        this.options = options;
    }

    /**
     * チャンネルのオプションを変更した人を取得する。
     * @return チャンネルのオプションを変更したChannelMember
     */
    public ChannelMember getMember() {
        return member;
    }
}
