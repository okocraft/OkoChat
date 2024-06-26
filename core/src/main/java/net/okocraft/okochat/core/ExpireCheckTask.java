/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package net.okocraft.okochat.core;

import net.okocraft.okochat.core.channel.Channel;

/**
 * 各チャンネルの期限付きBANや期限付きMuteを、1分間隔で確認しに行くタスク
 * @author ucchy
 */
public class ExpireCheckTask implements Runnable {

    /**
     * 1分ごとに呼び出されるメソッド
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        for ( Channel channel : LunaChat.getAPI().getChannels() ) {
            channel.checkExpires();
        }
    }
}
