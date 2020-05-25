package com.malaxg.codegenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.apache.tomcat.util.http.fileupload.FileUtils;

/**
 * hardwork
 *
 * @author wangrong
 * @version [版本号, 2019年1月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class FileUtil
{
    /**
     * 创建文件
     *
     * @param author 作者
     * @param className 路径跟文件名
     * @return File对象
     */
    public static File createFile(String author, String className)
    {
        File file = new File(author + "\\" + className + ".java");
        try
        {
            //获取父目录
            File fileParent = file.getParentFile();
            if (!fileParent.exists())
            {
                fileParent.mkdirs();
            }
            //创建文件
            if (!file.exists())
            {
                file.createNewFile();
            }
            else
            {
                FileUtils.forceDelete(file);
            }
        }
        catch (Exception e)
        {
            file = null;
            System.err.println("新建文件操作出错");
            e.printStackTrace();
        }
        return file;
    }
    
    /**
     * 字符流写入文件
     *
     * @param file file对象
     * @param stringBuffer 要写入的数据
     */
    public static void fileWriter(File file, StringBuffer stringBuffer)
    {
        //字符流
        try
        {
            FileWriter resultFile = new FileWriter(file, true);//true,则追加写入 false,则覆盖写入
            PrintWriter myFile = new PrintWriter(resultFile);
            //写入
            myFile.println(stringBuffer.toString());
            
            myFile.close();
            resultFile.close();
        }
        catch (Exception e)
        {
            System.err.println("写入操作出错");
            e.printStackTrace();
        }
    }
}