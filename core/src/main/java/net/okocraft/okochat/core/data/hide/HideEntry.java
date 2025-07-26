package net.okocraft.okochat.core.data.hide;

import org.jetbrains.annotations.NotNullByDefault;

import java.util.UUID;

@NotNullByDefault
public record HideEntry(UUID uuid, UUID hidden) {
}
