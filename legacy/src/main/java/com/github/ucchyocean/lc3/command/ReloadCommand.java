/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3.command;

import java.io.File;

import com.github.ucchyocean.lc3.LunaChat;
import com.github.ucchyocean.lc3.Messages;
import com.github.ucchyocean.lc3.member.ChannelMember;

/**
 * reloadコマンドの実行クラス
 * @author ucchy
 */
public class ReloadCommand extends LunaChatSubCommand {

    private static final String COMMAND_NAME = "reload";
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
    public void sendUsageMessage(
            ChannelMember sender, String label) {
        sender.sendMessage(Messages.usageReload(label));
    }

    /**
     * コマンドを実行します。
     * @param sender コマンド実行者
     * @param label 実行ラベル
     * @param args 実行時の引数
     * @return コマンドが実行されたかどうか
     * @see LunaChatSubCommand#runCommand(String[])
     */
    @Override
    public boolean runCommand(
            ChannelMember sender, String label, String[] args) {

        api.reloadAllData();
        config.reloadConfig(LunaChat.getDataFolder(), LunaChat.getPluginJarFile());
        Messages.initialize(new File(LunaChat.getDataFolder(), "messages"),
                LunaChat.getPluginJarFile(), config.getLang());
        sender.sendMessage(Messages.cmdmsgReload());
        return true;
    }

}
