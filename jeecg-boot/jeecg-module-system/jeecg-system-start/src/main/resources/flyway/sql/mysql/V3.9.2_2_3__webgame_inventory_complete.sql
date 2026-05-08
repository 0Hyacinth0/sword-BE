-- ============================================
-- WebGame 物品系统 - 完整脚本
-- 包含表结构创建和数据初始化
-- ============================================

-- ==================== 第一部分：创建表结构 ====================

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


-- ==================== 第二部分：初始化物品数据 ====================

-- 消耗品 - 生命药水
INSERT INTO `wg_item_template` (`item_id`, `item_name`, `category`, `rarity`, `description`, `max_stack`, `sell_price`, `effect_type`, `effect_value`) 
VALUES 
(1001, '小型生命药水', 'consumable', 'Normal', '恢复 50 点生命值。', 99, 10, 'heal_hp', 50),
(1002, '中型生命药水', 'consumable', 'Rare', '恢复 150 点生命值。', 99, 30, 'heal_hp', 150),
(1003, '大型生命药水', 'consumable', 'Epic', '恢复 400 点生命值。', 50, 80, 'heal_hp', 400);

-- 消耗品 - 魔法药水
INSERT INTO `wg_item_template` (`item_id`, `item_name`, `category`, `rarity`, `description`, `max_stack`, `sell_price`, `effect_type`, `effect_value`) 
VALUES 
(1004, '魔法药水', 'consumable', 'Normal', '恢复 30 点魔法值。', 99, 15, 'heal_mp', 30),
(1005, '高级魔法药水', 'consumable', 'Rare', '恢复 80 点魔法值。', 99, 40, 'heal_mp', 80);

-- 消耗品 - 经验卷轴
INSERT INTO `wg_item_template` (`item_id`, `item_name`, `category`, `rarity`, `description`, `max_stack`, `sell_price`, `effect_type`, `effect_value`) 
VALUES 
(1006, '经验卷轴（小）', 'consumable', 'Rare', '使用后可获得 100 点经验值。', 20, 50, 'add_exp', 100),
(1007, '经验卷轴（中）', 'consumable', 'Epic', '使用后可获得 500 点经验值。', 10, 200, 'add_exp', 500),
(1008, '经验卷轴（大）', 'consumable', 'Legendary', '珍贵的成长秘宝，使用后可获得 2000 点经验值。', 5, 800, 'add_exp', 2000);

-- 消耗品 - 复活卷轴
INSERT INTO `wg_item_template` (`item_id`, `item_name`, `category`, `rarity`, `description`, `max_stack`, `sell_price`, `effect_type`, `effect_value`) 
VALUES 
(1009, '复活卷轴', 'consumable', 'Epic', '角色阵亡时使用，复活并恢复 30% 最大生命值。', 5, 300, 'revive', 30);

-- 材料 - 矿石类
INSERT INTO `wg_item_template` (`item_id`, `item_name`, `category`, `rarity`, `description`, `max_stack`, `sell_price`, `source`, `usage`) 
VALUES 
(2001, '铁矿石', 'material', 'Normal', '基础的锻造材料。', 99, 5, '迷雾森林矿点、普通副本掉落', '装备强化（+1~+3）、普通装备制作'),
(2002, '精钢矿石', 'material', 'Rare', '优质的锻造材料。', 50, 20, '白骨荒野矿点、精英副本掉落', '装备强化（+4~+6）、稀有装备制作');

-- 材料 - 稀有材料
INSERT INTO `wg_item_template` (`item_id`, `item_name`, `category`, `rarity`, `description`, `max_stack`, `sell_price`, `source`, `usage`) 
VALUES 
(2003, '秘法水晶', 'material', 'Epic', '蕴含强大魔力的水晶。', 20, 100, '团队副本 Boss 掉落', '装备强化（+7~+10）、史诗装备制作、战宠进化'),
(2004, '龙鳞碎片', 'material', 'Legendary', '传说中的龙鳞碎片，极其珍贵。', 10, 500, '团队副本 Boss（低概率掉落）', '传说装备制作、战宠最终进化');

-- 材料 - 战宠相关
INSERT INTO `wg_item_template` (`item_id`, `item_name`, `category`, `rarity`, `description`, `max_stack`, `sell_price`, `source`, `usage`) 
VALUES 
(2005, '战宠进化石', 'material', 'Epic', '用于战宠进化的神秘石头。', 10, 150, '精英副本、团队副本掉落', '战宠进化（所有品质）');


-- ==================== 验证数据 ====================

-- 查看创建的物品数量
SELECT COUNT(*) as total_items FROM wg_item_template;

-- 查看所有物品
SELECT item_id, item_name, category, rarity, max_stack 
FROM wg_item_template 
ORDER BY item_id;
