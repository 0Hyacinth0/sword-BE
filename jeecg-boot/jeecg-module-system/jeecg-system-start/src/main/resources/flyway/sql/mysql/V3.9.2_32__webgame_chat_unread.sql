-- ============================================
-- WebGame 聊天系统 - 未读消息计数表
-- 用于跟踪每个会话中每个角色的未读消息数
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_chat_unread` (
  `id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID(UUID)',
  `conversation_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会话ID',
  `character_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色ID',
  `unread_count` INT NOT NULL DEFAULT 0 COMMENT '未读消息数量',
  
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_conversation_character` (`conversation_id`, `character_id`) USING BTREE COMMENT '会话-角色唯一索引',
  KEY `idx_character` (`character_id`) USING BTREE COMMENT '角色索引（查询用户的未读消息）',
  CONSTRAINT `fk_unread_conversation` FOREIGN KEY (`conversation_id`) REFERENCES `wg_chat_conversations` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_unread_character` FOREIGN KEY (`character_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='未读消息计数表';
