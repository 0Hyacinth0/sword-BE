-- ============================================
-- WebGame 战斗系统 - 怪物测试数据
-- 插入基础怪物模板和掉落配置
-- ============================================

-- 插入怪物模板数据
INSERT INTO `wg_monster_template` (
  `monster_id`, `name`, `level`, `element`,
  `base_hp`, `base_mp`, `base_physical_attack`, `base_magic_attack`, 
  `base_defense`, `base_speed`, `base_dodge_rate`, `base_critical_rate`,
  `hp_growth`, `physical_attack_growth`, `magic_attack_growth`, `defense_growth`, `speed_growth`,
  `skill_ids`, `exp_reward`, `gold_reward`, `drop_rate`, `description`
) VALUES
-- 低级怪物 (1-5级)
('monster-001', '暗影狼', 3, 6, 80, 20, 15, 3, 6, 12, 0.08, 0.05, 8, 2, 1, 1, 1, '[9001, 9002]', 45, 18, 0.15, '游荡在黑暗森林中的狼群'),
('monster-002', '哥布林盗贼', 2, 0, 60, 15, 12, 2, 5, 14, 0.12, 0.08, 6, 2, 0, 1, 2, '[9002]', 35, 15, 0.15, '贪婪的哥布林，喜欢偷窃'),
('monster-003', '史莱姆', 1, 2, 50, 10, 8, 5, 4, 8, 0.05, 0.03, 5, 1, 1, 0, 0, '[]', 25, 10, 0.10, '最基础的魔物，弱小的水元素生物'),

-- 中级怪物 (6-10级)
('monster-004', '骷髅战士', 7, 0, 120, 30, 22, 5, 12, 10, 0.06, 0.06, 12, 3, 1, 2, 1, '[9001, 9003]', 85, 32, 0.18, '复活的骷髅士兵，手持破旧武器'),
('monster-005', '火焰精灵', 8, 1, 100, 40, 18, 25, 8, 15, 0.10, 0.10, 10, 2, 3, 1, 2, '[9004]', 95, 38, 0.20, '诞生于熔岩的精灵，擅长火属性魔法'),
('monster-006', '冰霜巨魔', 10, 2, 180, 35, 28, 8, 18, 9, 0.04, 0.05, 15, 3, 1, 2, 1, '[9001, 9003]', 120, 45, 0.22, '居住在寒冷地区的巨型 troll');

-- 插入怪物掉落配置
INSERT INTO `wg_monster_drops` (`monster_id`, `item_id`, `min_quantity`, `max_quantity`, `drop_weight`, `quality`, `item_type`) VALUES
-- 暗影狼掉落
('monster-001', 'item-material-001', 1, 2, 100, 'common', 'material'),  -- 暗影狼的碎片
('monster-001', 'item-consumable-001', 1, 1, 50, 'common', 'consumable'),  -- 小回复药水

-- 哥布林盗贼掉落
('monster-002', 'item-material-002', 1, 3, 100, 'common', 'material'),  -- 哥布林的牙齿
('monster-002', 'equip-weapon-001', 1, 1, 20, 'rare', 'equipment'),  -- 精钢长剑(稀有)

-- 史莱姆掉落
('monster-003', 'item-material-003', 1, 2, 100, 'common', 'material'),  -- 史莱姆凝胶
('monster-003', 'item-consumable-001', 1, 1, 60, 'common', 'consumable'),

-- 骷髅战士掉落
('monster-004', 'item-material-004', 1, 2, 100, 'common', 'material'),  -- 骨头碎片
('monster-004', 'equip-chest-001', 1, 1, 25, 'rare', 'equipment'),  -- 铁甲(稀有)

-- 火焰精灵掉落
('monster-005', 'item-material-005', 1, 2, 100, 'common', 'material'),  -- 火焰精华
('monster-005', 'equip-weapon-002', 1, 1, 30, 'epic', 'equipment'),  -- 法师法杖(史诗)

-- 冰霜巨魔掉落
('monster-006', 'item-material-006', 1, 3, 100, 'common', 'material'),  -- 冰霜结晶
('monster-006', 'equip-accessory-002', 1, 1, 35, 'epic', 'equipment');  -- 冰霜戒指(史诗)
