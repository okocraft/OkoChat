package net.okocraft.okochat.bridge.protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;

public final class OkoChatProtocol {

    public static final String CHANNEL = "okochat:messaging";

    public static final MessageType<ServerChatMessageData> CHAT = new MessageType<>(
            (byte) 1,
            ServerChatMessageData::write,
            ServerChatMessageData::read
    );

    public record MessageType<T>(byte identity, Writer<T> writer, Reader<T> reader) {
    }

    public interface Writer<T> {
        void write(T data, DataOutput out) throws Exception;
    }

    public interface Reader<T> {
        T read(DataInput in) throws Exception;
    }

    public static <T> byte[] encodeData(MessageType<T> type, T data) throws Exception {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             DataOutputStream dataOut = new DataOutputStream(out)) {
            dataOut.writeByte(type.identity);
            type.writer.write(data, dataOut);
            return out.toByteArray();
        }
    }
}
