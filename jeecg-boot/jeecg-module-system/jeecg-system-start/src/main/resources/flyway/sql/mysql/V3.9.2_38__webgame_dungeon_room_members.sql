-- ============================================
-- WebGame 多人副本大厅 - 副本房间成员表
-- 用于存储副本房间成员的准备状态
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_dungeon_room_members` (
  `id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID(UUID)',
  `room_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房间ID',
  `character_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '成员角色ID',
  `ready_status` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'not_ready' COMMENT '准备状态(not_ready/ready)',
  `joined_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_character` (`character_id`) USING BTREE COMMENT '角色唯一索引',
  KEY `idx_room_id` (`room_id`) USING BTREE COMMENT '房间索引',
  CONSTRAINT `fk_room_member_room` FOREIGN KEY (`room_id`) REFERENCES `wg_dungeon_rooms` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_room_member_character` FOREIGN KEY (`character_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='副本房间成员表';
