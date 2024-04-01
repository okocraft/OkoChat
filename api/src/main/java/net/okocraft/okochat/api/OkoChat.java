package net.okocraft.okochat.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface OkoChat {

    @ApiStatus.Experimental
    static @NotNull OkoChat api() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
