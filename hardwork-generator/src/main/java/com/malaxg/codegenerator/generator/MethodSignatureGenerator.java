package com.malaxg.codegenerator.generator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.malaxg.codegenerator.domain.FieldType;
import com.malaxg.hardwork.util.DataVerificationUtil;

/**
 * 方法签名生成器
 *
 * @author wangrong
 * @version [版本号, 2019年1月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class MethodSignatureGenerator
{
    private Map paramTypeMap = new HashMap();
    
    {
        paramTypeMap.put("Integer", "@RequestParam");
        paramTypeMap.put("int", "@RequestParam");
        paramTypeMap.put("Long", "@RequestParam");
        paramTypeMap.put("long", "@RequestParam");
        paramTypeMap.put("String", "@RequestParam");
        paramTypeMap.put("Byte", "@RequestParam");
        paramTypeMap.put("byte", "@RequestParam");
        paramTypeMap.put("Char", "@RequestParam");
        paramTypeMap.put("char", "@RequestParam");
        paramTypeMap.put("Float", "@RequestParam");
        paramTypeMap.put("float", "@RequestParam");
        paramTypeMap.put("Double", "@RequestParam");
        paramTypeMap.put("double", "@RequestParam");
        paramTypeMap.put("List", "@RequestParam");
    }
    
    public static String generateVoid(String methodName, List<FieldType> params, String requestType)
    {
        StringBuffer line = new StringBuffer();
        
        line.append("\tpublic void " + methodName + "(");
        
        return getString1(params, line, requestType);
    }
    
    public static String generateContainsReturn(String methodName, List<FieldType> params, String requestType)
    {
        StringBuffer line = new StringBuffer();
        
        line.append("\tpublic " + ReturnGenerator.RETURN_TYPE + " " + methodName + "(");
        return getString1(params, line, requestType);
    }
    
    static String getString1(List<FieldType> params, StringBuffer line, String requestType)
    {
        if (params.size() == 1)
        {
            //添加参数注解
            if (!DataVerificationUtil.isEmpty(requestType))
            {
                if ("@RequestBody".equals(requestType))
                {
                    line.append(
                        requestType + " " + params.get(0).getType() + " " +
                            params.get(0).getField() + ")\n\t{\n");
                }
                else
                {
                    line.append(
                        requestType + "(value = \"" + params.get(0).getField() + "\") " + params.get(0).getType() + " "
                            +
                            params.get(0).getField() + ")\n\t{\n");
                }
            }
            else
            {
                line.append(params.get(0).getField() + "\") " + params.get(0).getType() + " " +
                    params.get(0).getField() + ")\n\t{\n");
            }
            
        }
        else
        {
            for (int i = 0; i < params.size() - 1; i++)
            {
                line.append(params.get(i).getType() + " " + params.get(i).getField() + ", ");
            }
            line.append(
                params.get(params.size() - 1).getType() + " " + params.get(params.size() - 1).getField() + ")\n\t{\n");
        }
        return line.toString();
    }
}
