package com.malaxg.codegenerator.generator;

import java.util.List;

import com.malaxg.codegenerator.domain.FieldType;

import static com.malaxg.codegenerator.generator.ReturnGenerator.RETURN_TYPE;

/**
 * 注释生成器
 *
 * @author wangrong
 * @version [版本号, 2019年1月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RemarksGenerator
{
    /**
     * 设置类的注释模板
     */
    public static String generateClassRemarks(String className, String author)
    {
        StringBuffer line = new StringBuffer();
        line.append("\n" +
            "/**\n"
            + " * " + className + "\n"
            + " * \n"
            + " * @author " + author + "\n"
            + " * @version [版本号, 2099年12月31日]\n"
            + " * @see [相关类/方法]\n"
            + " * @since [产品/模块版本]\n"
            + " */\n");
        
        return line.toString();
    }
    
    /**
     * 设置无返回值的方法注释
     */
    public static String generateMethodRemarks(String methodRemarks, List<FieldType> params,
        String author)
    {
        StringBuffer line = new StringBuffer();
        
        line.append("\n" +
            "\t/**\n"
            + "\t * " + methodRemarks + "\n"
            + "\t * \n");
        
        params.stream().forEach(k -> line.append("\t * @param " + k.getField() + " " + k.getField() + "\n"));
        
        line.append("\t * @author " + author + "\n" + "\t * @see [相关类/方法]\n"
            + "\t */\n");
        
        return line.toString();
    }
    
    /**
     * 设置有返回值的方法注释
     */
    public static String generateMethodRemarksContainsReturn(String methodRemarks, List<FieldType> params,
        String author)
    {
        StringBuffer line = new StringBuffer();
        
        line.append("\n" +
            "\t/**\n"
            + "\t * " + methodRemarks + "\n"
            + "\t * \n");
        
        params.stream().forEach(k -> line.append("\t * @param " + k.getField() + " " + k.getField() + "\n"));
        
        line.append("\t * @return " + RETURN_TYPE + " " + RETURN_TYPE + "\n");
        line.append("\t * @author " + author + "\n" + "\t * @see [相关类/方法]\n"
            + "\t */\n");
        
        return line.toString();
    }
}
