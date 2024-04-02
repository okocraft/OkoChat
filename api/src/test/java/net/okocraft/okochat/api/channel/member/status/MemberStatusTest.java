package net.okocraft.okochat.api.channel.member.status;

import net.okocraft.okochat.api.expirable.Expirable;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class MemberStatusTest {

    private static final Instant EXPIRATION = Instant.now().plusSeconds(TimeUnit.DAYS.toSeconds(1));
    private static final Instant NEW_EXPIRATION = EXPIRATION.plusSeconds(TimeUnit.DAYS.toSeconds(1));

    @ParameterizedTest
    @MethodSource("testCases")
    void test(TestCase<?, ?> testCase) {
        testCase.runTest();
    }

    private static Stream<TestCase<?, ?>> testCases() {
        return Stream.of(
                // Tests of interconversion between NormalMember, Moderator, and Administrator
                testCase(MemberStatus.member(), MemberStatus.Member::toNormalMember, Assertions::assertSame),
                testCase(MemberStatus.member(), MemberStatus.Member::toModerator, doNothing()),
                testCase(MemberStatus.member(), MemberStatus.Member::toAdministrator, doNothing()),
                testCase(MemberStatus.member().toModerator(), MemberStatus.Member::toNormalMember, doNothing()),
                testCase(MemberStatus.member().toModerator(), MemberStatus.Member::toModerator, Assertions::assertSame),
                testCase(MemberStatus.member().toModerator(), MemberStatus.Member::toAdministrator, doNothing()),
                testCase(MemberStatus.member().toAdministrator(), MemberStatus.Member::toNormalMember, doNothing()),
                testCase(MemberStatus.member().toAdministrator(), MemberStatus.Member::toModerator, doNothing()),
                testCase(MemberStatus.member().toAdministrator(), MemberStatus.Member::toAdministrator, Assertions::assertSame),

                // Tests of interconversion between NormalMember and MutedMember
                testCase(MemberStatus.member(), member -> member.mute(EXPIRATION), doNothing()),
                testCase(MemberStatus.member().mute(EXPIRATION), MemberStatus.MutedMember::unmute, doNothing()),

                // Checks if the specified expiration is set for Banned and Muted
                testCase(MemberStatus.banned(EXPIRATION), Function.identity(), (initial, modified) -> assertEquals(EXPIRATION, modified.expiration())),
                testCase(MemberStatus.muted(EXPIRATION), Function.identity(), (initial, modified) -> assertEquals(EXPIRATION, modified.expiration())),
                testCase(MemberStatus.muted(EXPIRATION), MemberStatus.Muted::toMutedMember, (initial, modified) -> assertEquals(EXPIRATION, modified.expiration())),
                testCase(MemberStatus.muted(EXPIRATION).toMutedMember(), MemberStatus.MutedMember::toMutedMember, Assertions::assertSame),
                testCase(MemberStatus.muted(EXPIRATION).toMutedMember(), MemberStatus.MutedMember::toNonMember, (initial, modified) -> assertEquals(EXPIRATION, modified.expiration())),
                testCase(MemberStatus.member(), member -> member.mute(EXPIRATION), (initial, modified) -> assertEquals(EXPIRATION, modified.expiration())),

                // Tests for changing expiration
                testCase(MemberStatus.banned(EXPIRATION), status -> status.expiration(NEW_EXPIRATION), MemberStatusTest::checkExpiration),
                testCase(MemberStatus.muted(EXPIRATION), status -> status.expiration(NEW_EXPIRATION), MemberStatusTest::checkExpiration),
                testCase(MemberStatus.member().mute(EXPIRATION), member -> member.expiration(NEW_EXPIRATION), MemberStatusTest::checkExpiration)
        );
    }

    private static <A, B> BiConsumer<A, B> doNothing() { // In the future, add more tests when additional fields are added to MemberStatus
        return (a, b) -> {
        };
    }

    private static <M1 extends Expirable, M2 extends Expirable> void checkExpiration(M1 initial, M2 modified) {
        assertNotSame(initial, modified); // new instance created
        assertEquals(EXPIRATION, initial.expiration()); // original expiration is not changed
        assertEquals(NEW_EXPIRATION, modified.expiration());
    }

    private static <M1 extends MemberStatus, M2 extends MemberStatus> TestCase<M1, M2> testCase(@NotNull M1 initial,
                                                                                                @NotNull Function<M1, M2> modifier,
                                                                                                @NotNull BiConsumer<M1, M2> checker) {
        return new TestCase<>(initial, modifier, checker);
    }

    private record TestCase<M1 extends MemberStatus, M2 extends MemberStatus>(@NotNull M1 initial,
                                                                              @NotNull Function<M1, M2> modifier,
                                                                              @NotNull BiConsumer<M1, M2> checker) {
        public void runTest() {
            this.checker.accept(this.initial, this.modifier.apply(this.initial));
        }
    }
}
