package net.okocraft.okochat.platform.velocity.sender;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.TriState;
import net.okocraft.okochat.api.sender.PlayerSender;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.List;
import java.util.UUID;

@NotNullByDefault
public record VelocityPlayerSender(Player player) implements PlayerSender {

    @Override
    public UUID uuid() {
        return this.player.getUniqueId();
    }

    @Override
    public String name() {
        return this.player.getUsername();
    }

    @Override
    public boolean hasPermission(String permissionNode) {
        return this.player.hasPermission(permissionNode);
    }

    @Override
    public TriState getPermissionValue(String permissionNode) {
        return this.player.getPermissionValue(permissionNode).toAdventureTriState();
    }

    @Override
    public Iterable<? extends Audience> audiences() {
        return List.of(this.player);
    }

    @Override
    public void sendMessage(Component component) {
        this.player.sendMessage(component);
    }

    @Override
    public Identity identity() {
        return this.player.identity();
    }
}
