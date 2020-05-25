package com.malaxg.hardwork.filter;

/**
 * 过滤接口条件
 */
public class FilterCondition
{
    /**
     * 排序字段
     */
    private String sortField;
    /**
     * 是否升序，默认升序
     */
    private Boolean isAsc = false;
    
    /**
     * 分页长度，默认10条
     */
    private Integer pageSize = 10;
    /**
     * 当前页码，默认第一页
     */
    private Integer page = 1;
    
    /**
     * 模糊匹配的字段
     */
    private String pattern;
    
    public String getSortField()
    {
        return sortField;
    }
    
    public void setSortField(String sortField)
    {
        this.sortField = sortField;
    }
    
    public Boolean getIsAsc()
    {
        return isAsc;
    }
    
    public void setIsAsc(Boolean isAsc)
    {
        this.isAsc = isAsc;
    }
    
    public Integer getPageSize()
    {
        return pageSize;
    }
    
    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }
    
    public Integer getPage()
    {
        return page;
    }
    
    public void setPage(Integer page)
    {
        this.page = page;
    }
    
    public String getPattern()
    {
        return pattern;
    }
    
    public void setPattern(String pattern)
    {
        this.pattern = pattern;
    }

}
