package net.okocraft.okochat.integration.luckperms;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.okocraft.okochat.integration.AffixProvider;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@NotNullByDefault
class LuckPermsAffixProvider<P> implements AffixProvider<P> {

    private final Function<P, UUID> uuidFunction;

    LuckPermsAffixProvider(Function<P, UUID> uuidFunction) {
        this.uuidFunction = uuidFunction;
    }

    @Override
    public String getPrefix(P player) {
        return this.getUserMetaData(player).map(CachedMetaData::getPrefix).orElse("");
    }

    @Override
    public String getSuffix(P player) {
        return this.getUserMetaData(player).map(CachedMetaData::getSuffix).orElse("");
    }

    private Optional<CachedMetaData> getUserMetaData(P player) {
        return Optional.of(this.uuidFunction.apply(player))
                .map(LuckPermsProvider.get().getUserManager()::getUser)
                .map(User::getCachedData)
                .map(CachedDataManager::getMetaData);
    }
}
