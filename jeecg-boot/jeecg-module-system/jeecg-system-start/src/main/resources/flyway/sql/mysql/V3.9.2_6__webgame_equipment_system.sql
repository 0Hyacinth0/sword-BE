-- ============================================
-- WebGame 装备系统表结构
-- 创建装备模板表和角色装备表
-- ============================================

-- 1. 装备模板表
CREATE TABLE IF NOT EXISTS `wg_equipment_template` (
  `equipment_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '装备模板ID',
  `name` VARCHAR(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '装备名称',
  `slot_type` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '槽位类型(weapon/helmet/chest/legs/accessory1/accessory2)',
  `rarity` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '稀有度(Normal/Rare/Epic/Legendary)',
  `base_stats` JSON DEFAULT NULL COMMENT '基础属性加成(JSON格式)',
  `set_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '套装ID',
  `set_name` VARCHAR(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '套装名称',
  `description` VARCHAR(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '装备描述',
  `icon_url` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图标URL',
  `level_requirement` INT DEFAULT 1 COMMENT '等级需求',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`equipment_id`) USING BTREE,
  KEY `idx_slot_type` (`slot_type`) USING BTREE COMMENT '槽位类型索引',
  KEY `idx_rarity` (`rarity`) USING BTREE COMMENT '稀有度索引',
  KEY `idx_set_id` (`set_id`) USING BTREE COMMENT '套装ID索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='装备模板表';

-- 2. 角色装备表
CREATE TABLE IF NOT EXISTS `wg_character_equipments` (
  `id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '装备实例唯一标识',
  `character_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色ID',
  `item_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '装备模板ID',
  `slot_type` VARCHAR(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '槽位类型',
  `extra_stats` JSON DEFAULT NULL COMMENT '随机词条（额外属性）',
  `enhance_level` INT DEFAULT 0 COMMENT '强化等级',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_character_slot` (`character_id`, `slot_type`) USING BTREE COMMENT '角色+槽位唯一索引',
  KEY `idx_character_id` (`character_id`) USING BTREE COMMENT '角色ID索引',
  KEY `idx_item_id` (`item_id`) USING BTREE COMMENT '装备模板ID索引',
  CONSTRAINT `fk_equip_character` FOREIGN KEY (`character_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_equip_item` FOREIGN KEY (`item_id`) REFERENCES `wg_item_template` (`item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色装备表';
