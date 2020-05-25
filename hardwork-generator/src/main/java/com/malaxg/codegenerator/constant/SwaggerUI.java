package com.malaxg.codegenerator.constant;

/**
 * hardwork
 *
 * @author wangrong
 * @version [版本号, 2019年1月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public enum SwaggerUI {
    ApiOperation("@ApiOperation");
    private String mappingName;

    SwaggerUI(String mappingName) {
        this.mappingName = mappingName;
    }

    public String getMappingName() {
        return mappingName;
    }
    
    public void setMappingName(String mappingName)
    {
        this.mappingName = mappingName;
    }
    
    public String line(String remarks)
    {
        return "\t@" + this.toString() + "(\"" + remarks + "\")" + "\n";
    }
}
