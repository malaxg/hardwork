package com.malaxg.service;

import com.malaxg.dto.EmuConfig;
import com.malaxg.dto.MatchResult;
import com.malaxg.hardwork.util.ResourceToFile;
import org.opencv.core.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

public class MuMuService implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MuMuService.class);

    private final EmuConfig emulatorConfig;
    private final ConfigManager configManager;
    private final ImageMatcher imageMatcher;
    private volatile boolean running = false;
    private final ExecutorService executor;
    private final String adbPath;

    public MuMuService(EmuConfig emulatorConfig, ConfigManager configManager) {
        this.emulatorConfig = emulatorConfig;
        this.configManager = configManager;
        this.imageMatcher = new ImageMatcher();
        this.executor = Executors.newSingleThreadExecutor();

        // 从配置中获取ADB路径
        this.adbPath = configManager.getAppConfig().getMumu().getAdb().getPath();
        logger.info("模拟器 {} 使用ADB路径: {}", emulatorConfig.getIndex(), adbPath);
    }

    @Override
    public void run() {
        running = true;
        emulatorConfig.setRunning(true);
        logger.info("开始控制模拟器 {} (端口: {})", emulatorConfig.getIndex(), emulatorConfig.getPort());

        try {
            // 验证ADB路径
            if (!validateAdbPath()) {
                logger.error("ADB路径无效或ADB不可用: {}", adbPath);
                return;
            }

            connectToEmulator();

            while (running && !Thread.currentThread().isInterrupted()) {
                try {
                    processEmulator();
                    Thread.sleep(configManager.getAppConfig().getMumu().getBehavior().getCheckInterval());
                } catch (InterruptedException e) {
                    logger.info("模拟器 {} 控制线程被中断", emulatorConfig.getIndex());
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    logger.error("模拟器 {} 处理过程中发生错误", emulatorConfig.getIndex(), e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } finally {
            emulatorConfig.setRunning(false);
            running = false;
            logger.info("停止控制模拟器 {}", emulatorConfig.getIndex());
        }
    }

    private boolean validateAdbPath() {
        try {
            File adbFile = new File(adbPath);
            if (!adbFile.exists()) {
                logger.error("ADB文件不存在: {}", adbPath);
                return false;
            }

            if (!adbFile.canExecute()) {
                logger.error("ADB文件不可执行: {}", adbPath);
                return false;
            }

            // 测试ADB版本命令
            String versionCommand = String.format("\"%s\" version", adbPath);
            boolean success = executeCommand(versionCommand, 5);
            if (success) {
                logger.info("ADB路径验证成功: {}", adbPath);
                return true;
            } else {
                logger.error("ADB版本命令执行失败");
                return false;
            }
        } catch (Exception e) {
            logger.error("ADB路径验证失败: {}", e.getMessage());
            return false;
        }
    }

    private void connectToEmulator() throws IOException, InterruptedException, TimeoutException {
        String adbPath = configManager.getAppConfig().getMumu().getAdb().getPath();
        String connectCommand = String.format("%s connect 127.0.0.1:%d", adbPath, emulatorConfig.getPort());
        executeCommand(connectCommand, configManager.getAppConfig().getMumu().getAdb().getConnectionTimeout());
        logger.info("模拟器 {} 连接成功", emulatorConfig.getIndex());
    }

    private void processEmulator() {
        try {
            String screenshotPath = takeScreenshot();
            if (screenshotPath == null) {
                logger.warn("模拟器 {} 截图失败", emulatorConfig.getIndex());
                return;
            }
            String fileUrl = "templates/网易邮箱.png";
            File templateFile = new ResourceToFile().loadImageFromResources(fileUrl);
            if (!templateFile.exists()) {
                logger.warn("未找到模板图片:" + fileUrl);
                return;
            }


            MatchResult result = imageMatcher.matchPng(screenshotPath, templateFile.getAbsolutePath(),
                    configManager.getAppConfig().getMumu().getImage().getMatchThreshold());

            if (result.isMatched()) {
                logger.info("模拟器 {} 匹配到模板: {}", emulatorConfig.getIndex(), templateFile.getName());
                // 点击
                click(result.getMatchLocation());
                Thread.sleep(configManager.getAppConfig().getMumu().getBehavior().getClickDelay());
            }

            new File(screenshotPath).delete();

        } catch (Exception e) {
            logger.error("模拟器 {} 处理过程中发生错误", emulatorConfig.getIndex(), e);
        }
    }

    private String takeScreenshot() {
        try {
            String adbPath = configManager.getAppConfig().getMumu().getAdb().getPath();
            String screenshotName = String.format("screenshot_%d_%d.png", emulatorConfig.getIndex(), System.currentTimeMillis());
            String remotePath = "/sdcard/" + screenshotName;
            String localPath = screenshotName;
            String screenshotCommand = String.format("%s -s 127.0.0.1:%d shell screencap -p %s",
                    adbPath, emulatorConfig.getPort(), remotePath);
            String pullCommand = String.format("%s -s 127.0.0.1:%d pull %s %s",
                    adbPath, emulatorConfig.getPort(), remotePath, localPath);

            if (executeCommand(screenshotCommand, configManager.getAppConfig().getMumu().getAdb().getCommandTimeout()) &&
                    executeCommand(pullCommand, configManager.getAppConfig().getMumu().getAdb().getCommandTimeout())) {
                return localPath;
            }
        } catch (Exception e) {
            logger.error("模拟器 {} 截图失败", emulatorConfig.getIndex(), e);
        }
        return null;
    }

    private void click(Point point) {
        try {
            String adbPath = configManager.getAppConfig().getMumu().getAdb().getPath();
            String clickCommand = String.format("%s -s 127.0.0.1:%d shell input tap %d %d",
                    adbPath, emulatorConfig.getPort(), (int) point.x, (int) point.y);
            executeCommand(clickCommand, configManager.getAppConfig().getMumu().getAdb().getCommandTimeout());
            logger.info("模拟器 {} 点击位置: ({}, {})", emulatorConfig.getIndex(), (int) point.x, (int) point.y);
        } catch (Exception e) {
            logger.error("模拟器 {} 点击操作失败", emulatorConfig.getIndex(), e);
        }
    }

    private boolean executeCommand(String command, int timeoutSeconds)
            throws IOException, InterruptedException, TimeoutException {
        Process process = Runtime.getRuntime().exec(command);

        if (!process.waitFor(timeoutSeconds, TimeUnit.SECONDS)) {
            process.destroy();
            throw new TimeoutException("命令执行超时: " + command);
        }

        return process.exitValue() == 0;
    }

    public void stop() {
        running = false;
        emulatorConfig.setRunning(false);
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.info("模拟器 {} 控制器已停止", emulatorConfig.getIndex());
    }

    public EmuConfig getEmulatorConfig() {
        return emulatorConfig;
    }

    public boolean isRunning() {
        return running;
    }

}