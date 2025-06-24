package net.okocraft.okochat.platform.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import net.okocraft.okochat.api.chat.recipient.RecipientProvider;
import net.okocraft.okochat.core.data.legacy.LegacyChannelMemberResolver;
import net.okocraft.okochat.core.platform.Platform;
import net.okocraft.okochat.platform.velocity.data.legacy.VelocityLegacyChannelMemberResolver;
import net.okocraft.okochat.platform.velocity.recipient.VelocityRecipientProvider;
import org.jetbrains.annotations.NotNullByDefault;
import org.slf4j.Logger;

import java.nio.file.Path;

@NotNullByDefault
public record VelocityPlatform(
        Logger logger, Path dataDirectory, VelocityScheduler scheduler,
        RecipientProvider recipientProvider, LegacyChannelMemberResolver legacyChannelMemberResolver
) implements Platform {

    public static VelocityPlatform initialize(ProxyServer server, Logger logger, Path dataDirectory, VelocityScheduler scheduler) {
        return new VelocityPlatform(
                logger,
                dataDirectory,
                scheduler,
                new VelocityRecipientProvider(server),
                new VelocityLegacyChannelMemberResolver()
        );
    }

}
