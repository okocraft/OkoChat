package net.okocraft.okochat.core.platform;

import net.kyori.adventure.identity.Identified;
import net.okocraft.okochat.bridge.protocol.OkoChatProtocol;
import org.jetbrains.annotations.NotNullByDefault;

@NotNullByDefault
public interface PluginMessageService {

    void registerListener(OkoChatProtocol.Listener listener);

    <T> void send(Identified sender, OkoChatProtocol.MessageType<T> type, T data);

}
