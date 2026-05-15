-- ============================================
-- WebGame 组队系统 - 入队申请表
-- 用于存储玩家的入队申请记录
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_team_applications` (
  `id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID(UUID)',
  `team_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '队伍ID',
  `applicant_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请者角色ID',
  `status` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'pending' COMMENT '申请状态(pending/accepted/rejected)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_team_applicant` (`team_id`, `applicant_id`) USING BTREE COMMENT '队伍-申请者唯一索引',
  KEY `idx_team_id` (`team_id`) USING BTREE COMMENT '队伍索引（查询队伍的申请列表）',
  KEY `idx_applicant_id` (`applicant_id`) USING BTREE COMMENT '申请者索引',
  KEY `idx_status` (`status`) USING BTREE COMMENT '状态索引',
  CONSTRAINT `fk_application_team` FOREIGN KEY (`team_id`) REFERENCES `wg_teams` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_application_applicant` FOREIGN KEY (`applicant_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='入队申请表';
