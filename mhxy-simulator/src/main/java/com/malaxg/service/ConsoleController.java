package com.malaxg.service;

import com.malaxg.dto.EmuConfig;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConsoleController {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleController.class);

    @FXML private ListView<EmuConfig> emulatorListView;
    @FXML private TextArea logArea;
    @FXML private Button startButton;
    @FXML private Button stopButton;
    @FXML private Button refreshButton;
    @FXML private Button selectAllButton;
    @FXML private Button deselectAllButton;
    @FXML private Button clearLogButton;
    @FXML private CheckBox autoScrollCheckBox;
    @FXML private Label statusLabel;
    @FXML private Label runningCountLabel;
    @FXML private Label totalCountLabel;

    private ConfigManager configManager;
    private Stage primaryStage;
    private final Map<Integer, MuMuService> runningServices = new ConcurrentHashMap<>();
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private final ObservableList<EmuConfig> emulatorList = FXCollections.observableArrayList();

    public void initialize() {
        setupListView();
        setupLogArea();
        updateStatus();
    }

    public void initializeData() {
        loadEmulatorList();
        checkAdbStatus(); // 添加ADB状态检查
    }

    // 在ConsoleController类中添加以下方法
    private void checkAdbStatus() {
        new Thread(() -> {
            try {
                // 获取ADB路径
                String adbPath = configManager.getAppConfig().getMumu().getAdb().getPath();
                File adbFile = new File(adbPath);

                Platform.runLater(() -> {
                    if (adbFile.exists()) {
                        appendLog("ADB路径检查: " + adbPath + " [存在]");
                    } else {
                        appendLog("ADB路径检查: " + adbPath + " [不存在，请检查配置]");
                    }
                });

                // 测试ADB版本
                Process process = Runtime.getRuntime().exec("\"" + adbPath + "\" version");
                if (process.waitFor(5, TimeUnit.SECONDS) && process.exitValue() == 0) {
                    Platform.runLater(() -> {
                        appendLog("ADB版本检查: [正常]");
                        statusLabel.setText("ADB状态: 正常");
                    });
                } else {
                    Platform.runLater(() -> {
                        appendLog("ADB版本检查: [异常，请检查ADB配置]");
                        statusLabel.setText("ADB状态: 异常");
                    });
                }
            } catch (Exception e) {
                Platform.runLater(() -> {
                    appendLog("ADB检查失败: " + e.getMessage());
                    statusLabel.setText("ADB状态: 检查失败");
                });
            }
        }).start();
    }

    private void setupListView() {
        emulatorListView.setItems(emulatorList);
        emulatorListView.setCellFactory(param -> new ListCell<EmuConfig>() {
            @Override
            protected void updateItem(EmuConfig item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.toString());
                    if (item.isRunning()) {
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #2c3e50;");
                    }
                }
            }
        });

        emulatorListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void setupLogArea() {
        logArea.setEditable(false);
    }

    private void loadEmulatorList() {
        emulatorList.clear();
        emulatorList.addAll(configManager.getAllEmulatorConfigs());
        updateCountLabels();
    }

    // 添加缺失的updateCountLabels方法
    private void updateCountLabels() {
        Platform.runLater(() -> {
            long runningCount = emulatorList.stream().filter(EmuConfig::isRunning).count();
            long totalCount = emulatorList.size();

            runningCountLabel.setText(String.valueOf(runningCount));
            totalCountLabel.setText(String.valueOf(totalCount));

            statusLabel.setText("运行中: " + runningCount + "/" + totalCount);
        });
    }

    // 修改updateStatus方法，让它调用updateCountLabels
    private void updateStatus() {
        // 更新模拟器运行状态
        for (EmuConfig emulator : emulatorList) {
            MuMuService service = runningServices.get(emulator.getIndex());
            emulator.setRunning(service != null && service.isRunning());
        }

        // 更新计数标签
        updateCountLabels();

        // 刷新ListView显示
        emulatorListView.refresh();
    }

    @FXML
    private void handleStart() {
        List<EmuConfig> selectedEmulators = emulatorListView.getSelectionModel().getSelectedItems();

        if (selectedEmulators.isEmpty()) {
            showAlert("提示", "请先选择要启动的模拟器");
            return;
        }

        for (EmuConfig emulator : selectedEmulators) {
            if (runningServices.containsKey(emulator.getIndex())) {
                appendLog("模拟器 " + emulator.getName() + " 已在运行中");
                continue;
            }

            MuMuService service = new MuMuService(emulator, configManager);
            runningServices.put(emulator.getIndex(), service);
            threadPool.execute(service);

            appendLog("启动模拟器: " + emulator.getName());
        }

        // 添加延迟，等待服务启动后再更新状态
        new Thread(() -> {
            try {
                Thread.sleep(500);
                Platform.runLater(this::updateStatus);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        startButton.setDisable(true);
        stopButton.setDisable(false);
    }

    @FXML
    private void handleStop() {
        List<EmuConfig> selectedEmulators = emulatorListView.getSelectionModel().getSelectedItems();

        if (selectedEmulators.isEmpty()) {
            showAlert("提示", "请先选择要停止的模拟器");
            return;
        }

        for (EmuConfig emulator : selectedEmulators) {
            MuMuService service = runningServices.remove(emulator.getIndex());
            if (service != null) {
                service.stop();
                appendLog("停止模拟器: " + emulator.getName());
            }
        }

        // 添加延迟，等待服务停止后再更新状态
        new Thread(() -> {
            try {
                Thread.sleep(500);
                Platform.runLater(this::updateStatus);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        startButton.setDisable(false);
        stopButton.setDisable(true);
    }

    @FXML
    private void handleRefresh() {
        loadEmulatorList();
        updateStatus();
        appendLog("刷新模拟器状态");
    }

    @FXML
    private void handleSelectAll() {
        emulatorListView.getSelectionModel().selectAll();
    }

    @FXML
    private void handleDeselectAll() {
        emulatorListView.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleClearLog() {
        logArea.clear();
    }

    private void appendLog(String message) {
        Platform.runLater(() -> {
            String timestamp = dateFormat.format(new Date());
            logArea.appendText("[" + timestamp + "] " + message + "\n");

            if (autoScrollCheckBox.isSelected()) {
                logArea.setScrollTop(Double.MAX_VALUE);
            }
        });
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void shutdown() {
        // 停止所有运行中的服务
        runningServices.values().forEach(MuMuService::stop);
        threadPool.shutdown();
        logger.info("控制台控制器已关闭");
    }
}