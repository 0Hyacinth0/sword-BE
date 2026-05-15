-- ============================================
-- WebGame 多人副本战斗 - 多人战斗实例表
-- 用于存储多人副本战斗状态和掉落分配
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_multi_battles` (
  `battle_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '战斗实例ID(UUID)',
  `room_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '副本房间ID',
  `dungeon_id` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '副本ID',
  `floor` INT NOT NULL DEFAULT 1 COMMENT '当前楼层',
  `total_floors` INT NOT NULL DEFAULT 1 COMMENT '总楼层数',
  `status` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'in_progress' COMMENT '战斗状态(in_progress/completed/failed)',
  `monster_scale` DECIMAL(5,2) DEFAULT 1.00 COMMENT '怪物属性缩放倍率',
  `current_round` INT NOT NULL DEFAULT 1 COMMENT '当前回合数',
  `battle_state_json` JSON DEFAULT NULL COMMENT '完整战斗状态快照',
  `start_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '战斗开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '战斗结束时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`battle_id`) USING BTREE,
  KEY `idx_room_id` (`room_id`) USING BTREE COMMENT '房间索引',
  KEY `idx_status` (`status`) USING BTREE COMMENT '状态索引',
  CONSTRAINT `fk_multi_battle_room` FOREIGN KEY (`room_id`) REFERENCES `wg_dungeon_rooms` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='多人战斗实例表';
