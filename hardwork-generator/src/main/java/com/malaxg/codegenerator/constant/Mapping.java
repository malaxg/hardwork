package com.malaxg.codegenerator.constant;

/**
 * hardwork
 *
 * @author wangrong
 * @version [版本号, 2019年1月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public enum Mapping {
    GetMapping("@GetMapping"), DeleteMapping("@DeleteMapping"),
    PutMapping("@PutMapping"), PostMapping("@PostMapping");
    private String mappingName;

    Mapping(String mappingName) {
        this.mappingName = mappingName;
    }

    public String getMappingName() {
        return mappingName;
    }
    
    public void setMappingName(String mappingName)
    {
        this.mappingName = mappingName;
    }
    
    public String line()
    {
        return "\t@" + this.toString() + "\n";
    }
    
    public String line(String url)
    {
        return "\t@" + this.toString() + "(\"" + url + "\")" + "\n";
    }
}
