package com.malaxg.codegenerator;

/**
 * hardwork
 *
 * @author wangrong
 * @version [版本号, 2019年1月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Generator
{
    private String database;
    private String author;
    private String className;
    private String restControllerUrl;
    public String getClassName()
    {
        return className;
    }
    
    public void setClassName(String className)
    {
        this.className = className;
    }
    
    public String getAuthor()
    {
        return author;
    }
    
    public void setAuthor(String author)
    {
        this.author = author;
    }
    
    public String getDatabase()
    {
        return database;
    }
    
    public void setDatabase(String database)
    {
        this.database = database;
    }
    
    public String getRestControllerUrl()
    {
        return restControllerUrl;
    }
    
    public void setRestControllerUrl(String restControllerUrl)
    {
        this.restControllerUrl = restControllerUrl;
    }
}
