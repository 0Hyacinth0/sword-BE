-- ============================================
-- WebGame 角色表添加头像字段
-- 执行此脚本将为 wg_character 表添加头像相关字段
-- ============================================

-- 添加头像字段
ALTER TABLE `wg_character` 
ADD COLUMN `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色头像URL（小图，用于角色列表卡片）' AFTER `bonus_defense`,
ADD COLUMN `portrait_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色立绘URL（大图，用于角色详情面板）' AFTER `avatar_url`;

