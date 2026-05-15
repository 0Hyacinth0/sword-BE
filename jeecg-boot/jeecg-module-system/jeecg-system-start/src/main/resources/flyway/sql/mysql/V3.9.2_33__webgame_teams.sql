-- ============================================
-- WebGame 组队系统 - 队伍表
-- 用于存储队伍基本信息
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_teams` (
  `id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID(UUID)',
  `leader_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '队长角色ID',
  `max_members` INT NOT NULL DEFAULT 4 COMMENT '最大人数（默认4）',
  `status` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'open' COMMENT '队伍状态(open/closed/in_dungeon)',
  `target_dungeon` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '目标副本标识',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_leader_id` (`leader_id`) USING BTREE COMMENT '队长索引',
  KEY `idx_status` (`status`) USING BTREE COMMENT '状态索引（查询公开队伍）',
  CONSTRAINT `fk_team_leader` FOREIGN KEY (`leader_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='队伍表';
