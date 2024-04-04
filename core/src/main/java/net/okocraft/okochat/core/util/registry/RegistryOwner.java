package net.okocraft.okochat.core.util.registry;

import java.util.Objects;

record RegistryOwner(Object object) {

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof RegistryOwner other && this.object == other.object);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.object);
    }

}
