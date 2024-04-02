package net.okocraft.okochat.api.channel.member;

import net.okocraft.okochat.api.channel.member.status.MemberStatus;
import net.okocraft.okochat.api.expirable.Expirable;
import net.okocraft.okochat.api.identity.Identity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import org.jetbrains.annotations.Unmodifiable;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

final class MemberHolderImpl implements MemberHolder {

    private final Map<Identity, MemberStatus> memberStatusMap = new HashMap<>();
    private final StampedLock lock = new StampedLock();

    @Override
    public @NotNull MemberStatus getStatus(@NotNull Identity identity) {
        MemberStatus status;

        {
            long stamp = this.lock.tryOptimisticRead();
            var tempStatus = this.memberStatusMap.get(identity);

            if (this.lock.validate(stamp)) {
                status = tempStatus;
            } else {
                stamp = this.lock.readLock();

                try {
                    status = this.memberStatusMap.get(identity);
                } finally {
                    this.lock.unlockRead(stamp);
                }
            }
        }

        if (status instanceof Expirable expirable && expirable.isExpired()) {
            if (status instanceof MemberStatus.MutedMember) {
                return MemberStatus.member();
            } else { // Banned or Muted
                return MemberStatus.NO_STATUS;
            }
        } else {
            return status;
        }
    }

    @Override
    public @NotNull MemberStatus getRawStatus(@NotNull Identity identity) {
        {
            long stamp = this.lock.tryOptimisticRead();
            var status = this.memberStatusMap.get(identity);

            if (this.lock.validate(stamp)) {
                return status;
            }
        }

        long stamp = this.lock.readLock();

        try {
            return this.memberStatusMap.get(identity);
        } finally {
            this.lock.unlockRead(stamp);
        }
    }

    @Override
    public boolean isMember(@NotNull Identity identity) {
        return this.getStatus(identity) instanceof MemberStatus.Member;
    }

    @Override
    public @NotNull MemberStatus join(@NotNull Identity identity) {
        long stamp = this.lock.writeLock();
        try {
            return this.memberStatusMap.merge(
                    identity,
                    MemberStatus.member(),
                    // @formatter:off
                    (oldValue, newValue) -> switch (oldValue) {
                        case MemberStatus.MutedMember mutedMember when mutedMember.isExpired() -> mutedMember.toNormalMember(); // already member, but mute status is expired
                        case MemberStatus.MutedMember ignored -> oldValue; // already (muted) member

                        case MemberStatus.Muted muted when muted.isExpired() -> newValue; // not member, and also mute status is expired
                        case MemberStatus.Muted muted -> muted.toMutedMember(); // update to muted member

                        case MemberStatus.Banned banned when banned.isExpired() -> newValue; // ban status is expired, so can be member
                        case MemberStatus.Banned ignored -> oldValue; // banned (cannot be member)

                        case MemberStatus.Member ignored -> oldValue; // already member

                        case MemberStatus.NoStatus ignored -> newValue; // become member
                    }
                    // @formatter:on
            );
        } finally {
            this.lock.unlockWrite(stamp);
        }
    }

    @Override
    public boolean quit(@NotNull Identity identity) {
        long stamp = this.lock.writeLock();
        try {
            var status = this.memberStatusMap.get(identity);

            // @formatter:off
            return switch (status) {
                case MemberStatus.MutedMember muted when muted.isExpired() -> {
                    this.memberStatusMap.remove(identity);
                    yield true;
                }
                case MemberStatus.MutedMember mutedMember -> {
                    this.memberStatusMap.put(identity, mutedMember.toNonMember()); // muted, update to not member
                    yield true;
                }
                case MemberStatus.Member ignored -> {
                    this.memberStatusMap.remove(identity);
                    yield true;
                }

                case MemberStatus.Banned ignored -> false; // already not member
                case MemberStatus.Muted ignored -> false; // already not member
                case MemberStatus.NoStatus ignored -> false; // already not member

                case null -> false; // no previous status exists
            };
            // @formatter:on
        } finally {
            this.lock.unlockWrite(stamp);
        }
    }

    @Override
    public boolean ban(@NotNull Identity identity, @NotNull Instant expiration) {
        checkExpiration(expiration);

        MemberStatus previousStatus;
        long stamp = this.lock.writeLock();

        try {
            previousStatus = this.memberStatusMap.put(identity, MemberStatus.banned(expiration));
        } finally {
            this.lock.unlockWrite(stamp);
        }

        return !(previousStatus instanceof MemberStatus.Banned banned) || banned.isExpired(); // previously, not banned or expired
    }

