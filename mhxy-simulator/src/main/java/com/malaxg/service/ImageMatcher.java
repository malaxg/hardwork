package com.malaxg.service;

import com.malaxg.dto.MatchResult;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class ImageMatcher {
    private static final Logger log = LoggerFactory.getLogger(ImageMatcher.class);

    public MatchResult matchPng(String screenshotPath, String templatePath, double threshold) {
        try {
            Mat screenshot = Imgcodecs.imread(screenshotPath);
            Mat template = Imgcodecs.imread(templatePath);

            if (screenshot.empty() || template.empty()) {
                throw new IllegalArgumentException("无法加载图像文件");
            }

            // 如果模板比截图还大，直接返回失败
            if (template.cols() > screenshot.cols() || template.rows() > screenshot.rows()) {
                System.out.println("模板尺寸大于截图，无法匹配");
                return new MatchResult(new Point(0, 0), 0, false);
            }

            Mat result = new Mat();
            Imgproc.matchTemplate(screenshot, template, result, Imgproc.TM_CCOEFF_NORMED);

            Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
            double maxVal = mmr.maxVal;
            Point maxLoc = mmr.maxLoc;

            if (maxVal >= threshold) {
                // 创建调试图像
//                Mat debugImage = screenshot.clone();
//
//                // 绘制匹配区域
//                Point topLeft = maxLoc;
//                Point bottomRight = new Point(maxLoc.x + template.cols(), maxLoc.y + template.rows());
//                Imgproc.rectangle(debugImage, topLeft, bottomRight, new Scalar(0, 255, 0), 3);
//
//                // 绘制中心点
//                Point centerPoint = new Point(centerX, centerY);
//                Imgproc.circle(debugImage, centerPoint, 8, new Scalar(0, 0, 255), -1);
//
//                // 添加文本信息
//                String infoText = String.format("Center: (%.1f, %.1f) Score: %.3f", centerX, centerY, maxVal);
//                Imgproc.putText(debugImage, infoText,
//                        new Point(maxLoc.x, maxLoc.y - 10),
//                        Imgproc.FONT_HERSHEY_SIMPLEX, 0.7, new Scalar(0, 0, 255), 2);
//
//                Imgcodecs.imwrite("debug_match_result.png", debugImage);
//                debugImage.release();

                // 计算安全的随机点击位置
                Point clickPoint = this.calculateSafeRandomClickPoint(
                        maxLoc, template.width(), template.height(), screenshot.width(), screenshot.height());

                return new MatchResult(clickPoint, maxVal, true);
            } else {
                System.out.println("匹配置信度低于阈值: " + maxVal + " < " + threshold);
                return new MatchResult(new Point(0, 0), maxVal, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new MatchResult(new Point(0, 0), 0, false);
        }
    }

    /**
     * 在匹配到的按钮区域内计算安全的随机点击位置
     * @param matchTopLeft 匹配区域的左上角坐标
     * @param templateWidth 模板图片宽度
     * @param templateHeight 模板图片高度
     * @param screenWidth 屏幕宽度（可选，用于边界检查）
     * @param screenHeight 屏幕高度（可选，用于边界检查）
     * @return 安全的随机点击位置
     */
    public Point calculateSafeRandomClickPoint(Point matchTopLeft, int templateWidth, int templateHeight,
                                               Integer screenWidth, Integer screenHeight) {
        Random random = new Random();
        // 计算按钮区域的边界
        int buttonLeft = (int) matchTopLeft.x;
        int buttonTop = (int) matchTopLeft.y;
        int buttonRight = buttonLeft + templateWidth;
        int buttonBottom = buttonTop + templateHeight;

        log.debug("按钮区域: 左{} 上{} 右{} 下{}, 尺寸: {}x{}",
                buttonLeft, buttonTop, buttonRight, buttonBottom, templateWidth, templateHeight);

        // 安全边界检查（确保不会点击到按钮边缘）
        int safeMargin = calculateSafeMargin(templateWidth, templateHeight);

        // 计算安全的点击区域（在按钮内部，距离边缘有一定距离）
        int safeLeft = buttonLeft + safeMargin;
        int safeTop = buttonTop + safeMargin;
        int safeRight = buttonRight - safeMargin;
        int safeBottom = buttonBottom - safeMargin;

        // 最终边界检查（确保安全区域有效）
        safeLeft = Math.max(buttonLeft, safeLeft);
        safeTop = Math.max(buttonTop, safeTop);
        safeRight = Math.min(buttonRight, safeRight);
        safeBottom = Math.min(buttonBottom, safeBottom);

        // 如果安全区域太小，使用原始按钮区域（但至少保证1像素）
        if (safeRight <= safeLeft) {
            safeLeft = buttonLeft;
            safeRight = buttonRight;
            log.warn("安全区域宽度太小，使用原始按钮宽度");
        }
        if (safeBottom <= safeTop) {
            safeTop = buttonTop;
            safeBottom = buttonBottom;
            log.warn("安全区域高度太小，使用原始按钮高度");
        }

        // 屏幕边界检查（如果提供了屏幕尺寸）
        if (screenWidth != null && screenHeight != null) {
            safeRight = Math.min(safeRight, screenWidth - 1);
            safeBottom = Math.min(safeBottom, screenHeight - 1);
            safeLeft = Math.max(safeLeft, 0);
            safeTop = Math.max(safeTop, 0);
        }

        // 生成随机点击位置
        int randomX = safeLeft + random.nextInt(safeRight - safeLeft);
        int randomY = safeTop + random.nextInt(safeBottom - safeTop);

        log.debug("安全点击区域: 左{} 上{} 右{} 下{}, 随机位置: ({}, {})",
                safeLeft, safeTop, safeRight, safeBottom, randomX, randomY);

        return new Point(randomX, randomY);
    }

    /**
     * 计算安全边距（基于按钮大小动态计算）
     */
    private int calculateSafeMargin(int buttonWidth, int buttonHeight) {
        // 随机点击偏移量（像素）
        int baseMargin = 15;

        // 根据按钮大小动态调整安全边距
        int dynamicMargin;
        if (buttonWidth < 50 || buttonHeight < 50) {
            // 小按钮使用较小的边距
            dynamicMargin = Math.max(2, baseMargin / 3);
        } else if (buttonWidth > 200 || buttonHeight > 200) {
            // 大按钮使用较大的边距
            dynamicMargin = baseMargin * 2;
        } else {
            // 中等按钮使用标准边距
            dynamicMargin = baseMargin;
        }

        // 确保边距不超过按钮尺寸的40%
        int maxMarginByWidth = (int) (buttonWidth * 0.4);
        int maxMarginByHeight = (int) (buttonHeight * 0.4);
        dynamicMargin = Math.min(dynamicMargin, Math.min(maxMarginByWidth, maxMarginByHeight));

        log.debug("按钮尺寸: {}x{}, 计算的安全边距: {}", buttonWidth, buttonHeight, dynamicMargin);
        return dynamicMargin;
    }
}