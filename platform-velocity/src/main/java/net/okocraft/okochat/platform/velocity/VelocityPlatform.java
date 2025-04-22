package net.okocraft.okochat.platform.velocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.okocraft.okochat.platform.velocity.sender.VelocityConsoleSender;
import net.okocraft.okochat.platform.velocity.sender.VelocityPlayerSender;
import org.jetbrains.annotations.NotNullByDefault;

@NotNullByDefault
public record VelocityPlatform(VelocityConsoleSender consoleSender) {

    public static VelocityPlatform initialize(ProxyServer server) {
        return new VelocityPlatform(
                new VelocityConsoleSender(server.getConsoleCommandSource())
        );
    }

    public VelocityPlayerSender createPlayerSender(Player player) {
        return new VelocityPlayerSender(player);
    }

}
