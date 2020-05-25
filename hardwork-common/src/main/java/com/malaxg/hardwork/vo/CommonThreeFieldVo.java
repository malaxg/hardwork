package com.malaxg.hardwork.vo;

/**
 * hardwork
 *
 * @author wangrong
 * @version [版本号, 2019年1月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CommonThreeFieldVo extends CommonTwoFieldVo
{
    private String three;
    
    public CommonThreeFieldVo()
    {
    }
    
    public CommonThreeFieldVo(String three)
    {
        this.three = three;
    }
    
    public CommonThreeFieldVo(String id, String label, String three)
    {
        super(id, label);
        this.three = three;
    }
}
