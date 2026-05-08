-- ============================================
-- WebGame 物品数据初始化
-- 插入消耗品和材料的基础数据
-- ============================================

-- ==================== 消耗品数据 ====================

-- 生命药水
INSERT INTO `wg_item_template` (`item_id`, `item_name`, `category`, `rarity`, `description`, `max_stack`, `sell_price`, `effect_type`, `effect_value`) 
VALUES 
(1001, '小型生命药水', 'consumable', 'Normal', '恢复 50 点生命值。', 99, 10, 'heal_hp', 50),
(1002, '中型生命药水', 'consumable', 'Rare', '恢复 150 点生命值。', 99, 30, 'heal_hp', 150),
(1003, '大型生命药水', 'consumable', 'Epic', '恢复 400 点生命值。', 50, 80, 'heal_hp', 400);

-- 魔法药水
INSERT INTO `wg_item_template` (`item_id`, `item_name`, `category`, `rarity`, `description`, `max_stack`, `sell_price`, `effect_type`, `effect_value`) 
VALUES 
(1004, '魔法药水', 'consumable', 'Normal', '恢复 30 点魔法值。', 99, 15, 'heal_mp', 30),
(1005, '高级魔法药水', 'consumable', 'Rare', '恢复 80 点魔法值。', 99, 40, 'heal_mp', 80);

-- 经验卷轴
INSERT INTO `wg_item_template` (`item_id`, `item_name`, `category`, `rarity`, `description`, `max_stack`, `sell_price`, `effect_type`, `effect_value`) 
VALUES 
(1006, '经验卷轴（小）', 'consumable', 'Rare', '使用后可获得 100 点经验值。', 20, 50, 'add_exp', 100),
(1007, '经验卷轴（中）', 'consumable', 'Epic', '使用后可获得 500 点经验值。', 10, 200, 'add_exp', 500),
(1008, '经验卷轴（大）', 'consumable', 'Legendary', '珍贵的成长秘宝，使用后可获得 2000 点经验值。', 5, 800, 'add_exp', 2000);

-- 复活卷轴
INSERT INTO `wg_item_template` (`item_id`, `item_name`, `category`, `rarity`, `description`, `max_stack`, `sell_price`, `effect_type`, `effect_value`) 
VALUES 
(1009, '复活卷轴', 'consumable', 'Epic', '角色阵亡时使用，复活并恢复 30% 最大生命值。', 5, 300, 'revive', 30);

-- ==================== 材料数据 ====================

-- 矿石类
INSERT INTO `wg_item_template` (`item_id`, `item_name`, `category`, `rarity`, `description`, `max_stack`, `sell_price`, `source`, `usage`) 
VALUES 
(2001, '铁矿石', 'material', 'Normal', '基础的锻造材料。', 99, 5, '迷雾森林矿点、普通副本掉落', '装备强化（+1~+3）、普通装备制作'),
(2002, '精钢矿石', 'material', 'Rare', '优质的锻造材料。', 50, 20, '白骨荒野矿点、精英副本掉落', '装备强化（+4~+6）、稀有装备制作');

-- 稀有材料
INSERT INTO `wg_item_template` (`item_id`, `item_name`, `category`, `rarity`, `description`, `max_stack`, `sell_price`, `source`, `usage`) 
VALUES 
(2003, '秘法水晶', 'material', 'Epic', '蕴含强大魔力的水晶。', 20, 100, '团队副本 Boss 掉落', '装备强化（+7~+10）、史诗装备制作、战宠进化'),
(2004, '龙鳞碎片', 'material', 'Legendary', '传说中的龙鳞碎片，极其珍贵。', 10, 500, '团队副本 Boss（低概率掉落）', '传说装备制作、战宠最终进化');

-- 战宠相关材料
INSERT INTO `wg_item_template` (`item_id`, `item_name`, `category`, `rarity`, `description`, `max_stack`, `sell_price`, `source`, `usage`) 
VALUES 
(2005, '战宠进化石', 'material', 'Epic', '用于战宠进化的神秘石头。', 10, 150, '精英副本、团队副本掉落', '战宠进化（所有品质）');

