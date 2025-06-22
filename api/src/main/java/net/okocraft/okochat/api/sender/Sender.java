package net.okocraft.okochat.api.sender;

import net.kyori.adventure.util.TriState;
import net.okocraft.okochat.api.chat.recipient.Recipient;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.UUID;

/**
 * An interface that represents the subject that sent the chat or command.
 */
@NotNullByDefault
public interface Sender extends Recipient {

    UUID uuid();

    String name();

    boolean hasPermission(String permissionNode);

    TriState getPermissionValue(String permissionNode);

}
