package com.malaxg.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class NativeLoader {
    public static void loadLibraryFromJar(String path) throws IOException {
        // 从JAR包中获取DLL的输入流
        InputStream inputStream = NativeLoader.class.getResourceAsStream(path);
        if (inputStream == null) {
            throw new FileNotFoundException("DLL not found: " + path);
        }

        // 获取DLL文件名
        String[] parts = path.split("/");
        String filename = parts[parts.length - 1];

        // 创建临时文件
        Path tempFile = Files.createTempFile(filename, ".dll");
        // 确保程序退出时删除临时文件
        tempFile.toFile().deleteOnExit();

        // 将DLL从JAR复制到临时文件
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        inputStream.close();

        // 加载临时DLL文件
        System.load(tempFile.toAbsolutePath().toString());
    }
}
