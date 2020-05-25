package com.malaxg.hardwork.filter;

import java.util.List;

/**
 * FilterUserInput
 *
 * @author wangrong
 * @version [版本号, 2019年2月20日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class FilterUserInput
{
    private FilterCondition filterCondition;
    private List<FilterCriteria> filterCriteria;

    public FilterCondition getFilterCondition()
    {
        return filterCondition;
    }

    public void setFilterCondition(FilterCondition filterCondition)
    {
        this.filterCondition = filterCondition;
    }

    public List<FilterCriteria> getFilterCriteria()
    {
        return filterCriteria;
    }

    public void setFilterCriteria(List<FilterCriteria> filterCriteria)
    {
        this.filterCriteria = filterCriteria;
    }

}
