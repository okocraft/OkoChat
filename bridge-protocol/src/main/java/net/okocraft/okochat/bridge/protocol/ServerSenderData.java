package net.okocraft.okochat.bridge.protocol;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

@NotNullByDefault
public record ServerSenderData(
        UUID uuid,
        String name,
        Component displayName,
        String prefix,
        String suffix,
        String worldName,
        @Nullable BlockPosition position
) {

    public void write(DataOutput out) throws IOException {
        out.writeLong(this.uuid.getMostSignificantBits());
        out.writeLong(this.uuid.getLeastSignificantBits());
        out.writeUTF(this.name);
        out.writeUTF(GsonComponentSerializer.gson().serialize(this.displayName));
        out.writeUTF(this.prefix);
        out.writeUTF(this.suffix);
        out.writeUTF(this.worldName);
        out.writeBoolean(this.position != null);
        if (this.position != null) {
            this.position.write(out);
        }
    }

    public static ServerSenderData read(DataInput in) throws IOException {
        return new ServerSenderData(
                new UUID(in.readLong(), in.readLong()),
                in.readUTF(),
                GsonComponentSerializer.gson().deserialize(in.readUTF()),
                in.readUTF(),
                in.readUTF(),
                in.readUTF(),
                readPosition(in)
        );
    }

    private static @Nullable BlockPosition readPosition(DataInput in) throws IOException {
        if (in.readBoolean()) {
            return BlockPosition.read(in);
        }
        return null;
    }
}
