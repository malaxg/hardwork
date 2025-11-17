package com.malaxg.hardwork.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ResourceToFile {
    public File loadImageFromResources(String imagePath) throws IOException {
        // 获取当前类的ClassLoader
        ClassLoader classLoader = getClass().getClassLoader();

        // 1. 获取资源的输入流 (resourcePath 是相对于classpath根目录的路径)
        try (InputStream inputStream = classLoader.getResourceAsStream(imagePath)) {
            if (inputStream == null) {
                throw new IOException("图片未找到: " + imagePath);
            }

            // 2. 创建临时文件
            // 参数前缀、后缀和指定目录（为null则使用系统默认临时目录）
            Path tempFile = Files.createTempFile("temp_image_", ".png");

            // 3. 将输入流的内容复制到临时文件
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

            return tempFile.toFile();
        }
        // 4. 输入流会自动关闭 (try-with-resources)
    }
}
