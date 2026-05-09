-- ============================================
-- WebGame 装备系统测试数据
-- 用户名: 123456
-- 创建日期: 2026-05-09
-- ============================================

-- 步骤1: 先查询用户ID和角色ID（需要根据实际情况修改）
-- SELECT id FROM wg_user WHERE username = '123456';
-- SELECT id FROM wg_character WHERE user_id = (SELECT id FROM wg_user WHERE username = '123456') LIMIT 1;

-- 假设用户ID为: user-123456-uuid
-- 假设角色ID为: char-test-uuid
-- 请根据实际情况替换下面的 UUID

-- ============================================
-- 第一部分：插入装备物品模板（如果不存在）
-- ============================================

-- 武器类装备
INSERT IGNORE INTO `wg_item_template` (`item_id`, `name`, `type`, `slot_type`, `rarity`, `base_stats`, `set_id`, `set_name`, `icon`, `description`, `level_requirement`) 
VALUES 
('equip-weapon-001', '精钢长剑', 1, 'weapon', 'Rare', '{"physicalAttack": 15, "strength": 3}', 'set-001', '勇者之证', '/assets/items/equipment/sword_001.png', '一把锋利的精钢长剑，适合战士使用。', 5),
('equip-weapon-002', '法师法杖', 1, 'weapon', 'Epic', '{"magicAttack": 20, "intelligence": 5}', NULL, NULL, '/assets/items/equipment/staff_001.png', '蕴含魔力的法杖，能增强法术威力。', 10),
('equip-weapon-003', '传说之剑', 1, 'weapon', 'Legendary', '{"physicalAttack": 35, "strength": 8, "criticalRate": 0.05}', 'set-003', '传说勇士', '/assets/items/equipment/sword_legendary.png', '传说中的神器，拥有强大的力量。', 20);

-- 防具类装备
INSERT IGNORE INTO `wg_item_template` (`item_id`, `name`, `type`, `slot_type`, `rarity`, `base_stats`, `set_id`, `set_name`, `icon`, `description`, `level_requirement`) 
VALUES 
('equip-helmet-001', '铁盔', 1, 'helmet', 'Normal', '{"defense": 3, "hp": 20}', NULL, NULL, '/assets/items/equipment/helmet_001.png', '基础的铁制头盔。', 1),
('equip-chest-001', '铁甲胸铠', 1, 'chest', 'Normal', '{"defense": 8, "hp": 50}', NULL, NULL, '/assets/items/equipment/chest_001.png', '坚固的铁甲，提供良好的防护。', 1),
('equip-legs-001', '战靴', 1, 'legs', 'Normal', '{"defense": 4, "agility": 2}', NULL, NULL, '/assets/items/equipment/legs_001.png', '轻便的战靴，提升移动速度。', 1),
('equip-chest-002', '史诗铠甲', 1, 'chest', 'Epic', '{"defense": 18, "hp": 120, "mp": 50}', 'set-002', '冰霜之心', '/assets/items/equipment/chest_epic.png', '由冰霜魔法加持的铠甲。', 15);

-- 饰品类装备
INSERT IGNORE INTO `wg_item_template` (`item_id`, `name`, `type`, `slot_type`, `rarity`, `base_stats`, `set_id`, `set_name`, `icon`, `description`, `level_requirement`) 
VALUES 
('equip-accessory-001', '力量戒指', 1, 'accessory1', 'Rare', '{"strength": 5, "physicalAttack": 5}', NULL, NULL, '/assets/items/equipment/ring_001.png', '镶嵌着力宝石的戒指。', 8),
('equip-accessory-002', '智力项链', 1, 'accessory2', 'Rare', '{"intelligence": 6, "magicAttack": 8}', NULL, NULL, '/assets/items/equipment/necklace_001.png', '闪烁着智慧光芒的项链。', 8);

-- ============================================
-- 第二部分：插入背包中的装备物品
-- ============================================

-- 注意：需要将 character_id 替换为实际的角色ID
-- 以下示例使用 'char-test-uuid' 作为占位符

