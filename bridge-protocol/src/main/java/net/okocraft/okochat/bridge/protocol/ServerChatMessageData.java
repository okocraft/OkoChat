package net.okocraft.okochat.bridge.protocol;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jetbrains.annotations.NotNullByDefault;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@NotNullByDefault
public record ServerChatMessageData(
        ServerSenderData senderData,
        Component message
) {

    public void write(DataOutput out) throws IOException {
        this.senderData.write(out);
        out.writeUTF(GsonComponentSerializer.gson().serialize(this.message));
    }
    
    public static ServerChatMessageData read(DataInput in) throws IOException {
        return new ServerChatMessageData(
                ServerSenderData.read(in),
                GsonComponentSerializer.gson().deserialize(in.readUTF())
        );
    }
}
