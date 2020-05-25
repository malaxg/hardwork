package com.malaxg.codegenerator.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * hardwork
 *
 * @author wangrong
 * @version [版本号, 2019年1月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Entity
@Table(name = "information_schema.columns")
public class MysqlColumnInfo
{
    /** 字段名称 */
    @Id
    private String columnName;
    /** 列类型  例如：double(20,3)  varchar(500) */
    private String columnType;
    /** 所属数据库 */
    private String tableSchema;
    /** 数据类型 */
    private String dataType;
    /** 是否为主键 */
    private String columnKey;
    /** 字段默认值 */
    private String columnDefault;
    /** 字段注释 */
    private String columnComment;
    
    public String getColumnName()
    {
        return columnName;
    }
    
    public String getDataType()
    {
        return dataType;
    }
    
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }
    
    public String getColumnDefault()
    {
        return columnDefault;
    }
    
    public void setColumnDefault(String columnDefault)
    {
        this.columnDefault = columnDefault;
    }
    
    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }
    
    public String getColumnType()
    {
        return columnType;
    }
    
    public void setColumnType(String columnType)
    {
        this.columnType = columnType;
    }
    
    public String getColumnComment()
    {
        return columnComment;
    }
    
    public void setColumnComment(String columnComment)
    {
        this.columnComment = columnComment;
    }
    
    public String getColumnKey()
    {
        return columnKey;
    }
    
    public void setColumnKey(String columnKey)
    {
        this.columnKey = columnKey;
    }
    
    public String getTableSchema()
    {
        return tableSchema;
    }
    
    public void setTableSchema(String tableSchema)
    {
        this.tableSchema = tableSchema;
    }
}
