package com.malaxg.hardwork.service;

import java.util.List;

/**
 * BaseJpaService
 *
 * @author wangrong
 * @version [版本号, 2019年1月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface BaseJpaService<T, ID>
{
    /**
     * findOne
     *
     * @param id id
     * @return T [返回类型说明]
     * @see [类、类#方法、类#成员]
     */
    T findOne(ID id);
    
    /**
     * findAll
     *
     * @return List<T> [返回类型说明]
     * @see [类、类#方法、类#成员]
     */
    List<T> findAll();
    
    /**
     * findOne
     *
     * @param id id
     * @see [类、类#方法、类#成员]
     */
    void deleteById(ID id);
    
    /**
     * save
     *
     * @param entities entities
     * @return List<T> [返回类型说明]
     * @see [类、类#方法、类#成员]
     */
    List<T> save(List<T> entities);
}
