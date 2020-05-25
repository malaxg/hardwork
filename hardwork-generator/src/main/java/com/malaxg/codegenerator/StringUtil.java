package com.malaxg.codegenerator;

/**
 * hardwork
 *
 * @author wangrong
 * @version [版本号, 2019年1月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class StringUtil
{
    /**
     * 数据库类型->JAVA类型
     *
     * @param dbType 数据库类型
     * @return JAVA类型
     */
    private static String typeMapping(String dbType)
    {
        String javaType = "";
        if ("int|integer".contains(dbType))
        {
            javaType = "Integer";
        }
        else if ("float|double|decimal|real".contains(dbType))
        {
            javaType = "Double";
        }
        else if ("date|time|datetime|timestamp".contains(dbType))
        {
            javaType = "Date";
        }
        else
        {
            javaType = "String";
        }
        return javaType;
    }
    
    /**
     * 驼峰转换为下划线
     */
    public static String underscoreName(String camelCaseName)
    {
        StringBuilder result = new StringBuilder();
        if (camelCaseName != null && camelCaseName.length() > 0)
        {
            result.append(camelCaseName.substring(0, 1).toLowerCase());
            for (int i = 1; i < camelCaseName.length(); i++)
            {
                char ch = camelCaseName.charAt(i);
                if (Character.isUpperCase(ch))
                {
                    result.append("_");
                    result.append(Character.toLowerCase(ch));
                }
                else
                {
                    result.append(ch);
                }
            }
        }
        return result.toString();
    }
    
    public static String toLowerCaseFirstOne(String s)
    {
        if (Character.isLowerCase(s.charAt(0)))
        {
            return s;
        }
        else
        {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
    
    //首字母转大写
    public static String toUpperCaseFirstOne(String s)
    {
        if (Character.isUpperCase(s.charAt(0)))
        {
            return s;
        }
        else
        {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
    
    /**
     * 下划线转换为驼峰
     */
    public static String camelCaseName(String underscoreName)
    {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.length() > 0)
        {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++)
            {
                char ch = underscoreName.charAt(i);
                if ("_".charAt(0) == ch)
                {
                    flag = true;
                }
                else
                {
                    if (flag)
                    {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    }
                    else
                    {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }
}
