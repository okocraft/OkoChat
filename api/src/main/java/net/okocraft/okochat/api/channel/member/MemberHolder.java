package net.okocraft.okochat.api.channel.member;

import net.okocraft.okochat.api.channel.member.status.MemberStatus;
import net.okocraft.okochat.api.identity.Identity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.time.Instant;
import java.util.Map;

public sealed interface MemberHolder permits MemberHolderImpl {

    @Contract(" -> new")
    static @NotNull MemberHolder create() {
        return new MemberHolderImpl();
    }

    @NotNull MemberStatus getStatus(@NotNull Identity identity);

    @NotNull MemberStatus getRawStatus(@NotNull Identity identity);

    boolean isMember(@NotNull Identity identity);

    @NotNull MemberStatus join(@NotNull Identity identity);

    boolean quit(@NotNull Identity identity);

    boolean ban(@NotNull Identity identity, @NotNull Instant expires);

    boolean unban(@NotNull Identity identity);

    @NotNull MemberStatus mute(@NotNull Identity identity, @NotNull Instant expires);

    boolean unmute(@NotNull Identity identity);

    boolean makeNormalMember(@NotNull Identity identity);

    boolean makeModerator(@NotNull Identity identity);

    boolean makeAdministrator(@NotNull Identity identity);

    @NotNull @Unmodifiable Map<Identity, MemberStatus> snapshot();

}
