-- ============================================
-- WebGame 装备系统测试数据 - 自动版
-- 用户名: 123456
-- 创建日期: 2026-05-09
-- 特点：自动查询角色ID，无需手动替换
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
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN `', @columnname, '` JSON DEFAULT NULL COMMENT \'随机属性加成\' AFTER `obtained_at`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 步骤2: 查询角色ID（user_id 直接存储用户名）
SET @username = '123456';

-- 查询该用户的所有角色
SELECT 
  c.id AS '角色ID',
  c.character_name AS '角色名称',
  c.profession AS '职业',
  c.level AS '等级',
  c.user_id AS 'user_id'
FROM wg_character c
WHERE c.user_id = @username
ORDER BY c.create_time;

-- 选择第一个角色（按创建时间）
SET @character_id = (
  SELECT c.id 
  FROM wg_character c
  WHERE c.user_id = @username
  ORDER BY c.create_time
  LIMIT 1
);

-- 显示最终选择的角色
SELECT 
  IFNULL(@character_id, '❌ 该用户没有角色，请先创建角色') AS '将测试的角色ID',
  IFNULL((SELECT character_name FROM wg_character WHERE id = @character_id), '无') AS '角色名称';

-- ============================================
-- 第一部分：插入装备物品模板
-- ============================================

-- 武器类装备
INSERT IGNORE INTO `wg_item_template` (`item_id`, `name`, `type`, `slot_type`, `rarity`, `base_stats`, `set_id`, `set_name`, `icon`, `description`, `level_requirement`) 
VALUES 
('equip-weapon-001', '精钢长剑', 1, 'weapon', 'Rare', '{"physicalAttack": 15, "strength": 3}', 'set-001', '勇者之证', '/assets/items/equipment/sword_001.png', '一把锋利的精钢长剑，适合战士使用。', 5),
('equip-weapon-002', '法师法杖', 1, 'weapon', 'Epic', '{"magicAttack": 20, "intelligence": 5}', NULL, NULL, '/assets/items/equipment/staff_001.png', '蕴含魔力的法杖，能增强法术威力。', 1);
('equip-weapon-003', '传说之剑', 1, 'weapon', 'Legendary', '{"physicalAttack": 35, "strength": 8, "criticalRate": 0.05}', 'set-003', '传说勇士', '/assets/items/equipment/sword_legendary.png', '传说中的神器，拥有强大的力量。', 1);

-- 防具类装备
INSERT IGNORE INTO `wg_item_template` (`item_id`, `name`, `type`, `slot_type`, `rarity`, `base_stats`, `set_id`, `set_name`, `icon`, `description`, `level_requirement`) 
VALUES 
('equip-helmet-001', '铁盔', 1, 'helmet', 'Normal', '{"defense": 3, "hp": 20}', NULL, NULL, '/assets/items/equipment/helmet_001.png', '基础的铁制头盔。', 1),
('equip-chest-001', '铁甲胸铠', 1, 'chest', 'Normal', '{"defense": 8, "hp": 50}', NULL, NULL, '/assets/items/equipment/chest_001.png', '坚固的铁甲，提供良好的防护。', 1),
('equip-legs-001', '战靴', 1, 'legs', 'Normal', '{"defense": 4, "agility": 2}', NULL, NULL, '/assets/items/equipment/legs_001.png', '轻便的战靴，提升移动速度。', 1),
('equip-chest-002', '史诗铠甲', 1, 'chest', 'Epic', '{"defense": 18, "hp": 120, "mp": 50}', 'set-002', '冰霜之心', '/assets/items/equipment/chest_epic.png', '由冰霜魔法加持的铠甲。', 1);

-- 饰品类装备
INSERT IGNORE INTO `wg_item_template` (`item_id`, `name`, `type`, `slot_type`, `rarity`, `base_stats`, `set_id`, `set_name`, `icon`, `description`, `level_requirement`) 
VALUES 
('equip-accessory-001', '力量戒指', 1, 'accessory1', 'Rare', '{"strength": 5, "physicalAttack": 5}', NULL, NULL, '/assets/items/equipment/ring_001.png', '镶嵌着力宝石的戒指。', 1),
('equip-accessory-002', '智力项链', 1, 'accessory2', 'Rare', '{"intelligence": 6, "magicAttack": 8}', NULL, NULL, '/assets/items/equipment/necklace_001.png', '闪烁着智慧光芒的项链。', 1);

-- ============================================
-- 第二部分：插入背包中的装备物品（自动使用角色ID）
-- ============================================

