package com.malaxg.codegenerator.dao;

import java.util.List;

import com.malaxg.codegenerator.domain.MysqlColumnInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * hardwork
 *
 * @author wangrong
 * @version [版本号, 2019年1月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Repository
public interface MysqlColumnInfoDao extends JpaRepository<MysqlColumnInfo, String>
{
    @Query(value = "select column_name,table_schema,column_type,data_type,column_key,column_comment,column_default "
        + "from information_schema.columns where table_name = ?1 and table_schema = ?2", nativeQuery = true)
    List<MysqlColumnInfo> findMysqlColumnInfo(String tableName, String dataBase);
}
