-- ============================================
-- WebGame 竞技场赛季表
-- 用于存储PVP赛季信息
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_arena_season` (
  `id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID(UUID)',
  `season_id` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '赛季唯一标识',
  `season_name` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '赛季名称',
  `season_number` INT NOT NULL COMMENT '赛季编号',
  `start_date` DATETIME NOT NULL COMMENT '赛季开始时间',
  `end_date` DATETIME NOT NULL COMMENT '赛季结束时间',
  `is_active` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否为当前活跃赛季',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_season_id` (`season_id`) USING BTREE COMMENT '赛季ID唯一索引',
  KEY `idx_is_active` (`is_active`) USING BTREE COMMENT '活跃状态索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='竞技场赛季表';

-- 插入初始赛季数据
INSERT INTO `wg_arena_season` (`id`, `season_id`, `season_name`, `season_number`, `start_date`, `end_date`, `is_active`) 
VALUES 
('season-1-id', 'season-1', '初始赛季', 1, '2026-01-01 00:00:00', '2026-03-01 00:00:00', 0),
('season-2-id', 'season-2', '冰霜纪元', 2, '2026-03-01 00:00:00', '2026-04-15 00:00:00', 0),
('season-3-id', 'season-3', '龙焰纪元', 3, '2026-04-15 00:00:00', '2026-06-15 00:00:00', 1);
