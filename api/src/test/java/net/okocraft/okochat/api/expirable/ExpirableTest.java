package net.okocraft.okochat.api.expirable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.List;

class ExpirableTest {

    private static final Instant TIME = Instant.ofEpochSecond(0);

    @ParameterizedTest
    @MethodSource("testCases")
    void test(Expiration expiration) {
        Assertions.assertEquals(expiration.expectedIsExpired, expiration.isExpiredAt(TIME));
        Assertions.assertEquals(expiration.expectedIsPermanent, expiration.isPermanent());
    }

    private static List<Expiration> testCases() {
        return List.of(
                new Expiration(TIME.minusSeconds(1), true, false),
                new Expiration(TIME, false, false),
                new Expiration(TIME.plusSeconds(1), false, false),
                new Expiration(Expirable.PERMANENT, false, true)
        );
    }

    private record Expiration(Instant expiration, boolean expectedIsExpired,
                              boolean expectedIsPermanent) implements Expirable {
    }
}
