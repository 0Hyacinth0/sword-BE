-- ============================================
-- WebGame 团队副本Boss战 - 复活记录表
-- 用于存储队友复活记录和次数限制
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_boss_revives` (
  `id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID(UUID)',
  `battle_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Boss战斗ID',
  `reviver_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '复活者角色ID',
  `target_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '被复活者角色ID',
  `revive_count` INT NOT NULL DEFAULT 1 COMMENT '该目标已被复活次数',
  `hp_restored` INT NOT NULL COMMENT '恢复的HP值',
  `mp_cost` INT NOT NULL COMMENT '消耗的MP值',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '复活时间',
  
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_battle_target` (`battle_id`, `target_id`) USING BTREE COMMENT '战斗-目标索引（查询目标的复活次数）',
  KEY `idx_reviver` (`reviver_id`) USING BTREE COMMENT '复活者索引',
  CONSTRAINT `fk_revive_battle` FOREIGN KEY (`battle_id`) REFERENCES `wg_boss_battles` (`battle_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_revive_reviver` FOREIGN KEY (`reviver_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_revive_target` FOREIGN KEY (`target_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='复活记录表';
