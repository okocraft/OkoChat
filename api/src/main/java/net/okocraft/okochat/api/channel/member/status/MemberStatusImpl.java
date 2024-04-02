package net.okocraft.okochat.api.channel.member.status;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;

final class MemberStatusImpl {

    // TODO: additional status information

    record NoStatus() implements MemberStatus.NoStatus {
    }

    record NormalMember() implements MemberStatus.NormalMember {
    }

    record Moderator() implements MemberStatus.Moderator {
    }

    record Administrator() implements MemberStatus.Administrator {
    }

    record Banned(@NotNull Instant expiration) implements MemberStatus.Banned {
    }

    record Muted(@NotNull Instant expiration) implements MemberStatus.Muted {
    }

    record MutedMember(@NotNull Instant expiration) implements MemberStatus.MutedMember {
    }
}
