package net.okocraft.okochat.platform.velocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.okocraft.okochat.core.platform.Platform;
import net.okocraft.okochat.platform.velocity.sender.VelocityConsoleSender;
import net.okocraft.okochat.platform.velocity.sender.VelocityPlayerSender;
import org.jetbrains.annotations.NotNullByDefault;
import org.slf4j.Logger;

import java.nio.file.Path;

@NotNullByDefault
public record VelocityPlatform(Logger logger, Path dataDirectory, VelocityConsoleSender consoleSender) implements Platform {

    public static VelocityPlatform initialize(ProxyServer server, Logger logger, Path dataDirectory) {
        return new VelocityPlatform(
                logger,
                dataDirectory,
                new VelocityConsoleSender(server.getConsoleCommandSource())
        );
    }

    public VelocityPlayerSender createPlayerSender(Player player) {
        return new VelocityPlayerSender(player);
    }

}
