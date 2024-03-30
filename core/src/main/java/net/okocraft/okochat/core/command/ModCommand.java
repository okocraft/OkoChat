/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package net.okocraft.okochat.core.command;

import net.okocraft.okochat.core.Messages;
import net.okocraft.okochat.core.member.ChannelMember;

/**
 * moderatorコマンドのエイリアス実行クラス、名前のみが異なるが、他は全て一緒。
 * @author ucchy
 */
public class ModCommand extends ModeratorCommand {

    private static final String COMMAND_NAME = "mod";

    /**
     * コマンドを取得します。
     * @return コマンド
     * @see LunaChatSubCommand#getCommandName()
     */
    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    /**
     * 使用方法に関するメッセージをsenderに送信します。
     * @param sender コマンド実行者O
     * @param label 実行ラベル
     * @see LunaChatSubCommand#sendUsageMessage()
     */
    @Override
    public void sendUsageMessage(
            ChannelMember sender, String label) {
        sender.sendMessage(Messages.usageMod(label));
    }
}
