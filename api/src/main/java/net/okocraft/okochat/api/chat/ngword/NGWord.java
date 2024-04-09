package net.okocraft.okochat.api.chat.ngword;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public sealed interface NGWord permits NGWord.Literal, NGWord.Regex {

    @NotNull Optional<DetectedNGWord> detect(@NotNull String message);

    @NotNull NGWordOperationType operationType();

    record Regex(@NotNull Pattern pattern, @NotNull NGWordOperationType operationType,
                 @NotNull String mask) implements NGWord {
        @Override
        public @NotNull Optional<DetectedNGWord> detect(@NotNull String message) {
            var matcher = this.pattern.matcher(message);
            return matcher.find() ?
                    Optional.of(new DetectedNGWord(message, this, matcher.group(), this.maskIfNeeded(matcher))) :
                    Optional.empty();
        }

        private @NotNull Optional<String> maskIfNeeded(@NotNull Matcher matcher) {
            return this.operationType == NGWordOperationType.MASK ?
                    Optional.of(matcher.replaceAll(this.mask)) :
                    Optional.empty();
        }
    }

    record Literal(@NotNull String literal, @NotNull NGWordOperationType operationType,
                   @NotNull String mask) implements NGWord {
        @Override
        public @NotNull Optional<DetectedNGWord> detect(@NotNull String message) {
            return message.contains(this.literal) ?
                    Optional.of(new DetectedNGWord(message, this, this.literal, this.maskIfNeeded(message))) :
                    Optional.empty();
        }

        private @NotNull Optional<String> maskIfNeeded(@NotNull String message) {
            return this.operationType == NGWordOperationType.MASK ?
                    Optional.of(message.replace(this.literal, this.mask)) :
                    Optional.empty();
        }
    }
}
