package net.okocraft.okochat.api.util.either;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public sealed interface Either<L, R> permits EitherImpl.Left, EitherImpl.Right {

    @Contract("_ -> new")
    static <L, R> @NotNull Either<L, R> left(L left) {
        return new EitherImpl.Left<>(left);
    }

    @Contract("_ -> new")
    static <L, R> @NotNull Either<L, R> right(R right) {
        return new EitherImpl.Right<>(right);
    }

    default boolean isLeft() {
        return getClass() == EitherImpl.Left.class;
    }

    default boolean isRight() {
        return getClass() == EitherImpl.Right.class;
    }

    L left();

    R right();

    default <T> T fold(@NotNull Function<? super L, ? extends T> ifLeft, @NotNull Function<? super R, ? extends T> ifRight) {
        return isLeft() ? ifLeft.apply(left()) : ifRight.apply(right());
    }

    default <R2> @NotNull Either<L, R2> flatMap(@NotNull Function<? super R, @NotNull Either<L, R2>> mapper) {
        return isLeft() ? EitherImpl.castRight(this) : mapper.apply(right());
    }

    default <L2> @NotNull Either<L2, R> flatMapLeft(@NotNull Function<? super L, @NotNull Either<L2, R>> mapper) {
        return isLeft() ? mapper.apply(left()) : EitherImpl.castLeft(this);
    }

    default @NotNull Either<L, R> onLeft(@NotNull Consumer<? super L> leftConsumer) {
        if (isLeft()) {
            leftConsumer.accept(left());
        }
        return this;
    }

    default @NotNull Either<L, R> onRight(@NotNull Consumer<? super R> rightConsumer) {
        if (isRight()) {
            rightConsumer.accept(right());
        }
        return this;
    }

    default R getOrElse(R other) {
        return isRight() ? right() : other;
    }

    default L getOrElseLeft(L other) {
        return isLeft() ? left() : other;
    }

    default @NotNull Either<L, R> orElse(@NotNull Either<? extends L, ? extends R> other) {
        return isLeft() ? EitherImpl.cast(other) : this;
    }

    default @NotNull Either<L, R> orElseGet(@NotNull Supplier<Either<? extends L, ? extends R>> other) {
        return isLeft() ? EitherImpl.cast(other.get()) : this;
    }

    default @NotNull Optional<Either<L, R>> filter(@NotNull Predicate<? super R> filter) {
        return isLeft() || filter.test(right()) ? Optional.empty() : Optional.of(this);
    }

    default @NotNull Either<L, R> filterOrElse(Predicate<? super R> filter, L zero) {
        return isLeft() || filter.test(right()) ? this : left(zero);
    }

    default @NotNull Either<L, R> filterOrElseGet(Predicate<? super R> filter, Function<? super R, ? extends L> zero) {
        return isLeft() || filter.test(right()) ? this : left(zero.apply(right()));
    }

    default <R2> @NotNull Either<L, R2> map(@NotNull Function<? super R, ? extends R2> rightMapper) {
        return isRight() ? right(rightMapper.apply(right())) : EitherImpl.castRight(this);
    }

    default <L2> @NotNull Either<L2, R> mapLeft(@NotNull Function<? super L, ? extends L2> leftMapper) {
        return isLeft() ? left(leftMapper.apply(left())) : EitherImpl.castLeft(this);
    }

    default @NotNull Either<R, L> swap() {
        return isLeft() ? right(left()) : left(right());
    }

    default @NotNull Optional<R> toOptional() {
        return isLeft() ? Optional.empty() : Optional.ofNullable(right());
    }

    default @NotNull Optional<L> toOptionalLeft() {
        return isLeft() ? Optional.ofNullable(left()) : Optional.empty();
    }

    default @NotNull Stream<R> stream() {
        return isLeft() ? Stream.empty() : Stream.of(right());
    }

    default @NotNull Stream<L> streamLeft() {
        return isLeft() ? Stream.of(left()) : Stream.empty();
    }
}
