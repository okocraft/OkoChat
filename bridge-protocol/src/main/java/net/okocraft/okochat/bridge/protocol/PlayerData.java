package net.okocraft.okochat.bridge.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

public record PlayerData(UUID uuid, String defaultChannelName) {

    public void write(DataOutput out) throws IOException {
        out.writeLong(this.uuid.getMostSignificantBits());
        out.writeLong(this.uuid.getLeastSignificantBits());
        out.writeUTF(this.defaultChannelName);
    }

    public static PlayerData read(DataInput in) throws IOException {
        return new PlayerData(
                new UUID(in.readLong(), in.readLong()),
                in.readUTF()
        );
    }
}
