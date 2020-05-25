package com.malaxg.codegenerator.service;

import java.util.List;

import com.malaxg.codegenerator.domain.MysqlColumnInfo;

/**
 * hardwork
 *
 * @author wangrong
 * @version [版本号, 2019年1月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface MysqlColumnInfoJpaService extends BaseJpaService<MysqlColumnInfo, String>
{
    List<MysqlColumnInfo> findMysqlColumnInfo(String tableName, String dataBase);
}
