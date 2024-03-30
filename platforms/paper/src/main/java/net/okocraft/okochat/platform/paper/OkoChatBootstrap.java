package net.okocraft.okochat.platform.paper;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import net.okocraft.okochat.core.util.OkoChatLogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.helpers.SubstituteLogger;

public class OkoChatBootstrap implements PluginBootstrap {
    @Override
    public void bootstrap(@NotNull BootstrapContext bootstrapContext) {
        ((SubstituteLogger) OkoChatLogger.logger()).setDelegate(bootstrapContext.getLogger());
    }
}