    @Override
    public boolean unban(@NotNull Identity identity) {
        long stamp = this.lock.writeLock();
        try {
            if (this.memberStatusMap.get(identity) instanceof MemberStatus.Banned banned) {
                this.memberStatusMap.remove(identity);
                return !banned.isExpired(); // if expired, it is already unbanned
            } else {
                return false;
            }
        } finally {
            this.lock.unlockWrite(stamp);
        }
    }

    @Override
    public @NotNull MemberStatus mute(@NotNull Identity identity, @NotNull Instant expiration) {
        checkExpiration(expiration);

        long stamp = this.lock.writeLock();
        try {
            return this.memberStatusMap.merge(
                    identity,
                    MemberStatus.muted(expiration),
                    (oldValue, newValue) -> switch (oldValue) {
                        // @formatter:off
                        case MemberStatus.Banned ignored -> oldValue; // already banned (cannot mute)
                        case MemberStatus.Muted muted -> muted.expiration(expiration); // update to new expiration
                        case MemberStatus.NormalMember member -> member.mute(expiration);
                        case MemberStatus.Moderator ignored -> oldValue; // cannot mute
                        case MemberStatus.Administrator ignored -> oldValue; // cannot mute
                        case MemberStatus.NoStatus ignored -> newValue; // mute
                        // @formatter:on
                    }
            );
        } finally {
            this.lock.unlockWrite(stamp);
        }
    }

    @Override
    public boolean unmute(@NotNull Identity identity) {
        long stamp = this.lock.writeLock();
        try {
            if (this.memberStatusMap.get(identity) instanceof MemberStatus.Muted muted) {
                if (muted instanceof MemberStatus.MutedMember mutedMember) {
                    this.memberStatusMap.put(identity, mutedMember.unmute());
                } else {
                    this.memberStatusMap.remove(identity);
                }
                return !muted.isExpired(); // if expired, it is already unmuted
            } else {
                return false;
            }
        } finally {
            this.lock.unlockWrite(stamp);
        }
    }

    @Override
    public boolean makeNormalMember(@NotNull Identity identity) {
        long stamp = this.lock.writeLock();
        try {
            if (this.memberStatusMap.get(identity) instanceof MemberStatus.Member member) {
                if (member instanceof MemberStatus.NormalMember) {
                    return false;
                } else {
                    this.memberStatusMap.put(identity, member.toNormalMember());
                    return true;
                }
            } else {
                return false;
            }
        } finally {
            this.lock.unlockWrite(stamp);
        }
    }

    @Override
    public boolean makeModerator(@NotNull Identity identity) {
        long stamp = this.lock.writeLock();
        try {
            if (this.memberStatusMap.get(identity) instanceof MemberStatus.Member member) {
                if (member instanceof MemberStatus.Moderator) {
                    return false;
                } else {
                    this.memberStatusMap.put(identity, member.toModerator());
                    return true;
                }
            } else {
                return false;
            }
        } finally {
            this.lock.unlockWrite(stamp);
        }
    }

    @Override
    public boolean makeAdministrator(@NotNull Identity identity) {
        long stamp = this.lock.writeLock();
        try {
            if (this.memberStatusMap.get(identity) instanceof MemberStatus.Member member) {
                if (member instanceof MemberStatus.Administrator) {
                    return false;
                } else {
                    this.memberStatusMap.put(identity, member.toAdministrator());
                    return true;
                }
            } else {
                return false;
            }
        } finally {
            this.lock.unlockWrite(stamp);
        }
    }

    @Override
    public @NotNull @Unmodifiable Map<Identity, MemberStatus> snapshot() {
        long stamp = this.lock.readLock();
        try {
            return Map.copyOf(this.memberStatusMap);
        } finally {
            this.lock.unlockRead(stamp);
        }
    }

    private static void checkExpiration(@NotNull Instant expiration) {
        if (expiration.isBefore(Instant.now())) {
            throw new IllegalArgumentException("The given expiration has already expired.");
        }
    }

    @TestOnly
    void putStatus(@NotNull Identity identity, @NotNull MemberStatus status) {
        long stamp = this.lock.writeLock();
        try {
            this.memberStatusMap.put(identity, status);
        } finally {
            this.lock.unlockWrite(stamp);
        }
    }
}
