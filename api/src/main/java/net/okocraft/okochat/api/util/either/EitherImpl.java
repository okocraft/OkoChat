package net.okocraft.okochat.api.util.either;

import org.jetbrains.annotations.Contract;

import java.util.NoSuchElementException;

final class EitherImpl {

    @Contract(value = "_ -> param1", pure = true)
    static <L, R> Either<L, R> castRight(Either<L, ?> either) {
        return cast(either);
    }

    @Contract(value = "_ -> param1", pure = true)
    static <L, R> Either<L, R> castLeft(Either<?, R> either) {
        return cast(either);
    }

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <R> R cast(Object obj) {
        return (R) obj;
    }

    record Left<L, R>(L left) implements Either<L, R> {
        @Override
        public R right() {
            throw new NoSuchElementException();
        }
    }

    record Right<L, R>(R right) implements Either<L, R> {
        @Override
        public L left() {
            throw new NoSuchElementException();
        }
    }
}
