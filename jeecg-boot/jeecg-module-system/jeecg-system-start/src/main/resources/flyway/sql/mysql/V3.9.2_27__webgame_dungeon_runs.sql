-- 副本运行记录系统 - 副本运行状态表
-- 版本: V3.9.2_27__webgame_dungeon_runs.sql
-- 描述: 创建副本运行记录表，用于跟踪玩家的副本进度

-- ========================================
-- 1. 副本运行记录表 (wg_dungeon_runs)
-- ========================================
CREATE TABLE IF NOT EXISTS `wg_dungeon_runs` (
  `id` VARCHAR(36) PRIMARY KEY COMMENT '运行ID（UUID）',
  `user_id` VARCHAR(50) NOT NULL COMMENT '用户名（关联wg_character.user_id）',
  `character_id` VARCHAR(36) NOT NULL COMMENT '角色ID',
  `dungeon_id` VARCHAR(64) NOT NULL COMMENT '副本ID',
  `current_floor` INTEGER NOT NULL DEFAULT 1 COMMENT '当前楼层',
  `status` VARCHAR(20) NOT NULL COMMENT '状态：active/complete/retreated/defeated',
  `accumulated_exp` INTEGER DEFAULT 0 COMMENT '累积经验值',
  `accumulated_gold` INTEGER DEFAULT 0 COMMENT '累积金币',
  `accumulated_items` JSON DEFAULT NULL COMMENT '累积物品JSON数组',
  `floor_history` JSON DEFAULT NULL COMMENT '楼层历史JSON数组',
  `player_hp_percent` INTEGER DEFAULT 100 COMMENT '玩家HP百分比',
  `player_mp_percent` INTEGER DEFAULT 100 COMMENT '玩家MP百分比',
  `started_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `ended_at` TIMESTAMP NULL DEFAULT NULL COMMENT '结束时间',
  FOREIGN KEY (`character_id`) REFERENCES `wg_character`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`dungeon_id`) REFERENCES `wg_dungeon_configs`(`id`) ON DELETE CASCADE,
  INDEX idx_user_id (`user_id`),
  INDEX idx_character_id (`character_id`),
  INDEX idx_status (`status`),
  INDEX idx_started_at (`started_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='副本运行记录表';
