-- ============================================
-- WebGame 物品系统表结构
-- 创建物品模板表和角色背包表
-- ============================================

-- 1. 物品模板表
CREATE TABLE IF NOT EXISTS `wg_item_template` (
  `item_id` INT NOT NULL COMMENT '物品模板ID',
  `item_name` VARCHAR(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '物品名称',
  `category` VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '物品分类(consumable/material/equipment)',
  `rarity` VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'Normal' COMMENT '稀有度(Normal/Rare/Epic/Legendary)',
  `description` VARCHAR(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '物品描述',
  `icon_url` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图标URL',
  `max_stack` INT NOT NULL DEFAULT 1 COMMENT '最大堆叠数量',
  `sell_price` INT NOT NULL DEFAULT 0 COMMENT '出售价格',
  
  -- 消耗品特有字段
  `effect_type` VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '效果类型(heal_hp/heal_mp/add_exp/revive)',
  `effect_value` INT DEFAULT NULL COMMENT '效果数值',
  
  -- 材料特有字段
  `source` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '获取途径描述',
  `usage` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用途描述',
  
  -- 装备特有字段（预留）
  `item_type` INT DEFAULT NULL COMMENT '物品类型(1-装备,2-消耗品,3-材料)',
  `equip_slot` INT DEFAULT NULL COMMENT '装备部位(1-武器,2-头部,3-胸部,4-腿部,5-饰品)',
  `quality` INT DEFAULT NULL COMMENT '品质(1-普通白色,2-稀有蓝色,3-史诗紫色,4-传说橙色)',
  `base_stat_percent` DOUBLE DEFAULT NULL COMMENT '基础属性加成百分比',
  `affix_count` INT DEFAULT NULL COMMENT '附加词条数',
  `has_special_passive` INT DEFAULT NULL COMMENT '是否特殊被动',
  
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`item_id`) USING BTREE,
  KEY `idx_category` (`category`) USING BTREE COMMENT '分类索引',
  KEY `idx_rarity` (`rarity`) USING BTREE COMMENT '稀有度索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='物品模板表';

-- 2. 角色背包表
CREATE TABLE IF NOT EXISTS `character_inventory` (
  `id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `character_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色ID',
  `item_id` INT NOT NULL COMMENT '物品模板ID',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT '持有数量（≥1）',
  `obtained_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '获取时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_character_item` (`character_id`, `item_id`) USING BTREE COMMENT '角色+物品唯一索引',
  KEY `idx_character_id` (`character_id`) USING BTREE COMMENT '角色ID索引',
  CONSTRAINT `fk_inventory_character` FOREIGN KEY (`character_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_inventory_item` FOREIGN KEY (`item_id`) REFERENCES `wg_item_template` (`item_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色背包表';

