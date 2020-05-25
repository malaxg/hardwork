package com.malaxg.codegenerator.service;

import java.util.List;

import com.malaxg.codegenerator.dao.MysqlColumnInfoDao;
import com.malaxg.codegenerator.domain.MysqlColumnInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * hardwork
 *
 * @author wangrong
 * @version [版本号, 2019年1月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Service
public class MysqlColumnInfoJpaServiceImpl implements MysqlColumnInfoJpaService
{
    @Autowired MysqlColumnInfoDao mysqlColumnInfoDao;
    
    @Override public List<MysqlColumnInfo> findMysqlColumnInfo(String tableName, String dataBase)
    {
        return mysqlColumnInfoDao.findMysqlColumnInfo(tableName, dataBase);
    }
    
    @Override
    public MysqlColumnInfo findOne(String s)
    {
        return mysqlColumnInfoDao.findById(s).get();
    }
    
    @Override
    public List<MysqlColumnInfo> findAll()
    {
        return mysqlColumnInfoDao.findAll();
    }
    
    @Override
    public void deleteById(String s)
    {
        mysqlColumnInfoDao.deleteById(s);
    }
    
    @Override
    public List<MysqlColumnInfo> save(List<MysqlColumnInfo> entities)
    {
        return null;
    }
    
}
