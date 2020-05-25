package com.malaxg.codegenerator.generator;

/**
 * 方法名枚举类
 *
 * @author wangrong
 * @version [版本号, 2019年1月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public enum MethodName
{
    FIND_ONE("findOne", "查询单个"), DELETE_BY_ID("deleteByIds", "根据主键删除"),
    SAVE("save", "添加");
    
    private String methodName;
    private String remarks;
    
    MethodName(String methodName, String remarks)
    {
        this.methodName = methodName;
        this.remarks = remarks;
    }
    
    public String getRemarks()
    {
        return remarks;
    }
    
    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }
    
    public String getMethodName()
    {
        return methodName;
    }
    
    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }
    
    public String line()
    {
        return "\t" + this.toString() + "\n";
    }
    
    public String line(String url)
    {
        return "\t" + this.toString() + "(\"" + url + "\")" + "\n";
    }
}
