/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package net.okocraft.okochat.core.bungee;

import com.github.ucchyocean.lc3.command.LunaChatMessageCommand;
import com.github.ucchyocean.lc3.member.ChannelMember;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

/**
 * Tellコマンドの処理クラス（Bungee実装）
 * @author ucchy
 */
public class MessageCommandBungee extends Command implements TabExecutor {

    private LunaChatMessageCommand command;

    /**
     * コンストラクタ
     * @param name
     * @param permission
     * @param aliases
     */
    public MessageCommandBungee(String name, String permission, String... aliases) {
        super(name, permission, aliases);
        command = new LunaChatMessageCommand();
    }

    /**
     * コマンドを実行したときに呼び出されるメソッド
     * @param sender 実行者
     * @param args 実行されたコマンドの引数
     * @see net.md_5.bungee.api.plugin.Command#execute(net.md_5.bungee.api.CommandSender, String[])
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        command.execute(ChannelMember.getChannelMember(sender), "m", args);
    }

    /**
     * TABキー補完が実行されたときに呼び出されるメソッド
     * @param sender 実行者
     * @param args 実行されたコマンドの引数
     * @return 補完候補
     * @see net.md_5.bungee.api.plugin.TabExecutor#onTabComplete(net.md_5.bungee.api.CommandSender, String[])
     */
    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return command.onTabComplete(ChannelMember.getChannelMember(sender), "m", args);
    }
}
