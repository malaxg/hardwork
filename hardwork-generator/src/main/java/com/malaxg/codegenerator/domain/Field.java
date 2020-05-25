package com.malaxg.codegenerator.domain;

/**
 * hardwork
 *
 * @author wangrong
 * @version [版本号, 2019年1月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Field
{
    //字段名称
    private String name;
    //字段类型
    private String type;
    //字段注释
    private String fieldRemarks;
    //是否为主键
    private int key;
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getFieldRemarks()
    {
        return fieldRemarks;
    }
    
    public void setFieldRemarks(String fieldRemarks)
    {
        this.fieldRemarks = fieldRemarks;
    }
    
    public int getKey()
    {
        return key;
    }
    
    public void setKey(int key)
    {
        this.key = key;
    }
}
