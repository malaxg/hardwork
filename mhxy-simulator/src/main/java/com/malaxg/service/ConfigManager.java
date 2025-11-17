package com.malaxg.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.malaxg.dto.EmuConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static final String CONFIG_PATH = "application.yml";

    private AppConfig appConfig;

    public ConfigManager() throws IOException {
        loadConfig();
    }

    private void loadConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        // 使用 ClassLoader 从 classpath 加载文件
        InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_PATH);

        if (inputStream == null) {
            throw new RuntimeException("在 classpath 中找不到 application.yml 文件");
        }

        appConfig = mapper.readValue(inputStream, AppConfig.class);
        logger.info("配置文件加载成功，共配置了 {} 个模拟器", appConfig.getMumu().getEmulators().size());
    }

    public EmuConfig getEmulatorConfig(int index) {
        return appConfig.getMumu().getEmulators().stream()
                .filter(e -> e.getIndex() == index)
                .findFirst()
                .orElse(null);
    }

    public List<EmuConfig> getAllEmulatorConfigs() {
        return appConfig.getMumu().getEmulators();
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }

    public boolean validateUser(String username, String password) {
        return appConfig.getUser().getUsername().equals(username) &&
                appConfig.getUser().getPassword().equals(password);
    }
}

class AppConfig {
    private MumuConfig mumu;
    private UserConfig user;
    private LoggingConfig logging;

    public MumuConfig getMumu() {
        return mumu;
    }

    public void setMumu(MumuConfig mumu) {
        this.mumu = mumu;
    }

    public UserConfig getUser() {
        return user;
    }

    public void setUser(UserConfig user) {
        this.user = user;
    }

    public LoggingConfig getLogging() {
        return logging;
    }

    public void setLogging(LoggingConfig logging) {
        this.logging = logging;
    }
}

class MumuConfig {
    private List<EmuConfig> emulators;
    private AdbConfig adb;
    private ImageConfig image;
    private BehaviorConfig behavior;

    public List<EmuConfig> getEmulators() {
        return emulators;
    }

    public void setEmulators(List<EmuConfig> emulators) {
        this.emulators = emulators;
    }

    public AdbConfig getAdb() {
        return adb;
    }

    public void setAdb(AdbConfig adb) {
        this.adb = adb;
    }

    public ImageConfig getImage() {
        return image;
    }

    public void setImage(ImageConfig image) {
        this.image = image;
    }

    public BehaviorConfig getBehavior() {
        return behavior;
    }

    public void setBehavior(BehaviorConfig behavior) {
        this.behavior = behavior;
    }
}

class AdbConfig {
    private String path;
    private int connectionTimeout;
    private int commandTimeout;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getCommandTimeout() {
        return commandTimeout;
    }

    public void setCommandTimeout(int commandTimeout) {
        this.commandTimeout = commandTimeout;
    }
}

class ImageConfig {
    private String templateDir;
    private double matchThreshold;
    private int clickOffsetRange;

    public String getTemplateDir() {
        return templateDir;
    }

    public void setTemplateDir(String templateDir) {
        this.templateDir = templateDir;
    }

    public double getMatchThreshold() {
        return matchThreshold;
    }

    public void setMatchThreshold(double matchThreshold) {
        this.matchThreshold = matchThreshold;
    }

    public int getClickOffsetRange() {
        return clickOffsetRange;
    }

    public void setClickOffsetRange(int clickOffsetRange) {
        this.clickOffsetRange = clickOffsetRange;
    }
}

class BehaviorConfig {
    private int checkInterval;
    private int maxRetryCount;
    private int clickDelay;

    public int getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(int checkInterval) {
        this.checkInterval = checkInterval;
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public int getClickDelay() {
        return clickDelay;
    }

    public void setClickDelay(int clickDelay) {
        this.clickDelay = clickDelay;
    }
}

class UserConfig {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class LoggingConfig {
    private LevelConfig level;

    public LevelConfig getLevel() {
        return level;
    }

    public void setLevel(LevelConfig level) {
        this.level = level;
    }
}

class LevelConfig {
    private String com;

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }
}