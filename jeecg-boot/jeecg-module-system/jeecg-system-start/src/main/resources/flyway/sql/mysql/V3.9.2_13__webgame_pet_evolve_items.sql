-- ============================================
-- WebGame 战宠系统 - 进化材料配置表
-- 用于存储战宠进化所需的材料配置
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_pet_evolve_items` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `pet_type_id` INT NOT NULL COMMENT '战宠类型ID(需要进化的战宠)',
  `item_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所需物品ID(关联wg_item_template)',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT '所需数量',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_pet_type_id` (`pet_type_id`) USING BTREE COMMENT '战宠类型索引',
  KEY `idx_item_id` (`item_id`) USING BTREE COMMENT '物品ID索引',
  CONSTRAINT `fk_evolve_pet_type` FOREIGN KEY (`pet_type_id`) REFERENCES `wg_pet_types` (`pet_type_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='战宠进化材料配置表';