INSERT IGNORE INTO `wg_character_inventory` (`id`, `character_id`, `item_id`, `quantity`, `obtained_at`, `extra_stats`) 
SELECT UUID(), @character_id, 'equip-weapon-001', 1, NOW(), '[{"key":"agility","value":2},{"key":"dodgeRate","value":0.02}]'
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_inventory` (`id`, `character_id`, `item_id`, `quantity`, `obtained_at`, `extra_stats`) 
SELECT UUID(), @character_id, 'equip-weapon-002', 1, NOW(), '[{"key":"intelligence","value":3},{"key":"mp","value":15}]'
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_inventory` (`id`, `character_id`, `item_id`, `quantity`, `obtained_at`, `extra_stats`) 
SELECT UUID(), @character_id, 'equip-helmet-001', 1, NOW(), NULL
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_inventory` (`id`, `character_id`, `item_id`, `quantity`, `obtained_at`, `extra_stats`) 
SELECT UUID(), @character_id, 'equip-chest-001', 1, NOW(), NULL
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_inventory` (`id`, `character_id`, `item_id`, `quantity`, `obtained_at`, `extra_stats`) 
SELECT UUID(), @character_id, 'equip-legs-001', 1, NOW(), NULL
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_inventory` (`id`, `character_id`, `item_id`, `quantity`, `obtained_at`, `extra_stats`) 
SELECT UUID(), @character_id, 'equip-chest-002', 1, NOW(), '[{"key":"hp","value":30},{"key":"defense","value":5},{"key":"mp","value":20}]'
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_inventory` (`id`, `character_id`, `item_id`, `quantity`, `obtained_at`, `extra_stats`) 
SELECT UUID(), @character_id, 'equip-accessory-001', 1, NOW(), '[{"key":"strength","value":3}]'
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_inventory` (`id`, `character_id`, `item_id`, `quantity`, `obtained_at`, `extra_stats`) 
SELECT UUID(), @character_id, 'equip-accessory-002', 1, NOW(), '[{"key":"intelligence","value":4},{"key":"magicAttack","value":5}]'
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_inventory` (`id`, `character_id`, `item_id`, `quantity`, `obtained_at`, `extra_stats`) 
SELECT UUID(), @character_id, 'equip-weapon-003', 1, NOW(), '[{"key":"physicalAttack","value":8},{"key":"strength","value":5},{"key":"criticalRate","value":0.03},{"key":"agility","value":4}]'
FROM DUAL WHERE @character_id IS NOT NULL;

-- ============================================
-- 第三部分：穿戴部分装备到装备栏
-- ============================================

INSERT IGNORE INTO `wg_character_equipments` (`id`, `character_id`, `item_id`, `slot_type`, `extra_stats`, `enhance_level`) 
SELECT UUID(), @character_id, 'equip-weapon-001', 'weapon', '[{"key":"agility","value":2},{"key":"dodgeRate","value":0.02}]', 0
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_equipments` (`id`, `character_id`, `item_id`, `slot_type`, `extra_stats`, `enhance_level`) 
SELECT UUID(), @character_id, 'equip-helmet-001', 'helmet', NULL, 0
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_equipments` (`id`, `character_id`, `item_id`, `slot_type`, `extra_stats`, `enhance_level`) 
SELECT UUID(), @character_id, 'equip-chest-001', 'chest', NULL, 2
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_equipments` (`id`, `character_id`, `item_id`, `slot_type`, `extra_stats`, `enhance_level`) 
SELECT UUID(), @character_id, 'equip-legs-001', 'legs', NULL, 0
FROM DUAL WHERE @character_id IS NOT NULL;

INSERT IGNORE INTO `wg_character_equipments` (`id`, `character_id`, `item_id`, `slot_type`, `extra_stats`, `enhance_level`) 
SELECT UUID(), @character_id, 'equip-accessory-001', 'accessory1', '[{"key":"strength","value":3}]', 0
FROM DUAL WHERE @character_id IS NOT NULL;
-- accessory2 槽位留空

-- ============================================
-- 验证数据
-- ============================================

SELECT '========== 装备模板 ==========' AS '';
SELECT item_id, name, slot_type, rarity, level_requirement 
FROM wg_item_template 
WHERE item_id LIKE 'equip-%'
ORDER BY item_id;

SELECT '========== 背包物品 ==========' AS '';
SELECT ci.id, it.name, ci.quantity, ci.extra_stats
FROM wg_character_inventory ci
LEFT JOIN wg_item_template it ON ci.item_id = it.item_id
WHERE ci.character_id = @character_id
ORDER BY ci.create_time DESC;

SELECT '========== 已穿戴装备 ==========' AS '';
SELECT ce.slot_type, it.name, ce.enhance_level, ce.extra_stats
FROM wg_character_equipments ce
LEFT JOIN wg_item_template it ON ce.item_id = it.item_id
WHERE ce.character_id = @character_id
ORDER BY ce.slot_type;
