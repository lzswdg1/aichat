-- 创建数据库
CREATE DATABASE IF NOT EXISTS deepai CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 切换到deepai数据库
USE deepai;

-- 创建user表
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `manager` varchar(1) NOT NULL DEFAULT '0' COMMENT '是否管理员',
  `password` varchar(255) NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

-- 创建database_ai表
CREATE TABLE IF NOT EXISTS `database_ai` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ai_api_key` varchar(255) NOT NULL COMMENT 'AIKey值',
  `ai_api_url` varchar(255) NOT NULL COMMENT 'AIURL值',
  `ai_api_model` varchar(255) NOT NULL COMMENT '模型',
  `user_id` bigint(20) NOT NULL COMMENT '所属用户id',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_database_ai_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='模型配置表';
