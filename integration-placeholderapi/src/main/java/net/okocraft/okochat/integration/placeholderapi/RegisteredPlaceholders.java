package net.okocraft.okochat.integration.placeholderapi;

import com.google.errorprone.annotations.CanIgnoreReturnValue;

public interface RegisteredPlaceholders {

    @CanIgnoreReturnValue
    boolean unregister();

}
