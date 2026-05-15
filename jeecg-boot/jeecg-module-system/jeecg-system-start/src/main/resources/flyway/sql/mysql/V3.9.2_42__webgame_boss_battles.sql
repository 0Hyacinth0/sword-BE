-- ============================================
-- WebGame 团队副本Boss战 - Boss战斗状态表
-- 用于存储Boss战斗的实时状态（阶段、狂暴计时等）
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_boss_battles` (
  `battle_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '战斗实例ID(UUID)',
  `multi_battle_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '多人战斗ID',
  `boss_id` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Boss ID',
  `current_phase` INT NOT NULL DEFAULT 1 COMMENT '当前阶段',
  `current_round` INT NOT NULL DEFAULT 1 COMMENT '当前回合数',
  `enrage_triggered` TINYINT(1) DEFAULT 0 COMMENT '是否已触发狂暴',
  `boss_hp_percent` DECIMAL(5,2) DEFAULT 100.00 COMMENT 'Boss当前HP百分比',
  `battle_state_json` JSON DEFAULT NULL COMMENT '完整战斗状态快照',
  `start_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '战斗开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '战斗结束时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`battle_id`) USING BTREE,
  UNIQUE KEY `uk_multi_battle` (`multi_battle_id`) USING BTREE COMMENT '多人战斗唯一索引',
  KEY `idx_boss_id` (`boss_id`) USING BTREE COMMENT 'Boss ID索引',
  CONSTRAINT `fk_boss_battle_multi` FOREIGN KEY (`multi_battle_id`) REFERENCES `wg_multi_battles` (`battle_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Boss战斗状态表';
