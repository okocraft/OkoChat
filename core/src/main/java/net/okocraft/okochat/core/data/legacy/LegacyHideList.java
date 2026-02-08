package net.okocraft.okochat.core.data.legacy;

import dev.siroshun.codec4j.api.decoder.Decoder;
import dev.siroshun.codec4j.api.decoder.collection.ListDecoder;
import dev.siroshun.codec4j.api.decoder.collection.MapDecoder;
import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.io.yaml.YamlIO;
import dev.siroshun.jfun.result.Result;
import net.okocraft.okochat.core.data.hide.HideListData;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class LegacyHideList {

    public static final Decoder<LegacyHideList> DECODER =
            MapDecoder.create(LegacyChannelMember.CODEC, ListDecoder.create(LegacyChannelMember.CODEC))
                    .map(LegacyHideList::new);

    public static Path getFilepath(Path dataDirectory) {
        return dataDirectory.resolve("hidelist.yml");
    }

    public static @NotNull Result<LegacyHideList, DecodeError> loadFromYaml(@NotNull Path filepath) {
        return YamlIO.DEFAULT.decodeFrom(filepath, DECODER);
    }

    // key: the hidden player
    // value: the list of players who hides the key
    private final Map<LegacyChannelMember, List<LegacyChannelMember>> hideListMap;

    private LegacyHideList(Map<LegacyChannelMember, List<LegacyChannelMember>> hideListMap) {
        this.hideListMap = hideListMap;
    }

    public HideListData toHideListData(LegacyChannelMemberResolver resolver, Consumer<LegacyChannelMember> unresolvedMemberConsumer) {
        Map<UUID, List<UUID>> hideListMap = new HashMap<>();

        for (Map.Entry<LegacyChannelMember, List<LegacyChannelMember>> entry : this.hideListMap.entrySet()) {
            UUID hidden = resolver.resolveMember(entry.getKey());
            if (hidden == null) {
                unresolvedMemberConsumer.accept(entry.getKey());
            }

            for (LegacyChannelMember player : entry.getValue()) {
                UUID uuid = resolver.resolveMember(player);
                if (uuid == null) {
                    unresolvedMemberConsumer.accept(player);
                }

                hideListMap.computeIfAbsent(uuid, ignored -> new ArrayList<>()).add(hidden);
            }
        }

        return new HideListData(Collections.unmodifiableMap(hideListMap));
    }
}
