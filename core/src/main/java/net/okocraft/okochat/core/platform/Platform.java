package net.okocraft.okochat.core.platform;

import net.okocraft.okochat.api.chat.recipient.RecipientProvider;
import org.jetbrains.annotations.NotNullByDefault;
import org.slf4j.Logger;

import java.nio.file.Path;

@NotNullByDefault
public interface Platform {

    Logger logger();

    Path dataDirectory();

    RecipientProvider recipientProvider();

}
