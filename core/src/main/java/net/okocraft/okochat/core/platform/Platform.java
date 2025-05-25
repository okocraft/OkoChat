package net.okocraft.okochat.core.platform;

import org.slf4j.Logger;

import java.nio.file.Path;

public interface Platform {

    Logger logger();

    Path dataDirectory();

}
