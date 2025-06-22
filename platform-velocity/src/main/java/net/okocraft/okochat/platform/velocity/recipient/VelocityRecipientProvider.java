package net.okocraft.okochat.platform.velocity.recipient;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.okocraft.okochat.api.recipient.Recipient;
import net.okocraft.okochat.api.recipient.RecipientProvider;
import net.okocraft.okochat.platform.velocity.sender.VelocityConsoleSender;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.UUID;

@NotNullByDefault
public class VelocityRecipientProvider implements RecipientProvider {

    private final ProxyServer proxy;
    private final VelocityConsoleSender console;

    public VelocityRecipientProvider(ProxyServer proxy) {
        this.proxy = proxy;
        this.console = new VelocityConsoleSender(proxy.getConsoleCommandSource());
    }

    @Override
    public Recipient getConsole() {
        return this.console;
    }

    @Override
    public Recipient getByUUID(UUID uuid) {
        return this.proxy.getPlayer(uuid).map(VelocityRecipientProvider::createFromPlayer).orElse(Recipient.nullRecipient());
    }

    @Override
    public Recipient getByName(String name) {
        return this.proxy.getPlayer(name).map(VelocityRecipientProvider::createFromPlayer).orElse(Recipient.nullRecipient());
    }

    private static Recipient createFromPlayer(Player player) {
        return Recipient.create(player.identity(), player);
    }
}
