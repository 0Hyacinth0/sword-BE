-- ============================================
-- WebGame 聊天系统 - 私聊会话表
-- 用于跟踪私聊会话的最后消息和时间
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_chat_conversations` (
  `id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID(UUID)',
  `user1_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '较小角色ID',
  `user2_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '较大角色ID',
  `last_message` TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '最后一条消息内容',
  `last_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后消息时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_user_pair` (`user1_id`, `user2_id`) USING BTREE COMMENT '用户对唯一索引',
  KEY `idx_user1` (`user1_id`) USING BTREE COMMENT '用户1索引',
  KEY `idx_user2` (`user2_id`) USING BTREE COMMENT '用户2索引',
  CONSTRAINT `fk_conv_user1` FOREIGN KEY (`user1_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_conv_user2` FOREIGN KEY (`user2_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='私聊会话表';
