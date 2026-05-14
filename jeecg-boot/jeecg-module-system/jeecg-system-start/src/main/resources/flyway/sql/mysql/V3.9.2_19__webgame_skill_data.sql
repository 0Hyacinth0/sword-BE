-- ============================================
-- WebGame 战斗系统 - 职业技能配置数据
-- 插入战士、法师、猎人的技能数据到 wg_pet_skills 表
-- 注意：wg_pet_skills 表已存在，这里只插入数据
-- ============================================

-- 战士技能 (WARRIOR)
INSERT INTO `wg_pet_skills` (`skill_id`, `name`, `type`, `power`, `cooldown`, `mp_cost`, `learn_level`, `description`, `pet_type_ids`) VALUES
(1001, '旋风斩', 'active_attack', 130, 2, 8, 1, '挥舞武器形成风暴，对全体敌人造成伤害', '[1001]'),
(1002, '重击', 'active_attack', 180, 3, 12, 1, '强力一击，对单个敌人造成巨额伤害', '[1001]'),
(1003, '钢铁壁垒', 'active_buff', NULL, 4, 8, 5, '提升自身防御力15点，持续2回合', '[1001]'),
(1004, '战吼', 'active_buff', NULL, 5, 10, 8, '提升自身攻击力15点，持续3回合', '[1001]'),
(1005, '狂战士之血', 'passive', NULL, NULL, NULL, 3, 'HP低于30%时，攻击力提升15点', '[1001]'),
(1006, '破甲打击', 'active_attack', 120, 3, 10, 12, '降低目标防御力10点，持续3回合', '[1001]');

-- 法师技能 (MAGE)
INSERT INTO `wg_pet_skills` (`skill_id`, `name`, `type`, `power`, `cooldown`, `mp_cost`, `learn_level`, `description`, `pet_type_ids`) VALUES
(2001, '火球术', 'active_attack', 160, 2, 12, 1, '发射火球对单个敌人造成伤害', '[1002]'),
(2002, '陨石术', 'active_attack', 140, 4, 20, 1, '召唤陨石对全体敌人造成伤害并施加灼烧', '[1002]'),
(2003, '治疗术', 'active_heal', 120, 2, 10, 3, '恢复自身生命值', '[1002]'),
(2004, '魔力涌动', 'active_buff', NULL, 5, 8, 6, '提升自身魔法攻击力20点，持续3回合', '[1002]'),
(2005, '奥术屏障', 'passive', NULL, NULL, NULL, 4, '被攻击时有20%概率提升防御力15点', '[1002]'),
(2006, '冰冻术', 'active_attack', 110, 3, 15, 10, '降低目标速度5点，持续2回合', '[1002]');

-- 猎人技能 (HUNTER)
INSERT INTO `wg_pet_skills` (`skill_id`, `name`, `type`, `power`, `cooldown`, `mp_cost`, `learn_level`, `description`, `pet_type_ids`) VALUES
(3001, '穿心箭', 'active_attack', 180, 2, 10, 1, '精准射击，对单个敌人造成高额伤害', '[1003]'),
(3002, '连射', 'active_attack', 90, 2, 8, 1, '快速射击，对全体敌人造成伤害', '[1003]'),
(3003, '影遁', 'active_buff', NULL, 4, 8, 4, '提升自身闪避率20%，持续2回合', '[1003]'),
(3004, '鹰眼', 'active_buff', NULL, 5, 6, 7, '提升自身暴击率15%，持续3回合', '[1003]'),
(3005, '嗜血本能', 'passive', NULL, NULL, NULL, 5, '暴击时有50%概率提升速度8点', '[1003]'),
(3006, '毒箭', 'active_attack', 100, 3, 10, 11, '对目标施加灼烧Debuff', '[1003]');

-- 怪物技能 (通用)
INSERT INTO `wg_pet_skills` (`skill_id`, `name`, `type`, `power`, `cooldown`, `mp_cost`, `learn_level`, `description`, `pet_type_ids`) VALUES
(9001, '撕咬', 'active_attack', 120, 0, 0, 1, '用利齿撕咬目标', NULL),
(9002, '爪击', 'active_attack', 110, 0, 0, 1, '用锋利的爪子攻击', NULL),
(9003, '冲撞', 'active_attack', 130, 2, 5, 1, '猛烈冲撞目标', NULL),
(9004, '咆哮', 'active_buff', NULL, 3, 8, 1, '提升自身攻击力10点，持续2回合', NULL);
