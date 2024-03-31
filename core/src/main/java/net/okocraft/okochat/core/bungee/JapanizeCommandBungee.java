/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package net.okocraft.okochat.core.bungee;

import com.github.ucchyocean.lc3.command.LunaChatJapanizeCommand;
import com.github.ucchyocean.lc3.member.ChannelMember;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

/**
 * Japanizeコマンドの処理クラス（Bungee実装）
 * @author ucchy
 */
public class JapanizeCommandBungee extends Command {

    private LunaChatJapanizeCommand command;

    /**
     * コンストラクタ
     * @param name
     * @param permission
     * @param aliases
     */
    public JapanizeCommandBungee(String name, String permission, String... aliases) {
        super(name, permission, aliases);
        command = new LunaChatJapanizeCommand();
    }

    /**
     * コマンドを実行したときに呼び出されるメソッド
     * @param sender 実行者
     * @param args 実行されたコマンドの引数
     * @see net.md_5.bungee.api.plugin.Command#execute(net.md_5.bungee.api.CommandSender, String[])
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        command.execute(ChannelMember.getChannelMember(sender), "jp", args);
    }

}
