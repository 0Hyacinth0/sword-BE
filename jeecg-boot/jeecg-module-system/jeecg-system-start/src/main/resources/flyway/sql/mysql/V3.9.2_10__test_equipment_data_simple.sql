-- ============================================
-- WebGame 装备系统测试数据 - 简化版
-- 用户名: 123456
-- 创建日期: 2026-05-09
-- ============================================

-- 步骤1: 确保 wg_character_inventory 表有 extra_stats 字段
SET @dbname = DATABASE();
SET @tablename = 'wg_character_inventory';
SET @columnname = 'extra_stats';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  'SELECT 1',
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN `', @columnname, '` JSON DEFAULT NULL COMMENT \'随机属性加成(JSON格式，仅装备类物品)\' AFTER `obtained_at`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 步骤2: 创建临时变量存储角色ID
SET @username = '123456';
SET @character_id = (SELECT id FROM wg_character WHERE user_id = (SELECT id FROM wg_user WHERE username = @username) LIMIT 1);

-- 检查角色是否存在
SELECT @character_id AS '角色ID';

-- 如果角色ID为NULL，说明用户不存在，停止执行
-- 您可以先执行以下SQL确认用户和角色存在：
-- SELECT u.id AS user_id, u.username, c.id AS character_id, c.name 
-- FROM wg_user u 
-- LEFT JOIN wg_character c ON u.id = c.user_id 
-- WHERE u.username = '123456';

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

INSERT IGNORE INTO `wg_character_inventory` (`id`, `character_id`, `item_id`, `quantity`, `obtained_at`, `extra_stats`, `create_time`, `update_time`) 
SELECT UUID(), @character_id, 'equip-weapon-001', 1, NOW(), '[{"key":"agility","value":2},{"key":"dodgeRate","value":0.02}]', NOW(), NOW()
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_inventory` (`id`, `character_id`, `item_id`, `quantity`, `obtained_at`, `extra_stats`, `create_time`, `update_time`) 
SELECT UUID(), @character_id, 'equip-weapon-002', 1, NOW(), '[{"key":"intelligence","value":3},{"key":"mp","value":15}]', NOW(), NOW()
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_inventory` (`id`, `character_id`, `item_id`, `quantity`, `obtained_at`, `extra_stats`, `create_time`, `update_time`) 
SELECT UUID(), @character_id, 'equip-helmet-001', 1, NOW(), NULL, NOW(), NOW()
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_inventory` (`id`, `character_id`, `item_id`, `quantity`, `obtained_at`, `extra_stats`, `create_time`, `update_time`) 
SELECT UUID(), @character_id, 'equip-chest-001', 1, NOW(), NULL, NOW(), NOW()
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_inventory` (`id`, `character_id`, `item_id`, `quantity`, `obtained_at`, `extra_stats`, `create_time`, `update_time`) 
SELECT UUID(), @character_id, 'equip-legs-001', 1, NOW(), NULL, NOW(), NOW()
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_inventory` (`id`, `character_id`, `item_id`, `quantity`, `obtained_at`, `extra_stats`, `create_time`, `update_time`) 
SELECT UUID(), @character_id, 'equip-chest-002', 1, NOW(), '[{"key":"hp","value":30},{"key":"defense","value":5},{"key":"mp","value":20}]', NOW(), NOW()
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_inventory` (`id`, `character_id`, `item_id`, `quantity`, `obtained_at`, `extra_stats`, `create_time`, `update_time`) 
SELECT UUID(), @character_id, 'equip-accessory-001', 1, NOW(), '[{"key":"strength","value":3}]', NOW(), NOW()
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_inventory` (`id`, `character_id`, `item_id`, `quantity`, `obtained_at`, `extra_stats`, `create_time`, `update_time`) 
SELECT UUID(), @character_id, 'equip-accessory-002', 1, NOW(), '[{"key":"intelligence","value":4},{"key":"magicAttack","value":5}]', NOW(), NOW()
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_inventory` (`id`, `character_id`, `item_id`, `quantity`, `obtained_at`, `extra_stats`, `create_time`, `update_time`) 
SELECT UUID(), @character_id, 'equip-weapon-003', 1, NOW(), '[{"key":"physicalAttack","value":8},{"key":"strength","value":5},{"key":"criticalRate","value":0.03},{"key":"agility","value":4}]', NOW(), NOW()
FROM DUAL WHERE @character_id IS NOT NULL;

-- ============================================
-- 第三部分：直接在装备栏穿戴部分装备
-- ============================================

INSERT IGNORE INTO `wg_character_equipments` (`id`, `character_id`, `item_id`, `slot_type`, `extra_stats`, `enhance_level`, `create_time`, `update_time`) 
SELECT UUID(), @character_id, 'equip-weapon-001', 'weapon', '[{"key":"agility","value":2},{"key":"dodgeRate","value":0.02}]', 0, NOW(), NOW()
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_equipments` (`id`, `character_id`, `item_id`, `slot_type`, `extra_stats`, `enhance_level`, `create_time`, `update_time`) 
SELECT UUID(), @character_id, 'equip-helmet-001', 'helmet', NULL, 0, NOW(), NOW()
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_equipments` (`id`, `character_id`, `item_id`, `slot_type`, `extra_stats`, `enhance_level`, `create_time`, `update_time`) 
SELECT UUID(), @character_id, 'equip-chest-001', 'chest', NULL, 2, NOW(), NOW()
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_equipments` (`id`, `character_id`, `item_id`, `slot_type`, `extra_stats`, `enhance_level`, `create_time`, `update_time`) 
SELECT UUID(), @character_id, 'equip-legs-001', 'legs', NULL, 0, NOW(), NOW()
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_equipments` (`id`, `character_id`, `item_id`, `slot_type`, `extra_stats`, `enhance_level`, `create_time`, `update_time`) 
SELECT UUID(), @character_id, 'equip-accessory-001', 'accessory1', '[{"key":"strength","value":3}]', 0, NOW(), NOW()
FROM DUAL WHERE @character_id IS NOT NULL;
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
WHERE ci.character_id = @character_id
ORDER BY ci.create_time DESC;

-- 查看角色的装备
SELECT ce.id, ce.character_id, ce.slot_type, it.name, ce.enhance_level, ce.extra_stats
FROM wg_character_equipments ce
LEFT JOIN wg_item_template it ON ce.item_id = it.item_id
WHERE ce.character_id = @character_id
ORDER BY ce.slot_type;
