package net.okocraft.okochat.platform.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import net.okocraft.okochat.api.chat.recipient.RecipientProvider;
import net.okocraft.okochat.core.platform.Platform;
import net.okocraft.okochat.platform.velocity.recipient.VelocityRecipientProvider;
import org.jetbrains.annotations.NotNullByDefault;
import org.slf4j.Logger;

import java.nio.file.Path;

@NotNullByDefault
public record VelocityPlatform(Logger logger, Path dataDirectory, RecipientProvider recipientProvider) implements Platform {

    public static VelocityPlatform initialize(ProxyServer server, Logger logger, Path dataDirectory) {
        return new VelocityPlatform(
                logger,
                dataDirectory,
                new VelocityRecipientProvider(server)
        );
    }

}
