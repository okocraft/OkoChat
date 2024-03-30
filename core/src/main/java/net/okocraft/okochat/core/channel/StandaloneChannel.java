/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package net.okocraft.okochat.core.channel;

import org.jetbrains.annotations.Nullable;

import net.okocraft.okochat.core.member.ChannelMember;
import net.okocraft.okochat.core.util.ClickableFormat;

/**
 *
 * @author ucchy
 */
public class StandaloneChannel extends Channel {

    public StandaloneChannel(String name) {
        super(name);
    }

    @Override
    protected void sendMessage(ChannelMember member, String message,
            @Nullable ClickableFormat format, boolean sendDynmap) {

        // デバッグ表示メッセージ
        System.out.println(String.format("room=%s, member=%s, format=%s, message=%s",
                getName(), member.toString(), format, message));

        // TODO チャンネルのメンバーに送信

    }

    @Override
    protected void log(String message, String name) {

        // デバッグ表示メッセージ
        System.out.println(String.format("room=%s, name=%s, message=%s",
                getName(), name, message));

        // TODO ログファイルを出力
    }
}
