-- ============================================
-- WebGame 多人副本战斗 - 楼层掉落记录表
-- 用于存储每个玩家在每层楼的掉落物品
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_floor_drops` (
  `id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID(UUID)',
  `battle_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '战斗实例ID',
  `character_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色ID',
  `floor` INT NOT NULL COMMENT '楼层',
  `item_id` INT NOT NULL COMMENT '物品ID',
  `item_name` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物品名称',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT '数量',
  `quality` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'common' COMMENT '品质(common/rare/epic/legendary)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '掉落时间',
  
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_battle_character` (`battle_id`, `character_id`) USING BTREE COMMENT '战斗-角色索引',
  KEY `idx_character` (`character_id`) USING BTREE COMMENT '角色索引（查询玩家的掉落历史）',
  CONSTRAINT `fk_floor_drop_battle` FOREIGN KEY (`battle_id`) REFERENCES `wg_multi_battles` (`battle_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_floor_drop_character` FOREIGN KEY (`character_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='楼层掉落记录表';
