-- ============================================
-- WebGame 好友系统 - 好友请求表
-- 用于存储玩家之间的好友申请记录
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_friend_requests` (
  `id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID(UUID)',
  `from_character_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '发送者角色ID',
  `to_character_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接收者角色ID',
  `status` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'pending' COMMENT '请求状态(pending/accepted/rejected)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_from_to` (`from_character_id`, `to_character_id`) USING BTREE COMMENT '发送者-接收者唯一索引',
  KEY `idx_to_character` (`to_character_id`) USING BTREE COMMENT '接收者索引（查询待处理请求）',
  KEY `idx_from_character` (`from_character_id`) USING BTREE COMMENT '发送者索引（查询已发送请求）',
  KEY `idx_status` (`status`) USING BTREE COMMENT '状态索引',
  CONSTRAINT `fk_request_from` FOREIGN KEY (`from_character_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_request_to` FOREIGN KEY (`to_character_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='好友请求表';
