package net.okocraft.okochat.bridge.protocol;

import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class OkoChatProtocol {

    public static final byte VERSION = 1;
    public static final String CHANNEL = "okochat:messaging";
    private static final Map<Byte, MessageType<?>> BY_ID = new HashMap<>();

    public static final MessageType<ServerChatMessageData> CHAT = register(
            (byte) 1,
            ServerChatMessageData::write,
            ServerChatMessageData::read,
            OkoChatProtocol.Listener::onServerChatMessageData
    );

    public static final MessageType<SyncPlayerRequestData> REQUEST_PLAYER_DATA_SYNC = register(
            (byte) 2,
            SyncPlayerRequestData::write,
            SyncPlayerRequestData::read,
            OkoChatProtocol.Listener::onSyncPlayerRequestData
    );

    public static final MessageType<PlayerData> SYNC_PLAYER_DATA = register(
            (byte) 3,
            PlayerData::write,
            PlayerData::read,
            OkoChatProtocol.Listener::onPlayerData
    );

    private static <T> MessageType<T> register(byte identity, Writer<T> writer, Reader<T> reader, MessageConsumer<T> acceptor) {
        MessageType<T> type = new MessageType<>(identity, writer, reader, acceptor);
        BY_ID.put(identity, type);
        return type;
    }

    public record MessageType<T>(byte identity, Writer<T> writer, Reader<T> reader, MessageConsumer<T> acceptor) {
        void process(UUID receiver, DataInput in, Listener listener) throws Exception {
            T data = this.reader.read(in);
            this.acceptor.accept(listener, receiver, data);
        }
    }

    public interface Writer<T> {
        void write(T data, DataOutput out) throws Exception;
    }

    public interface Reader<T> {
        T read(DataInput in) throws Exception;
    }

    public interface MessageConsumer<T> {
        void accept(Listener listener, UUID receiver, T data);
    }

    public interface Listener {

        default void processPluginMessage(UUID receiver, byte[] rawData, Logger logger) {
            try (var in = new ByteArrayInputStream(rawData);
                 var dataIn = new DataInputStream(in)) {
                byte version = readVersion(dataIn);
                if (version != VERSION) {
                    logger.error("Unknown protocol version: {}", version);
                    return;
                }

                byte identity = readMessageType(dataIn);
                MessageType<?> type = BY_ID.get(identity);
                if (type == null) {
                    logger.error("Unknown message type: {}", identity);
                    return;
                }

                type.process(receiver, dataIn, this);
            } catch (Exception e) {
                logger.error("Failed to process a plugin message", e);
            }
        }

        default void onServerChatMessageData(UUID receiver, ServerChatMessageData data) {
        }

        default void onSyncPlayerRequestData(UUID receiver, SyncPlayerRequestData data) {
        }

        default void onPlayerData(UUID receiver, PlayerData data) {
        }
    }

    public static <T> byte[] encodeData(MessageType<T> type, T data) throws Exception {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             DataOutputStream dataOut = new DataOutputStream(out)) {
            dataOut.writeByte(VERSION);
            dataOut.writeByte(type.identity);
            type.writer.write(data, dataOut);
            return out.toByteArray();
        }
    }

    private static byte readVersion(DataInput in) throws Exception {
        return in.readByte();
    }

    private static byte readMessageType(DataInput in) throws Exception {
        return in.readByte();
    }
}
