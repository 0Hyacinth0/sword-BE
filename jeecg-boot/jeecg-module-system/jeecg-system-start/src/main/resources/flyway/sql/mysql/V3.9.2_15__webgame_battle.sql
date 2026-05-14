-- ============================================
-- WebGame 战斗系统 - 战斗实例表
-- 用于存储进行中的战斗状态数据
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_battle` (
  `battle_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '战斗实例唯一ID(UUID)',
  `character_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色ID',
  `active_pet_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '出战战宠ID(可选)',
  `dungeon_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '副本ID(可选)',
  `stage_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '关卡ID(可选)',
  `enemy_group_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '怪物组ID(可选)',
  
  -- 战斗状态
  `status` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'in_progress' COMMENT '战斗状态(in_progress/completed/abandoned)',
  `outcome` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '战斗结果(victory/defeat/fled)',
  `current_round` INT NOT NULL DEFAULT 1 COMMENT '当前回合数',
  `battle_phase` VARCHAR(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'ROUND_START' COMMENT '战斗阶段',
  
  -- 战斗统计数据
  `total_damage_dealt` INT DEFAULT 0 COMMENT '玩家造成的总伤害',
  `total_damage_taken` INT DEFAULT 0 COMMENT '玩家承受的总伤害',
  `total_healed` INT DEFAULT 0 COMMENT '玩家治疗总量',
  `critical_hits` INT DEFAULT 0 COMMENT '暴击次数',
  `dodge_count` INT DEFAULT 0 COMMENT '闪避次数',
  `enemies_killed` INT DEFAULT 0 COMMENT '击杀敌人数',
  
  -- 战斗状态快照(JSON格式，存储完整战斗状态)
  `battle_state_json` JSON DEFAULT NULL COMMENT '完整战斗状态快照',
  
  -- 时间戳
  `start_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '战斗开始时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '战斗结束时间',
  `last_action_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后行动时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`battle_id`) USING BTREE,
  KEY `idx_character_id` (`character_id`) USING BTREE COMMENT '角色ID索引',
  KEY `idx_status` (`status`) USING BTREE COMMENT '战斗状态索引',
  KEY `idx_start_time` (`start_time`) USING BTREE COMMENT '开始时间索引',
  CONSTRAINT `fk_battle_character` FOREIGN KEY (`character_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='战斗实例表';
