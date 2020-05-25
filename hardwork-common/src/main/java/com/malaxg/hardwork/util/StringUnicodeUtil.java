package com.malaxg.hardwork.util;

/**
 * hardwork
 *
 * @author wangrong
 * @version [版本号, 2019年1月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class StringUnicodeUtil
{
    
    public static void main(String[] args)
    {

        String str1 = "执行部门比例不能为负数";
        String str2 = "所属部门比例不能为负数";
        String str3 = "预算来源比例不能为负数";
        String str4 = "技术领域比例不能为负数";
        String unicode1 = stringToUnicode(str1);
        String unicode2 = stringToUnicode(str2);
        String unicode3 = stringToUnicode(str3);
        String unicode4 = stringToUnicode(str4);
  /*      System.out.println(unicodeToString(
            "\\u4e2d\\u6587\\u540d\\uff0c\\u4e2d\\u6587\\u673a\\u6784\\uff0c\\u804c\\u4f4d\\u6216\\u82f1"
                + "\\u6587\\u540d\\uff0c\\u82f1\\u6587\\u673a\\u6784\\uff0c\\u804c\\u4f4d\\u4e0d\\u80fd\\u91cd\\u590d"
                + "\\u3002"));*/
        System.out.println("字符串转unicode结果：" + unicode1);
        System.out.println("字符串转unicode结果：" + unicode2);
        System.out.println("字符串转unicode结果：" + unicode3);
        System.out.println("字符串转unicode结果：" + unicode4);
        
    }
    
    /**
     * 字符串转unicode
     *
     * @param str
     * @return
     */
    public static String stringToUnicode(String str)
    {
        StringBuffer sb = new StringBuffer();
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++)
        {
            sb.append("\\u" + Integer.toHexString(c[i]));
        }
        return sb.toString();
    }
    
    /**
     * unicode转字符串
     *
     * @param unicode
     * @return
     */
    public static String unicodeToString(String unicode)
    {
        StringBuffer sb = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++)
        {
            int index = Integer.parseInt(hex[i], 16);
            sb.append((char)index);
        }
        return sb.toString();
    }
}
