package com.zw.aichat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zw.aichat.bean.DatabaseAi;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DatabaseAiDao extends BaseMapper<DatabaseAi> {

//    DatabaseAi queryById(Long id);
//
//
//    long count(DatabaseAi databaseAi);
//
//    int insert(DatabaseAi databaseAi);
//
//    int insertBatch(@Param("beans") List<DatabaseAi> beans);
//
//    int insertOrUpdateBatch(@Param("beans") List<DatabaseAi> beans);
//
//    int update(DatabaseAi databaseAi);
//
//    int deleteById(Long id);
//
//    List<DatabaseAi> selectByDatabaseAi(DatabaseAi databaseAi);
}
