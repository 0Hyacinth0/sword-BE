-- ============================================
-- WebGame 好友系统 - 好友关系表
-- 用于存储玩家之间的好友关系
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_friendships` (
  `id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID(UUID)',
  `character_id_1` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色1 ID（较小的ID）',
  `character_id_2` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色2 ID（较大的ID）',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '建立好友时间',
  
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_character_pair` (`character_id_1`, `character_id_2`) USING BTREE COMMENT '好友关系唯一索引',
  KEY `idx_character_1` (`character_id_1`) USING BTREE COMMENT '角色1索引',
  KEY `idx_character_2` (`character_id_2`) USING BTREE COMMENT '角色2索引',
  CONSTRAINT `fk_friendship_char1` FOREIGN KEY (`character_id_1`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_friendship_char2` FOREIGN KEY (`character_id_2`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='好友关系表';
