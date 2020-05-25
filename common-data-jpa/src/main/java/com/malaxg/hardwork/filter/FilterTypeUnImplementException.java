package com.malaxg.hardwork.filter;

/**
 * FilterTypeUnImplementException
 * @version  [版本号, 2019年2月20日]
 * @see      [相关类/方法]
 * @since    [产品/模块版本]
 */
public class FilterTypeUnImplementException extends RuntimeException
{
    private static final long serialVersionUID = 4241084635366075219L;

    /** 
     * 默认构造函数
     * @param message message
     */
    public FilterTypeUnImplementException(String message)
    {
        super(message);
    }
}