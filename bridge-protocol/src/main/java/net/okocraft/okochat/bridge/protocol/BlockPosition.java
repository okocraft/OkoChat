package net.okocraft.okochat.bridge.protocol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public record BlockPosition(int x, int y, int z) {

    public void write(DataOutput out) throws IOException {
        out.writeInt(this.x);
        out.writeInt(this.y);
        out.writeInt(this.z);
    }

    public static BlockPosition read(DataInput in) throws IOException {
        return new BlockPosition(in.readInt(), in.readInt(), in.readInt());
    }

}
