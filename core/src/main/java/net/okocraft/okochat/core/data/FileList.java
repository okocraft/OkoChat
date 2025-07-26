package net.okocraft.okochat.core.data;

import dev.siroshun.codec4j.api.error.DecodeError;
import dev.siroshun.codec4j.io.yaml.YamlIO;
import dev.siroshun.jfun.result.Result;
import net.okocraft.okochat.core.chat.hide.HideListHolder;
import net.okocraft.okochat.core.data.hide.HideListData;
import net.okocraft.okochat.core.data.legacy.LegacyChannelMemberResolver;
import net.okocraft.okochat.core.data.legacy.LegacyHideList;
import net.okocraft.okochat.core.platform.Scheduler;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static net.okocraft.okochat.api.OkoChat.logger;

@NotNullByDefault
public class FileList {

    private static final String HIDE_LIST_DATA_FILENAME = "hide_list.yml";

    private final Path rootDataDirectory;
    private final Path dataDirectory;
    private final List<Runnable> saveTasks;

    private @Nullable Scheduler.CancellableTask scheduledAutoSaveTask;

    public FileList(Path dataDirectory) {
        this.rootDataDirectory = dataDirectory;
        this.dataDirectory = dataDirectory.resolve("data");
        this.saveTasks = new ArrayList<>();
    }

    public void scheduleAutoSave(Scheduler scheduler) {
        this.scheduledAutoSaveTask = scheduler.schedule(
                () -> this.saveTasks.forEach(Runnable::run),
                Duration.ofSeconds(30),
                Duration.ofSeconds(30)
        );
    }

    public void shutdownAutoSaveTask() {
        if (this.scheduledAutoSaveTask != null) {
            this.scheduledAutoSaveTask.cancel();
            this.scheduledAutoSaveTask = null;
        }

        this.saveTasks.forEach(Runnable::run);
    }

    public HideListHolder initializeHideListHolder() {
        Path filepath = this.dataDirectory.resolve(HIDE_LIST_DATA_FILENAME);
        HideListHolder holder = new HideListHolder();

        if (Files.isRegularFile(filepath)) {
            YamlIO.DEFAULT.decodeFrom(filepath, HideListData.CODEC)
                    .inspect(holder::importFromData)
                    .inspectError(error -> logger().error("Failed to load hide list: {}", error));
        }

        this.saveTasks.add(() -> {
            if (holder.checkDirty()) {
                this.saveHideListData(holder.exportToData());
            }
        });

        return holder;
    }

    public void migrateLegacyHideListIfExists(HideListHolder holder, LegacyChannelMemberResolver resolver) {
        Path filepath = LegacyHideList.getFilepath(this.rootDataDirectory);
        if (!Files.isRegularFile(filepath)) {
            return;
        }

        logger().info("Migrating legacy hide list...");

        Result<LegacyHideList, DecodeError> result = LegacyHideList.loadFromYaml(filepath);
        if (result.isFailure()) {
            logger().error("Failed to load legacy hide list: {}", result.unwrapError());
            return;
        }

        LegacyHideList legacyHideList = result.unwrap();
        holder.importFromData(legacyHideList.toHideListData(resolver, member -> logger().warn("Failed to get the uuid of the legacy channel member: {}", member)));

        try {
            Files.move(filepath, this.createBackupFilepath("hidelist", "yml"));
        } catch (IOException e) {
            logger().error("Failed to rename {}", filepath.toAbsolutePath());
        }

        this.saveHideListData(holder.exportToData());
    }

    private void saveHideListData(HideListData data) {
        Path filepath = this.dataDirectory.resolve(HIDE_LIST_DATA_FILENAME);
        YamlIO.DEFAULT.encodeTo(filepath, HideListData.CODEC, data)
                .inspectError(error -> logger().error("Failed to save hide list: {}", error));
    }

    private Path createBackupFilepath(String filename, String extension) {
        String datetime = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm-ss").format(LocalDateTime.now());
        return this.rootDataDirectory.resolve(filename + "_" + datetime + "." + extension);
    }

}
