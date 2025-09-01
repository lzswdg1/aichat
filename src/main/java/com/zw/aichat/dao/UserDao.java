package com.zw.aichat.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zw.aichat.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//User表数据库访问层
@Mapper
public interface UserDao extends BaseMapper<User> {

//    User queryById(Long id);
//
//
//    List<User> queryAllByLimit(User user);
//
//    Long count(User user);
//
//    int insert(User user);
//
//    /**
//     * 批量新增数据（MyBatis原生foreach方法）
//     *
//     * @param
//     * @return 影响行数
//     */
//    int insertBatch(@Param("beans") List<User> beans);
//    /**
//     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
//     *
//     * @param
//     * @return 影响行数
//     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
//     */
//    int insertOrUpdateBatch(@Param("beans") List<User> beans);
//
//    int update(User user);
//
//    int deleteById(Long id);
//


}
