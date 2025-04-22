package net.okocraft.okochat.platform.velocity.sender;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import net.kyori.adventure.text.Component;
import net.okocraft.okochat.api.sender.ConsoleSender;
import org.jetbrains.annotations.NotNullByDefault;

@NotNullByDefault
public record VelocityConsoleSender(ConsoleCommandSource console) implements ConsoleSender {
    @Override
    public void sendMessage(Component component) {
        this.console.sendMessage(component);
    }
}
