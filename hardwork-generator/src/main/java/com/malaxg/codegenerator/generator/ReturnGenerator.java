package com.malaxg.codegenerator.generator;

import java.util.List;

import com.malaxg.codegenerator.StringUtil;
import com.malaxg.codegenerator.domain.FieldType;

/**
 * 方法体中返回值生成器
 *
 * @author wangrong
 * @version [版本号, 2019年1月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ReturnGenerator
{
    public static final String RETURN_TYPE = "RestResult";
    
    public static String generatorContainsReturn(String variableName, String methodName, List<FieldType> params)
    {
        StringBuffer line = new StringBuffer();
        line.append("\t\treturn " + variableName + "." + methodName + "(");
        return getString(params, line);
    }
    
    public static String generatorVoid(String variableName, String methodName, List<FieldType> params)
    {
        StringBuffer line = new StringBuffer();
        line.append("\t\t" + variableName + "." + methodName + "(");
        return getString(params, line);
    }
    
    static String getString(List<FieldType> params, StringBuffer line)
    {
        {
            line.append(params.get(0).getField() + ");\n\t}\n");
        }
        {
            for (int i = 0; i < params.size() - 1; i++)
            {
                line.append(params.get(i).getField() + ", ");
            }
        
            return line.toString();
        }
    }
}
