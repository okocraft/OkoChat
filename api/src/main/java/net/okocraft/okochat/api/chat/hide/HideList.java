package net.okocraft.okochat.api.chat.hide;

import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.stream.Stream;

@NotNullByDefault
public interface HideList {

    boolean isHidden(Identified target);

    void hide(Identified target);

    void unhide(Identified target);

    Stream<Identity> stream();

}
