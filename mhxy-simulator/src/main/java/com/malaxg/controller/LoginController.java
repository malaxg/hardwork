package com.malaxg.controller;

import com.malaxg.service.ConfigManager;
import com.malaxg.service.ConsoleController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button cancelButton;
    @FXML private Label errorLabel;

    private ConfigManager configManager;
    private Stage primaryStage;

    public void initialize() {
        try {
            configManager = new ConfigManager();
        } catch (IOException e) {
            showAlert("错误", "配置文件加载失败: " + e.getMessage());
            logger.error("配置文件加载失败", e);
        }

        // 设置回车键登录
        usernameField.setOnAction(e -> handleLogin());
        passwordField.setOnAction(e -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("请输入用户名和密码");
            return;
        }

        if (configManager.validateUser(username, password)) {
            logger.info("用户 {} 登录成功", username);
            openConsole();
        } else {
            showError("用户名或密码错误");
            logger.warn("用户 {} 登录失败", username);
        }
    }

    @FXML
    private void handleCancel() {
        System.exit(0);
    }

    private void openConsole() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/console.fxml"));
            Parent root = loader.load();

            ConsoleController controller = loader.getController();
            controller.setConfigManager(configManager);
            controller.setPrimaryStage(primaryStage);
            controller.initializeData();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle("MuMu模拟器控制台");
            primaryStage.setResizable(true);
            primaryStage.centerOnScreen();

        } catch (IOException e) {
            logger.error("加载控制台界面失败", e);
            showAlert("错误", "加载控制台界面失败: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}