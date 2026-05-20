-- ============================================
-- WebGame 玩家竞技场数据表
-- 用于存储玩家在竞技场中的段位、积分和战绩
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_arena_player` (
  `id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID(UUID)',
  `character_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色ID',
  `season_id` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '赛季ID',
  
  -- 段位信息
  `tier` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'bronze' COMMENT '段位(bronze/silver/gold/platinum/diamond/master)',
  `sub_tier` VARCHAR(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'I' COMMENT '小级(I/II/III)',
  `score` INT NOT NULL DEFAULT 0 COMMENT '当前积分',
  
  -- 战绩统计
  `wins` INT NOT NULL DEFAULT 0 COMMENT '胜利场次',
  `losses` INT NOT NULL DEFAULT 0 COMMENT '失败场次',
  `draws` INT NOT NULL DEFAULT 0 COMMENT '平局场次',
  
  -- 时间戳
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_character_season` (`character_id`, `season_id`) USING BTREE COMMENT '角色赛季唯一索引',
  KEY `idx_character_id` (`character_id`) USING BTREE COMMENT '角色ID索引',
  KEY `idx_season_id` (`season_id`) USING BTREE COMMENT '赛季ID索引',
  KEY `idx_score` (`score`) USING BTREE COMMENT '积分索引(用于排行榜)',
  CONSTRAINT `fk_arena_player_character` FOREIGN KEY (`character_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='玩家竞技场数据表';
