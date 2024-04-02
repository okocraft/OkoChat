package net.okocraft.okochat.api.channel.member.status;

import net.okocraft.okochat.api.expirable.Expirable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Objects;

@ApiStatus.Experimental
public sealed interface MemberStatus permits MemberStatus.Banned, MemberStatus.Member, MemberStatus.Muted, MemberStatus.NoStatus {

    MemberStatus NO_STATUS = new MemberStatusImpl.NoStatus();

    static @NotNull NormalMember member() {
        return new MemberStatusImpl.NormalMember();
    }

    static @NotNull Banned banned(@NotNull Instant expires) {
        return new MemberStatusImpl.Banned(Objects.requireNonNull(expires));
    }

    static @NotNull Muted muted(@NotNull Instant expires) {
        return new MemberStatusImpl.Muted(Objects.requireNonNull(expires));
    }

    sealed interface NoStatus extends MemberStatus permits MemberStatusImpl.NoStatus {
        String TYPE = "no_status";
    }

    sealed interface Member extends MemberStatus permits Administrator, Moderator, MutedMember, NormalMember {

        default @NotNull NormalMember toNormalMember() {
            return new MemberStatusImpl.NormalMember();
        }

        default @NotNull Moderator toModerator() {
            return new MemberStatusImpl.Moderator();
        }

        default @NotNull Administrator toAdministrator() {
            return new MemberStatusImpl.Administrator();
        }

    }

    sealed interface NormalMember extends Member permits MemberStatusImpl.NormalMember {

        String TYPE = "member";

        default @NotNull MutedMember mute(@NotNull Instant expiration) {
            return new MemberStatusImpl.MutedMember(expiration);
        }

        @Override
        default @NotNull NormalMember toNormalMember() {
            return this;
        }

    }

    sealed interface Moderator extends Member permits MemberStatusImpl.Moderator {

        String TYPE = "moderator";

        @Override
        default @NotNull Moderator toModerator() {
            return this;
        }

    }

    sealed interface Administrator extends Member permits MemberStatusImpl.Administrator {
        String TYPE = "administrator";

        @Override
        default @NotNull Administrator toAdministrator() {
            return this;
        }
    }

    sealed interface Banned extends MemberStatus, Expirable permits MemberStatusImpl.Banned {
        String TYPE = "banned";

        default @NotNull Banned expiration(@NotNull Instant expiration) {
            return new MemberStatusImpl.Banned(expiration);
        }
    }

    sealed interface Muted extends MemberStatus, Expirable permits MutedMember, MemberStatusImpl.Muted {

        String TYPE = "muted";

        default @NotNull MutedMember toMutedMember() {
            return new MemberStatusImpl.MutedMember(this.expiration());
        }

        default @NotNull Muted expiration(@NotNull Instant expiration) {
            return new MemberStatusImpl.Muted(expiration);
        }

    }

    sealed interface MutedMember extends Member, Muted permits MemberStatusImpl.MutedMember {

        String TYPE = "muted_member";

        @Override
        default @NotNull NormalMember toNormalMember() {
            return this.unmute();
        }

        @Override
        default @NotNull MutedMember toMutedMember() {
            return this;
        }

        default @NotNull Muted toNonMember() {
            return new MemberStatusImpl.Muted(this.expiration());
        }

        default @NotNull NormalMember unmute() {
            return new MemberStatusImpl.NormalMember();
        }

        @Override
        default @NotNull MutedMember expiration(@NotNull Instant expiration) {
            return new MemberStatusImpl.MutedMember(expiration);
        }

    }
}
