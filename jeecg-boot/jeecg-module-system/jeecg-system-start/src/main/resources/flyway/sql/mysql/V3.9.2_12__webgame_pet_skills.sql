-- ============================================
-- WebGame 战宠系统 - 战宠技能配置表
-- 用于存储所有战宠技能的配置信息
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_pet_skills` (
  `skill_id` INT NOT NULL AUTO_INCREMENT COMMENT '技能ID',
  `name` VARCHAR(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '技能名称',
  `type` VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '技能类型(active_attack/active_support/passive)',
  `power` INT DEFAULT NULL COMMENT '技能威力(百分比,如120表示120%)',
  `cooldown` INT DEFAULT NULL COMMENT '冷却时间(回合数)',
  `learn_level` INT NOT NULL DEFAULT 1 COMMENT '学习等级(0表示进化专属技能)',
  `description` VARCHAR(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '技能描述',
  `icon_url` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '技能图标URL',
  `pet_type_ids` JSON DEFAULT NULL COMMENT '可学习该技能的战宠类型ID列表(JSON数组)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`skill_id`) USING BTREE,
  KEY `idx_type` (`type`) USING BTREE COMMENT '技能类型索引',
  KEY `idx_learn_level` (`learn_level`) USING BTREE COMMENT '学习等级索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='战宠技能配置表';
