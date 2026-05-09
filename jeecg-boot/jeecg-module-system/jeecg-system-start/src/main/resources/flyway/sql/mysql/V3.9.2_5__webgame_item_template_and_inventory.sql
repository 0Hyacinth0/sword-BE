-- ============================================
-- WebGame 物品模板表创建脚本
-- 表名统一 wg_ 前缀
-- ============================================

-- 1. 创建物品模板表（根据截图结构）
CREATE TABLE IF NOT EXISTS `wg_item_template` (
  `item_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物品模板ID（主键）',
  `name` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物品名称',
  `type` TINYINT NOT NULL COMMENT '物品类型(1-装备,2-消耗品,3-材料)',
  `slot_type` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '装备部位类型(weapon/helmet/chest/legs/accessory)',
  `rarity` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '稀有度(Normal/Rare/Epic/Legendary)',
  `base_stats` JSON DEFAULT NULL COMMENT '基础属性加成(JSON格式)',
  `set_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属套装ID',
  `set_name` VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '套装名称',
  `icon` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图标路径',
  `description` VARCHAR(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '物品描述',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`item_id`) USING BTREE,
  KEY `idx_type` (`type`) USING BTREE COMMENT '物品类型索引',
  KEY `idx_rarity` (`rarity`) USING BTREE COMMENT '稀有度索引',
  KEY `idx_set_id` (`set_id`) USING BTREE COMMENT '套装ID索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='物品模板表';

-- 2. 创建角色背包表（wg_ 前缀统一）
CREATE TABLE IF NOT EXISTS `wg_character_inventory` (
  `id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键ID',
  `character_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色ID',
  `item_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物品模板ID',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT '持有数量（≥1）',
  `obtained_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '获取时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_character_item` (`character_id`, `item_id`) USING BTREE COMMENT '角色+物品唯一索引',
  KEY `idx_character_id` (`character_id`) USING BTREE COMMENT '角色ID索引',
  
  -- 外键约束（与 wg_character 表字符集一致）
  CONSTRAINT `fk_inv_character` FOREIGN KEY (`character_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_inv_item` FOREIGN KEY (`item_id`) REFERENCES `wg_item_template` (`item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色背包表';
