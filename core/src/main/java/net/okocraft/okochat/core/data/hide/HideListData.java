package net.okocraft.okochat.core.data.hide;

import dev.siroshun.codec4j.api.codec.Codec;
import dev.siroshun.codec4j.api.codec.UUIDCodec;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record HideListData(Map<UUID, List<UUID>> hideListMap) {

    public static final Codec<HideListData> CODEC =
            UUIDCodec.UUID_AS_STRING.toMapCodecAsKey(UUIDCodec.UUID_AS_STRING.toListCodec())
                    .xmap(HideListData::hideListMap, HideListData::new);

}
