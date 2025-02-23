package net.okocraft.okochat.bridge.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

public record SyncPlayerRequestData(UUID uuid) {

    public void write(DataOutput out) throws IOException {
        out.writeLong(this.uuid.getMostSignificantBits());
        out.writeLong(this.uuid.getLeastSignificantBits());
    }

    public static SyncPlayerRequestData read(DataInput in) throws IOException {
        return new SyncPlayerRequestData(new UUID(in.readLong(), in.readLong()));
    }
}
