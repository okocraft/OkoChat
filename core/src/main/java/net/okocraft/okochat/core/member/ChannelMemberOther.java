package net.okocraft.okochat.core.member;

import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.okocraft.okochat.core.util.BlockLocation;

/**
 * 任意の内容を設定できるChannelMember
 * @author ucchy
 */
public class ChannelMemberOther implements ChannelMember {

    private String id;
    private String name;
    private BlockLocation location;

    public ChannelMemberOther(@NotNull String name) {
        this(name, null);
    }

    public ChannelMemberOther(@NotNull String name,
            @Nullable BlockLocation location) {
        this(name, location, null);
    }

    public ChannelMemberOther(@NotNull String name,
            @Nullable BlockLocation location, @Nullable String id) {
        this.name = name;
        this.location = location;
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public void sendMessage(String message) {
        // do nothing.
    }

    @Override
    public void sendMessage(Component message) {
        // do nothing.
    }

    public @Nullable BlockLocation getLocation() {
        return location;
    }

    @Override
    public boolean hasPermission(String node) {
        return true;
    }

    @Override
    public TriState checkPermission(String node) {
        return TriState.TRUE;
    }

    @Override
    public String toString() {
        if ( id != null ) return "$" + id;
        return name;
    }

    /**
     * @return id
     */
    public String getId() {
        return id;
    }
}
