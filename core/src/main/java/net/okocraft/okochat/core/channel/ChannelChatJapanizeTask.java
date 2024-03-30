/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package net.okocraft.okochat.core.channel;

import net.okocraft.okochat.core.japanize.JapanizeType;
import net.okocraft.okochat.core.member.ChannelMember;
import net.okocraft.okochat.core.util.ClickableFormat;

/**
 * Japanize2行表示のときに、変換結果を遅延してチャンネルに表示するためのタスク
 * @author ucchy
 */
public class ChannelChatJapanizeTask implements Runnable {

    private Channel channel;
    private ChannelMember player;
    private ClickableFormat lineFormat;

    private JapanizeConvertTask task;

    /**
     * コンストラクタ
     * @param org 変換前の文字列
     * @param type 変換タイプ
     * @param channel 変換後に発言する、発言先チャンネル
     * @param player 発言したプレイヤー
     * @param japanizeFormat 変換後に発言するときの、発言フォーマット
     * @param lineFormat チャンネルのフォーマット
     */
    public ChannelChatJapanizeTask(String org, JapanizeType type, Channel channel,
                                   ChannelMember player, String japanizeFormat, ClickableFormat lineFormat) {

        task = new JapanizeConvertTask(org, type, japanizeFormat, channel, player);
        this.channel = channel;
        this.player = player;
        this.lineFormat = lineFormat;
    }

    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        if ( task.runSync() ) {
            // チャンネルへ送信
            channel.sendMessage(player, task.getResult(), lineFormat, true);
        }
    }
}
