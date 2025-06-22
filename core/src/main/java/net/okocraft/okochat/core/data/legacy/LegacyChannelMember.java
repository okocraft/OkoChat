package net.okocraft.okochat.core.data.legacy;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.codec.UUIDCodec;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.jfun.result.Result;

import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Pattern;

public sealed interface LegacyChannelMember permits LegacyChannelMember.LegacyChannelMemberName, LegacyChannelMember.LegacyChannelMemberUUID {

    Codec<LegacyChannelMember> CODEC = Codec.STRING.flatXmap(
            identity -> Result.success(identity.asLegacySettingValue()),
            str -> {
                if (str.startsWith("$")) {
                    try {
                        return Result.success(new LegacyChannelMemberUUID(UUID.fromString(str.substring(1))));
                    } catch (IllegalArgumentException e) {
                        return new UUIDCodec.InvalidUUIDFormatError(str.substring(1)).asIgnorable().asFailure();
                    }
                } else {
                    return new LegacyChannelMemberName(str).validate().map(Function.identity());
                }
            }
    );

    String asLegacySettingValue();

    record LegacyChannelMemberName(String name) implements LegacyChannelMember {

        private static final int MAX_PLAYER_USERNAME_LENGTH = 16;
        private static final Pattern PLAYER_USERNAME_INVALID_CHAR_MATCHER = Pattern.compile("[^A-Za-z0-9_]");

        @Override
        public String asLegacySettingValue() {
            return this.name;
        }

        public Result<LegacyChannelMemberName, DecodeError> validate() {
            if (this.name.length() > MAX_PLAYER_USERNAME_LENGTH) {
                return DecodeError.failure("Player name is too long: " + this.name).asIgnorable().asFailure();
            } else if (!PLAYER_USERNAME_INVALID_CHAR_MATCHER.matcher(this.name).find()) {
                return DecodeError.failure("Player name contains invalid character: " + this.name).asIgnorable().asFailure();
            }
            return Result.success(this);
        }
    }

    record LegacyChannelMemberUUID(UUID uuid) implements LegacyChannelMember {
        @Override
        public String asLegacySettingValue() {
            return "$" + this.uuid;
        }
    }
}
