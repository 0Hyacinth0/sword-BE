-- ============================================
-- WebGame 组队系统 - 队伍邀请表
-- 用于存储队长对玩家的邀请记录
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_team_invitations` (
  `id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID(UUID)',
  `team_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '队伍ID',
  `inviter_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邀请者角色ID',
  `invitee_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '被邀请者角色ID',
  `status` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'pending' COMMENT '邀请状态(pending/accepted/rejected)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_team_invitee` (`team_id`, `invitee_id`) USING BTREE COMMENT '队伍-被邀请者唯一索引',
  KEY `idx_invitee_id` (`invitee_id`) USING BTREE COMMENT '被邀请者索引（查询收到的邀请）',
  CONSTRAINT `fk_invitation_team` FOREIGN KEY (`team_id`) REFERENCES `wg_teams` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_invitation_inviter` FOREIGN KEY (`inviter_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_invitation_invitee` FOREIGN KEY (`invitee_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='队伍邀请表';
