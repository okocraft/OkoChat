package net.okocraft.okochat.core.data.hide;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.codec.UUIDCodec;
import dev.siroshun.codec4j.api.codec.collection.ListCodec;
import dev.siroshun.codec4j.api.codec.collection.MapCodec;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record HideListData(Map<UUID, List<UUID>> hideListMap) {

    public static final Codec<HideListData> CODEC =
            MapCodec.create(UUIDCodec.UUID_AS_STRING, ListCodec.create(UUIDCodec.UUID_AS_STRING))
                    .xmap(HideListData::hideListMap, HideListData::new);

}
