-- ============================================
-- WebGame 团队副本Boss战 - Boss配置表
-- 用于存储Boss的阶段、狂暴、复活等配置
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_boss_configs` (
  `id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID(UUID)',
  `boss_id` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Boss唯一标识',
  `boss_name` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Boss名称',
  `phases_json` JSON NOT NULL COMMENT '阶段配置JSON数组',
  `enrage_json` JSON DEFAULT NULL COMMENT '狂暴配置JSON',
  `revive_json` JSON DEFAULT NULL COMMENT '复活配置JSON',
  `aoe_skill_ids` JSON DEFAULT NULL COMMENT 'AOE技能ID列表',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_boss_id` (`boss_id`) USING BTREE COMMENT 'Boss ID唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Boss配置表';
