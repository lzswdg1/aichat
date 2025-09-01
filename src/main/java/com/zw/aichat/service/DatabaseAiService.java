package com.zw.aichat.service;

import com.zw.aichat.bean.DatabaseAi;

import java.util.List;

public interface DatabaseAiService {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    DatabaseAi queryById(Long id);

    /**
     * 新增数据
     *
     * @param databaseAi 实例对象
     * @return 实例对象
     */
    int insert(DatabaseAi databaseAi);

    /**
     * 修改数据
     *
     * @param databaseAi 实例对象
     * @return 实例对象
     */
    int update(DatabaseAi databaseAi);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    /**
     * 根据信息查询所拥有的模型
     * @param databaseAi
     * @return
     */
    List<DatabaseAi> selectByDatabaseAi(DatabaseAi databaseAi);

}
