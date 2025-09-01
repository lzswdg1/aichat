//package com.zw.aichat.service.impl;
//
//import com.zw.aichat.bean.DatabaseAi;
//import com.zw.aichat.dao.DatabaseAiDao;
//import com.zw.aichat.service.DatabaseAiService;
//import org.springframework.stereotype.Service;
//
//import jakarta.annotation.Resource;
//import java.util.List;
//
//@Service("databaseAiService")
//public class DatabaseAiServiceImpl implements DatabaseAiService {
//    @Resource
//    private DatabaseAiDao databaseAiDao;
//
//    @Override
//    public DatabaseAi queryById(Long id) { return this.databaseAiDao.queryById(id);}
//
//    @Override
//    public int insert(DatabaseAi databaseAi) {
//        return this.databaseAiDao.insert(databaseAi);
//    }
//    @Override
//    public boolean deleteById(Long id) {
//        return this.databaseAiDao.deleteById(id) > 0;
//    }
//
//    @Override
//    public int update(DatabaseAi databaseAi) {
//        return this.databaseAiDao.update(databaseAi);
//    }
//
//    @Override
//    public List<DatabaseAi> selectByDatabaseAi(DatabaseAi databaseAi) {
//        return  this.databaseAiDao.selectByDatabaseAi(databaseAi);
//    }
//
//}
package com.zw.aichat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zw.aichat.bean.DatabaseAi;
import com.zw.aichat.dao.DatabaseAiDao;
import com.zw.aichat.service.DatabaseAiService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service("databaseAiService")
public class DatabaseAiServiceImpl implements DatabaseAiService {
    @Resource
    private DatabaseAiDao databaseAiDao;

    /**
     * 【已修改】使用 BaseMapper 的 selectById 方法
     */
    @Override
    public DatabaseAi queryById(Long id) {
        return this.databaseAiDao.selectById(id);
    }

    /**
     * 【无需修改】insert 方法名和 BaseMapper 中的一致
     */
    @Override
    public int insert(DatabaseAi databaseAi) {
        return this.databaseAiDao.insert(databaseAi);
    }

    /**
     * 【已修改】使用 BaseMapper 的 updateById 方法
     */
    @Override
    public int update(DatabaseAi databaseAi) {
        return this.databaseAiDao.updateById(databaseAi);
    }

    /**
     * 【已修改】BaseMapper 的 deleteById 返回的也是影响行数
     */
    @Override
    public boolean deleteById(Long id) {
        return this.databaseAiDao.deleteById(id) > 0;
    }

    /**
     * 【已修改】使用 Wrapper 构建动态查询条件
     */
    @Override
    public List<DatabaseAi> selectByDatabaseAi(DatabaseAi databaseAi) {
        // 1. 创建 Wrapper 对象
        QueryWrapper<DatabaseAi> wrapper = new QueryWrapper<>();

        // 2. 根据传入对象的非空属性，动态添加查询条件
        // wrapper.eq(条件, "数据库字段名", 值)
        // 第一个参数是布尔条件，如果为 true，则该查询条件生效
        wrapper.eq(databaseAi.getId() != null, "id", databaseAi.getId());
        wrapper.eq(databaseAi.getUserId() != null, "user_id", databaseAi.getUserId());
        wrapper.eq(databaseAi.getAiApiKey() != null && !databaseAi.getAiApiKey().isEmpty(), "ai_api_key", databaseAi.getAiApiKey());
        wrapper.eq(databaseAi.getAiApiUrl() != null && !databaseAi.getAiApiUrl().isEmpty(), "ai_api_url", databaseAi.getAiApiUrl());
        wrapper.eq(databaseAi.getAiApiModel() != null && !databaseAi.getAiApiModel().isEmpty(), "ai_api_model", databaseAi.getAiApiModel());

        // 3. 执行查询
        return this.databaseAiDao.selectList(wrapper);
    }
}