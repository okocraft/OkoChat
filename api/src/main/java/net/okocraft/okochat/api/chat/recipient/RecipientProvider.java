package net.okocraft.okochat.api.chat.recipient;

import org.jetbrains.annotations.NotNullByDefault;

import java.util.UUID;

@NotNullByDefault
public interface RecipientProvider {

    Recipient getConsole();

    Recipient getByUUID(UUID uuid);

    Recipient getByName(String name);

}
