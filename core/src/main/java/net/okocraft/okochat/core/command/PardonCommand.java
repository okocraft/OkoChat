/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package net.okocraft.okochat.core.command;

import net.okocraft.okochat.core.Messages;
import net.okocraft.okochat.core.channel.Channel;
import net.okocraft.okochat.core.member.ChannelMember;

import java.util.Optional;
import java.util.UUID;

/**
 * pardonコマンドの実行クラス
 * @author ucchy
 */
public class PardonCommand extends LunaChatSubCommand {

    private static final String COMMAND_NAME = "pardon";
    private static final String PERMISSION_NODE = "lunachat." + COMMAND_NAME;

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
     * パーミッションノードを取得します。
     * @return パーミッションノード
     * @see LunaChatSubCommand#getPermissionNode()
     */
    @Override
    public String getPermissionNode() {
        return PERMISSION_NODE;
    }

    /**
     * コマンドの種別を取得します。
     * @return コマンド種別
     * @see LunaChatSubCommand#getCommandType()
     */
    @Override
    public CommandType getCommandType() {
        return CommandType.MODERATOR;
    }

    /**
     * 使用方法に関するメッセージをsenderに送信します。
     * @param sender コマンド実行者
     * @param label 実行ラベル
     * @see LunaChatSubCommand#sendUsageMessage()
     */
    @Override
    public void sendUsageMessage(
            ChannelMember sender, String label) {
        sender.sendMessage(Messages.usagePardon(label));
    }

    /**
     * コマンドを実行します。
     * @param sender コマンド実行者
     * @param label 実行ラベル
     * @param args 実行時の引数
     * @return コマンドが実行されたかどうか
     * @see LunaChatSubCommand#runCommand(java.lang.String[])
     */
    @Override
    public boolean runCommand(
            ChannelMember sender, String label, String[] args) {

        // 実行引数から、BAN解除するユーザーを取得する
        String kickedName = "";
        if (args.length >= 2) {
            kickedName = args[1];
        } else {
            sender.sendMessage(Messages.errmsgCommand());
            return true;
        }

        // 対象チャンネルを取得、取得できない場合はエラー表示して終了する
        Channel channel = null;
        if (args.length >= 3) {
            channel = api.getChannel(args[2]);
        } else {
            channel = api.getDefaultChannel(sender.getName());
        }
        if (channel == null) {
            sender.sendMessage(Messages.errmsgNoJoin());
            return true;
        }

        // モデレーターかどうか確認する
        if ( !channel.hasModeratorPermission(sender) ) {
            sender.sendMessage(Messages.errmsgNotModerator());
            return true;
        }

        // BAN解除されるプレイヤーがBANされているかどうかチェックする
        UUID kicked = api.getUserProvider().lookupUuid(kickedName);

        if (kicked == null) {
            sender.sendMessage(Messages.errmsgNotfoundPlayer(kickedName));
            return true;
        }

        // BAN解除実行
        if (!channel.unban(kicked)) { // If false, the user is already banned
            sender.sendMessage(Messages.errmsgNotBanned());
            return true;
        }

        // senderに通知メッセージを出す
        sender.sendMessage(Messages.cmdmsgPardon(kickedName, channel.getName()));

        // チャンネルに通知メッセージを出す
        channel.sendSystemMessage(Messages.pardonMessage(
                channel.getColorCode(), channel.getName(), kickedName),
                true, "system");

        // BANされていた人に通知メッセージを出す
        String channelName = channel.getName();
        Optional.ofNullable(api.getChannelMemberProvider().getByUniqueId(kicked))
                .ifPresent(member -> member.sendMessage(Messages.cmdmsgPardoned(channelName)));

        return true;
    }
}
