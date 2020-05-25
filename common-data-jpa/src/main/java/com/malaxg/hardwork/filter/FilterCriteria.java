package com.malaxg.hardwork.filter;
/*
 * FilterCriteria实体类
 */
/**
 * FilterCriteria
 *
 * @author wangrong
 * @version [版本号, 2019年2月20日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class FilterCriteria
{
    private String fieldName;
    private FilterType filterType;
    private Object value;
    /**
     * 无参构造
     */
    public FilterCriteria()
    {
        super();
    }
    /**
     * 有参构造
     * @param fieldName 文件名
     * @param filterType 文件类型
     * @param value value
     */
    public FilterCriteria(String fieldName, FilterType filterType, Object value)
    {
        super();
        this.fieldName = fieldName;
        this.filterType = filterType;
        this.value = value;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }

    public FilterType getFilterType()
    {
        return filterType;
    }

    public void setFilterType(FilterType filterType)
    {
        this.filterType = filterType;
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }
}