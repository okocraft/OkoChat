package net.okocraft.okochat.legacy;

import com.github.ucchyocean.lc3.member.ChannelMember;
import com.github.ucchyocean.lc3.util.YamlConfig;
import net.okocraft.okochat.core.chat.hide.HideListHolder;
import net.okocraft.okochat.core.data.FileList;
import net.okocraft.okochat.core.data.legacy.LegacyHideList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.okocraft.okochat.api.OkoChat.logger;

class HideListMigrationTest {

    @Test
    void test(@TempDir Path dir) throws Exception {
        Path legacyFilepath = LegacyHideList.getFilepath(dir);
        Files.copy(Path.of("src/test/resources/legacy_hide_list.yml"), legacyFilepath);

        Map<String, List<ChannelMember>> legacyHideList = loadLegacyHideList(legacyFilepath);

        FileList fileList = new FileList(dir);

        HideListHolder hideListHolder = fileList.initializeHideListHolder();
        fileList.migrateLegacyHideListIfExists(hideListHolder, name -> Assertions.fail("non-uuid ChannelMember found: " + name));

        for (Map.Entry<String, List<ChannelMember>> entry : legacyHideList.entrySet()) {
            String hidden = entry.getKey();
            for (ChannelMember hidedBy : entry.getValue()) {
                hideListHolder.getByIdentified(hidedBy).isHidden(ChannelMember.getChannelMember(hidden));
                logger().info("(HideList) {} is hidden by {}", hidden, hidedBy);
            }
        }


        for (Map.Entry<UUID, List<UUID>> entry : hideListHolder.exportToData().hideListMap().entrySet()) {
            UUID hidedBy = entry.getKey();
            for (UUID hidden : entry.getValue()) {
                Assertions.assertTrue(legacyHideList.getOrDefault("$" + hidden.toString(), List.of()).contains(ChannelMember.getChannelMember("$" + hidedBy.toString())));
                logger().info("(LegacyHideList) {} is hidden by {}", hidden, hidedBy);
            }
        }
    }

    private static Map<String, List<ChannelMember>> loadLegacyHideList(Path legacyFilepath) {
        YamlConfig configHidelist = YamlConfig.load(legacyFilepath.toFile());
        Map<String, List<ChannelMember>> hidelist = new HashMap<>();
        for (String key : configHidelist.getKeys(false)) {
            hidelist.put(key, new ArrayList<>());
            for (String id : configHidelist.getStringList(key, new ArrayList<>())) {
                hidelist.get(key).add(ChannelMember.getChannelMember(id));
            }
        }
        return hidelist;
    }
}
