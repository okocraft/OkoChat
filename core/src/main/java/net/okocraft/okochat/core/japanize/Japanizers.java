package net.okocraft.okochat.core.japanize;

import net.okocraft.okochat.api.chat.converter.ChatConverter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static net.okocraft.okochat.core.util.OkoChatLogger.logger;

public final class Japanizers {

    private static final Pattern HALF_WIDTH_KANA_PATTERN = Pattern.compile("[\\uFF61-\\uFF9F]+");
    private static final Pattern URL_PATTERN = Pattern.compile("(?:https?://)?(?:[\\w-]+\\.)+[?:\\w-]+(?:/[\\w- ./?%&=~]*)?");

    public static @NotNull ChatConverter toKana(@NotNull Supplier<Map<String, String>> dictionarySupplier) {
        return message -> {
            if (shouldIgnore(message)) {
                return message;
            }

            var modifiedMessage = message;
            var lockMap = new HashMap<String, String>();

            modifiedMessage = lock(dictionarySupplier.get(), modifiedMessage, lockMap);
            modifiedMessage = YukiKanaConverter.convert(modifiedMessage);
            modifiedMessage = unlock(modifiedMessage, lockMap);

            return modifiedMessage.trim();
        };
    }

    public static @NotNull ChatConverter useGoogleIME(@NotNull Supplier<Map<String, String>> dictionarySupplier) {
        return message -> {
            if (shouldIgnore(message)) {
                return message;
            }

            var modifiedMessage = message;
            var lockMap = new HashMap<String, String>();

            modifiedMessage = lock(dictionarySupplier.get(), modifiedMessage, lockMap);
            modifiedMessage = YukiKanaConverter.convert(modifiedMessage);

            try {
                modifiedMessage = GoogleIME.convert(modifiedMessage);
            } catch (Exception e) {
                logger().error("Could not convert a message using GoogleIME.", e);
            }

            modifiedMessage = unlock(modifiedMessage, lockMap);

            return modifiedMessage.trim();
        };
    }

    private static boolean shouldIgnore(@NotNull String original) {
        // use && instead of || to avoid creating unneeded matcher
        return !(original.getBytes().length == original.length() && !HALF_WIDTH_KANA_PATTERN.matcher(original).matches());
    }

    private static String lock(@NotNull Map<String, String> dictionary, String message, HashMap<String, String> lockMap) {
        var modifiedMessage = message;
        var counter = new LockCounter();

        modifiedMessage = lockURL(modifiedMessage, lockMap, counter);
        modifiedMessage = lockKeywords(modifiedMessage, dictionary, lockMap, counter);

        return modifiedMessage;
    }

    private static String lockURL(String message, Map<String, String> lockMap, LockCounter counter) {
        return URL_PATTERN.matcher(message).replaceAll(result -> {
            var lockKey = counter.nextURLLock();
            lockMap.put(lockKey, result.group());
            return lockKey;
        });
    }

    private static String lockKeywords(String message, Map<String, String> dictionary, Map<String, String> lockMap, LockCounter counter) {
        var result = message;
        for (var entry : dictionary.entrySet()) {
            if (entry.getKey().isEmpty() || !result.contains(message)) {
                continue;
            }

            var lockKey = counter.nextKeywordLock();
            lockMap.put(lockKey, entry.getValue());
            result = result.replace(entry.getKey(), lockKey);
        }
        return result;
    }

    private static String unlock(String message, Map<String, String> lockMap) {
        var result = message;
        for (var entry : lockMap.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        return result;
    }

    private Japanizers() {
        throw new UnsupportedOperationException();
    }

    private static class LockCounter {

        private static final Map<Integer, String> LOCK_KEY_CACHE = new ConcurrentHashMap<>();

        private int urlCount;
        private int keywordCount;

        public String nextURLLock() {
            return getLockKey(-(this.urlCount++));
        }

        public String nextKeywordLock() {
            return getLockKey(this.keywordCount++);
        }

        private static @NotNull String getLockKey(int digit) {
            return LOCK_KEY_CACHE.computeIfAbsent(digit, LockCounter::createLockKey);
        }

        private static @NotNull String createLockKey(int digit) {
            String half = Integer.toString(digit);
            StringBuilder result = new StringBuilder("＜");

            for (int index = 0; index < half.length(); index++) {
                result.append(switch (half.charAt(index)) {
                    case '-' -> "‐";
                    case '0' -> "０";
                    case '1' -> "１";
                    case '2' -> "２";
                    case '3' -> "３";
                    case '4' -> "４";
                    case '5' -> "５";
                    case '6' -> "６";
                    case '7' -> "７";
                    case '8' -> "８";
                    case '9' -> "９";
                    default -> String.valueOf(half.charAt(index));
                });
            }
            return result.append("＞").toString();
        }
    }
}
