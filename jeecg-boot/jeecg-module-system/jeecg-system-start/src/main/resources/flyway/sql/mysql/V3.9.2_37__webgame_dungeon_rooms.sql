-- ============================================
-- WebGame 多人副本大厅 - 副本房间表
-- 用于存储副本挑战房间信息
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_dungeon_rooms` (
  `id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID(UUID)',
  `team_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联队伍ID',
  `dungeon_id` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '副本配置ID',
  `leader_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '队长角色ID',
  `status` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'waiting' COMMENT '房间状态(waiting/ready/starting/in_progress/completed/cancelled)',
  `monster_scale` DECIMAL(5,2) DEFAULT 1.00 COMMENT '怪物缩放倍率',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_team_id` (`team_id`) USING BTREE COMMENT '队伍唯一索引（一个队伍只能有一个房间）',
  KEY `idx_leader_id` (`leader_id`) USING BTREE COMMENT '队长索引',
  KEY `idx_status` (`status`) USING BTREE COMMENT '状态索引',
  CONSTRAINT `fk_room_team` FOREIGN KEY (`team_id`) REFERENCES `wg_teams` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_room_leader` FOREIGN KEY (`leader_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='副本房间表';
