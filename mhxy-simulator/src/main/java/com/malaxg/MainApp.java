package com.malaxg;

import com.malaxg.controller.LoginController;
import com.malaxg.util.NativeLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.info("启动MuMu模拟器控制台应用程序");

        // 使用getClass().getResource加载FXML文件
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        Scene scene = new Scene(root);

        // 加载CSS样式
        try {
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        } catch (Exception e) {
            logger.warn("无法加载CSS样式文件: {}", e.getMessage());
        }

        primaryStage.setTitle("MuMu模拟器控制台 - 登录");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();

        // 设置关闭事件
        primaryStage.setOnCloseRequest(event -> {
            logger.info("应用程序关闭");
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        // 加载OpenCV本地库
        try {
            // 从JAR包中的路径加载DLL
            NativeLoader.loadLibraryFromJar("/lib/opencv_java451.dll");
            logger.info("OpenCV本地库加载成功");
        } catch (Exception e) {
            logger.error("无法加载OpenCV本地库: {}", e.getMessage());
            System.err.println("无法加载OpenCV本地库，请确保OpenCV正确安装");
        }

        launch(args);
    }


}