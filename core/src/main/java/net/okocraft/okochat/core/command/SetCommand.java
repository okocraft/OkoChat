package net.okocraft.okochat.core.command;

import net.okocraft.okochat.core.Messages;
import net.okocraft.okochat.core.channel.Channel;
import net.okocraft.okochat.core.member.ChannelMember;

import java.util.Optional;

public class SetCommand extends LunaChatSubCommand {

    private static final String COMMAND_NAME = "set";
    private static final String PERMISSION_NODE = "lunachat-admin." + COMMAND_NAME;

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
        return CommandType.ADMIN;
    }

    /**
     * 使用方法に関するメッセージをsenderに送信します。
     * @param sender コマンド実行者
     * @param label 実行ラベル
     * @see LunaChatSubCommand#sendUsageMessage()
     */
    @Override
    public void sendUsageMessage(ChannelMember sender, String label) {
        sender.sendMessage(Messages.usageSet1(label));
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
    public boolean runCommand(ChannelMember sender, String label, String[] args) {

        // 引数チェック
        // このコマンドは、コンソールでも実行できる

        if ( args.length >= 3 && args[1].equalsIgnoreCase("default") ) {
            // 「/ch set default (player名) [channel名]」の実行

            String targetPlayer = args[2];

            Channel targetChannel = null;
            if ( args.length >= 4 ) {
                targetChannel = api.getChannel(args[3]);
            } else {
                targetChannel = api.getDefaultChannel(sender.getName());
            }

            if ( targetChannel == null ) {
                // チャンネルが正しく指定されなかった
                sender.sendMessage(Messages.errmsgNotExistOrNotSpecified());
                return true;
            }

            // 発言先を設定
            api.setDefaultChannel(targetPlayer, targetChannel.getName());

            sender.sendMessage(Messages.cmdmsgSetDefault(targetPlayer, targetChannel.getName()));

            // setされる相手のプレイヤーにも通知する
            String channelName = targetChannel.getName();
            Optional.ofNullable(api.getChannelMemberProvider().getByName(targetPlayer))
                    .ifPresent(member -> member.sendMessage(Messages.cmdmsgSet(channelName)));

            return true;
        }

        return false;
    }

}
