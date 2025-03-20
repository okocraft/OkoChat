package net.okocraft.okochat.api.sender;

import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.UUID;

/**
 * An interface that represents the subject that sent the chat or command.
 */
@NotNullByDefault
public interface Sender extends Identified {

    UUID uuid();

    String name();

    boolean hasPermission(String permissionNode);

    TriState getPermissionValue(String permissionNode);

    void sendMessage(Component component);

}
