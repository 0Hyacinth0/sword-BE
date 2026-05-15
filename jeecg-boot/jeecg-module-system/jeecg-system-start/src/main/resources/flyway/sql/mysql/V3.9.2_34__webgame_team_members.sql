-- ============================================
-- WebGame 组队系统 - 队伍成员表
-- 用于存储队伍成员关系
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_team_members` (
  `id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID(UUID)',
  `team_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '队伍ID',
  `character_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '成员角色ID',
  `role` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'member' COMMENT '成员角色(leader/member)',
  `joined_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_character` (`character_id`) USING BTREE COMMENT '角色唯一索引（一个角色只能在一个队伍中）',
  KEY `idx_team_id` (`team_id`) USING BTREE COMMENT '队伍索引',
  CONSTRAINT `fk_member_team` FOREIGN KEY (`team_id`) REFERENCES `wg_teams` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_member_character` FOREIGN KEY (`character_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='队伍成员表';