INSERT INTO `wg_character_inventory` (`id`, `character_id`, `item_id`, `quantity`, `obtained_at`, `extra_stats`, `create_time`, `update_time`) 
VALUES 
-- 武器
(UUID(), 'char-test-uuid', 'equip-weapon-001', 1, NOW(), '[{"key":"agility","value":2},{"key":"dodgeRate","value":0.02}]', NOW(), NOW()),
(UUID(), 'char-test-uuid', 'equip-weapon-002', 1, NOW(), '[{"key":"intelligence","value":3},{"key":"mp","value":15}]', NOW(), NOW()),

-- 防具
(UUID(), 'char-test-uuid', 'equip-helmet-001', 1, NOW(), NULL, NOW(), NOW()),
(UUID(), 'char-test-uuid', 'equip-chest-001', 1, NOW(), NULL, NOW(), NOW()),
(UUID(), 'char-test-uuid', 'equip-legs-001', 1, NOW(), NULL, NOW(), NOW()),
(UUID(), 'char-test-uuid', 'equip-chest-002', 1, NOW(), '[{"key":"hp","value":30},{"key":"defense","value":5},{"key":"mp","value":20}]', NOW(), NOW()),

-- 饰品
(UUID(), 'char-test-uuid', 'equip-accessory-001', 1, NOW(), '[{"key":"strength","value":3}]', NOW(), NOW()),
(UUID(), 'char-test-uuid', 'equip-accessory-002', 1, NOW(), '[{"key":"intelligence","value":4},{"key":"magicAttack","value":5}]', NOW(), NOW()),

-- 传说装备（带多条随机词条）
(UUID(), 'char-test-uuid', 'equip-weapon-003', 1, NOW(), '[{"key":"physicalAttack","value":8},{"key":"strength","value":5},{"key":"criticalRate","value":0.03},{"key":"agility","value":4}]', NOW(), NOW());

-- ============================================
-- 第三部分：可选 - 直接在装备栏穿戴部分装备
-- ============================================

-- 如果需要直接穿戴某些装备到角色身上，可以插入到 wg_character_equipments 表
-- 注意：每个槽位只能有一件装备

INSERT INTO `wg_character_equipments` (`id`, `character_id`, `item_id`, `slot_type`, `extra_stats`, `enhance_level`, `create_time`, `update_time`) 
VALUES 
-- 已穿戴的装备示例
(UUID(), 'char-test-uuid', 'equip-weapon-001', 'weapon', '[{"key":"agility","value":2},{"key":"dodgeRate","value":0.02}]', 0, NOW(), NOW()),
(UUID(), 'char-test-uuid', 'equip-helmet-001', 'helmet', NULL, 0, NOW(), NOW()),
(UUID(), 'char-test-uuid', 'equip-chest-001', 'chest', NULL, 2, NOW(), NOW()),  -- 强化+2
(UUID(), 'char-test-uuid', 'equip-legs-001', 'legs', NULL, 0, NOW(), NOW()),
(UUID(), 'char-test-uuid', 'equip-accessory-001', 'accessory1', '[{"key":"strength","value":3}]', 0, NOW(), NOW());
-- accessory2 槽位为空

-- ============================================
-- 验证数据
-- ============================================

-- 查看插入的装备模板
SELECT item_id, name, type, slot_type, rarity, level_requirement 
FROM wg_item_template 
WHERE item_id LIKE 'equip-%'
ORDER BY item_id;

-- 查看角色的背包物品
SELECT ci.id, ci.character_id, it.name, ci.quantity, ci.extra_stats
FROM wg_character_inventory ci
LEFT JOIN wg_item_template it ON ci.item_id = it.item_id
WHERE ci.character_id = 'char-test-uuid'
ORDER BY ci.create_time DESC;

-- 查看角色的装备
SELECT ce.id, ce.character_id, ce.slot_type, it.name, ce.enhance_level, ce.extra_stats
FROM wg_character_equipments ce
LEFT JOIN wg_item_template it ON ce.item_id = it.item_id
WHERE ce.character_id = 'char-test-uuid'
ORDER BY ce.slot_type;
