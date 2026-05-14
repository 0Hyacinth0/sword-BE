-- ============================================
-- 战斗系统测试数据初始化脚本
-- 说明: 为战斗系统提供必要的测试数据
-- 执行顺序: 在 V3.9.2_15 ~ V3.9.2_21 之后执行
-- ============================================

-- 1. 插入怪物模板数据
INSERT IGNORE INTO `wg_monster_template` (`monster_id`, `name`, `level`, `element`, `base_hp`, `base_mp`, `base_physical_attack`, `base_magic_attack`, `base_defense`, `base_speed`, `base_dodge_rate`, `base_critical_rate`, `exp_reward`, `gold_reward`, `skill_ids`, `description`) VALUES
('monster-001', '暗影狼', 5, 6, 80, 20, 15, 3, 6, 12, 0.08, 0.05, 45, 15, '[1001]', '游荡在森林深处的暗影狼，擅长突袭'),
('monster-002', '火焰史莱姆', 3, 1, 60, 30, 8, 12, 4, 8, 0.05, 0.03, 30, 10, '[1002]', '被火焰元素感染的史莱姆'),
('monster-003', '冰霜巨魔', 10, 2, 150, 40, 25, 8, 15, 10, 0.03, 0.08, 80, 30, '[1003]', '居住在寒冷地区的巨魔'),
('monster-004', '骷髅战士', 7, 6, 100, 0, 18, 0, 10, 9, 0.06, 0.06, 55, 20, '[1004]', '复活的骷髅战士'),
('monster-005', '雷霆鹰', 8, 3, 90, 35, 20, 15, 7, 18, 0.12, 0.10, 65, 25, '[1005]', '栖息在高山的雷霆之鹰'),
('monster-006', '毒液蜘蛛', 6, 4, 70, 25, 12, 10, 5, 14, 0.10, 0.04, 40, 12, '[1006]', '洞穴中的剧毒蜘蛛');

-- 2. 插入怪物掉落配置（如果V3.9.2_17未执行）
INSERT IGNORE INTO `wg_monster_drops` (`monster_id`, `item_id`, `item_type`, `min_quantity`, `max_quantity`, `drop_weight`, `quality`) VALUES
('monster-001', 'item-wolf-pelt', 'material', 1, 2, 30, 'common'),
('monster-001', 'item-small-health-potion', 'consumable', 1, 1, 20, 'common'),
('monster-002', 'item-fire-essence', 'material', 1, 1, 25, 'common'),
('monster-003', 'item-ice-crystal', 'material', 1, 2, 35, 'rare'),
('monster-003', 'item-medium-health-potion', 'consumable', 1, 1, 15, 'common'),
('monster-004', 'item-bone-fragment', 'material', 2, 3, 40, 'common'),
('monster-005', 'item-lightning-feather', 'material', 1, 1, 20, 'rare'),
('monster-006', 'item-spider-silk', 'material', 1, 2, 30, 'common'),
('monster-006', 'item-poison-sac', 'material', 1, 1, 15, 'rare');

-- 3. 为角色 2052660469459632129 创建一个测试战宠（用于战斗测试）
-- 先检查是否已有战宠
SELECT COUNT(*) as pet_count FROM wg_character_pets WHERE character_id = '2052660469459632129';

-- 如果没有战宠，创建一个
INSERT IGNORE INTO `wg_character_pets` (
  `id`, `character_id`, `pet_type_id`, `nickname`, 
  `level`, `exp`, `max_exp`, `rarity`, 
  `is_active`, `hp`, `max_hp`, `mp`, `max_mp`,
  `physical_attack`, `magic_attack`, `defense`, `speed`,
  `skill_slot_1`, `skill_slot_2`, `skill_slot_3`,
  `create_time`, `update_time`
) VALUES (
  'pet-test-001', 
  '2052660469459632129', 
  'pet-type-001',  -- 假设已有宠物类型，需要根据实际情况修改
  '雷霆',
  10, 0, 1000, 'rare',
  1,  -- 设为出战状态
  200, 200, 80, 80,
  45, 30, 35, 25,
  1, 2, NULL,  -- 技能ID，需要根据实际技能表修改
  NOW(), NOW()
);

-- 4. 创建测试背包物品（消耗品，用于战斗中可能使用）
-- 注意: 这里假设 wg_character_inventory 表已存在
INSERT IGNORE INTO `wg_character_inventory` (
  `id`, `character_id`, `item_id`, `item_name`, `item_type`, 
  `quantity`, `max_stack`, `obtained_at`
) VALUES
(UUID(), '2052660469459632129', 'item-small-health-potion', '小型生命药水', 'consumable', 10, 99, NOW()),
(UUID(), '2052660469459632129', 'item-small-mana-potion', '小型法力药水', 'consumable', 10, 99, NOW()),
(UUID(), '2052660469459632129', 'item-revive-token', '复活币', 'special', 5, 99, NOW());

-- 5. 验证数据
SELECT '=== 怪物模板数据 ===' as info;
SELECT monster_id, name, level, element, base_hp, base_physical_attack FROM wg_monster_template;

SELECT '=== 怪物掉落配置 ===' as info;
SELECT monster_id, item_id, drop_weight, quality FROM wg_monster_drops;

SELECT '=== 角色战宠 ===' as info;
SELECT id, nickname, level, rarity, is_active FROM wg_character_pets WHERE character_id = '2052660469459632129';

SELECT '=== 技能列表(前10个) ===' as info;
SELECT skill_id, name, type, power, mp_cost FROM wg_pet_skills LIMIT 10;
