-- ============================================
-- WebGame 聊天系统 - 聊天消息表
-- 用于存储世界频道和私聊消息
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_chat_messages` (
  `id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID(UUID)',
  `sender_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送者角色ID',
  `channel` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '频道类型(world/private)',
  `target_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '私聊目标ID（仅私聊时有值）',
  `content` TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息内容（最大500字）',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_channel_created` (`channel`, `created_at`) USING BTREE COMMENT '世界频道查询索引',
  KEY `idx_sender_target_created` (`sender_id`, `target_id`, `created_at`) USING BTREE COMMENT '私聊查询索引',
  KEY `idx_target_created` (`target_id`, `created_at`) USING BTREE COMMENT '接收者消息查询索引',
  CONSTRAINT `fk_chat_sender` FOREIGN KEY (`sender_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_chat_target` FOREIGN KEY (`target_id`) REFERENCES `wg_character` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='聊天消息表';
