-- ============================================
-- WebGame 战斗系统 - 怪物掉落表
-- 用于存储怪物可能掉落的物品配置
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_monster_drops` (
  `drop_id` INT NOT NULL AUTO_INCREMENT COMMENT '掉落配置ID',
  `monster_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '怪物模板ID',
  `item_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物品ID(关联wg_item_template)',
  `min_quantity` INT NOT NULL DEFAULT 1 COMMENT '最小掉落数量',
  `max_quantity` INT NOT NULL DEFAULT 1 COMMENT '最大掉落数量',
  `drop_weight` INT NOT NULL DEFAULT 100 COMMENT '掉落权重(数值越大越容易掉落)',
  `quality` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'common' COMMENT '物品品质(common/rare/epic/legendary)',
  `item_type` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'material' COMMENT '物品类型(equipment/material/consumable/pet_egg)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`drop_id`) USING BTREE,
  KEY `idx_monster_id` (`monster_id`) USING BTREE COMMENT '怪物ID索引',
  KEY `idx_item_id` (`item_id`) USING BTREE COMMENT '物品ID索引',
  CONSTRAINT `fk_drop_monster` FOREIGN KEY (`monster_id`) REFERENCES `wg_monster_template` (`monster_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='怪物掉落配置表';
