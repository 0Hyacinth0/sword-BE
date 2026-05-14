-- 单人副本系统 - 副本配置表
-- 版本: V3.9.2_24__webgame_dungeon_configs.sql
-- 描述: 创建副本配置、副本楼层相关表

-- ========================================
-- 1. 副本配置表 (wg_dungeon_configs)
-- ========================================
CREATE TABLE IF NOT EXISTS `wg_dungeon_configs` (
  `id` VARCHAR(64) PRIMARY KEY COMMENT '副本ID',
  `name` VARCHAR(50) NOT NULL COMMENT '副本名称',
  `area_id` VARCHAR(32) NOT NULL COMMENT '所属区域ID',
  `difficulty` ENUM('normal', 'elite') NOT NULL COMMENT '难度',
  `stamina_cost` INTEGER NOT NULL DEFAULT 10 COMMENT '体力消耗',
  `level_requirement` INTEGER NOT NULL DEFAULT 1 COMMENT '等级要求',
  `total_floors` INTEGER NOT NULL COMMENT '总层数',
  `bonus_exp` INTEGER NOT NULL DEFAULT 0 COMMENT '通关经验奖励',
  `bonus_gold` INTEGER NOT NULL DEFAULT 0 COMMENT '通关金币奖励',
  `guaranteed_items` JSON DEFAULT NULL COMMENT '通关保底物品JSON数组',
  `is_elite` BOOLEAN DEFAULT FALSE COMMENT '是否为精英副本',
  `elite_stat_multiplier` DECIMAL(3,2) DEFAULT 1.30 COMMENT '精英属性倍率',
  `elite_skill_group_id` INT DEFAULT NULL COMMENT '精英技能组ID',
  `sort_order` INTEGER DEFAULT 0 COMMENT '排序顺序',
  FOREIGN KEY (`area_id`) REFERENCES `wg_map_areas`(`id`) ON DELETE CASCADE,
  INDEX idx_area_id (`area_id`),
  INDEX idx_difficulty (`difficulty`),
  INDEX idx_sort_order (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='副本配置表';

-- ========================================
-- 2. 副本楼层配置表 (wg_dungeon_floors)
-- ========================================
CREATE TABLE IF NOT EXISTS `wg_dungeon_floors` (
  `id` SERIAL PRIMARY KEY COMMENT '自增ID',
  `dungeon_id` VARCHAR(64) NOT NULL COMMENT '所属副本ID',
  `floor_number` INTEGER NOT NULL COMMENT '楼层编号（1-based）',
  `enemies` JSON NOT NULL COMMENT '敌人列表JSON [{monsterId, level, type}]',
  `is_boss_floor` BOOLEAN DEFAULT FALSE COMMENT '是否为Boss层',
  FOREIGN KEY (`dungeon_id`) REFERENCES `wg_dungeon_configs`(`id`) ON DELETE CASCADE,
  UNIQUE KEY uk_dungeon_floor (`dungeon_id`, `floor_number`),
  INDEX idx_dungeon_id (`dungeon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='副本楼层配置表';

-- ========================================
-- 插入初始数据 - 迷雾森林副本
-- ========================================
INSERT INTO `wg_dungeon_configs` (`id`, `name`, `area_id`, `difficulty`, `stamina_cost`, `level_requirement`, `total_floors`, `bonus_exp`, `bonus_gold`, `guaranteed_items`, `is_elite`, `sort_order`) VALUES
('mist_forest_normal', '迷雾密林', 'mist_forest', 'normal', 10, 1, 3, 50, 30, '[{"itemId": 2001, "name": "木盾碎片", "quantity": 2}]', FALSE, 1),
('mist_forest_elite', '暗影树海', 'mist_forest', 'elite', 20, 8, 4, 120, 80, '[{"itemId": 2002, "name": "精钢矿石", "quantity": 3}, {"itemId": 3001, "name": "烈焰之刃", "quantity": 1}]', TRUE, 2);

-- 迷雾密林（普通）楼层配置
INSERT INTO `wg_dungeon_floors` (`dungeon_id`, `floor_number`, `enemies`, `is_boss_floor`) VALUES
('mist_forest_normal', 1, '[{"monsterId": "slime", "level": 2, "type": "normal"}, {"monsterId": "fairy", "level": 3, "type": "normal"}]', FALSE),
('mist_forest_normal', 2, '[{"monsterId": "slime", "level": 4, "type": "normal"}, {"monsterId": "forest_spider", "level": 6, "type": "elite"}]', FALSE),
('mist_forest_normal', 3, '[{"monsterId": "ancient_tree_guardian", "level": 10, "type": "boss"}]', TRUE);

-- 暗影树海（精英）楼层配置
INSERT INTO `wg_dungeon_floors` (`dungeon_id`, `floor_number`, `enemies`, `is_boss_floor`) VALUES
('mist_forest_elite', 1, '[{"monsterId": "slime", "level": 8, "type": "normal"}, {"monsterId": "fairy", "level": 9, "type": "normal"}]', FALSE),
('mist_forest_elite', 2, '[{"monsterId": "forest_spider", "level": 12, "type": "elite"}, {"monsterId": "slime", "level": 10, "type": "normal"}]', FALSE),
('mist_forest_elite', 3, '[{"monsterId": "fairy", "level": 13, "type": "normal"}, {"monsterId": "forest_spider", "level": 14, "type": "elite"}]', FALSE),
('mist_forest_elite', 4, '[{"monsterId": "ancient_tree_guardian", "level": 18, "type": "boss"}]', TRUE);

-- ========================================
-- 插入初始数据 - 白骨荒野副本
-- ========================================
INSERT INTO `wg_dungeon_configs` (`id`, `name`, `area_id`, `difficulty`, `stamina_cost`, `level_requirement`, `total_floors`, `bonus_exp`, `bonus_gold`, `guaranteed_items`, `is_elite`, `sort_order`) VALUES
('bone_wasteland_normal', '白骨荒野', 'bone_wasteland', 'normal', 10, 5, 3, 80, 50, '[{"itemId": 2001, "name": "铁矿石", "quantity": 3}]', FALSE, 3),
('bone_wasteland_elite', '亡者峡谷', 'bone_wasteland', 'elite', 20, 12, 4, 180, 120, '[{"itemId": 2003, "name": "秘法水晶", "quantity": 2}, {"itemId": 3002, "name": "秘银头盔", "quantity": 1}]', TRUE, 4);

-- 白骨荒野（普通）楼层配置
INSERT INTO `wg_dungeon_floors` (`dungeon_id`, `floor_number`, `enemies`, `is_boss_floor`) VALUES
('bone_wasteland_normal', 1, '[{"monsterId": "skeleton", "level": 11, "type": "normal"}, {"monsterId": "zombie", "level": 12, "type": "normal"}]', FALSE),
('bone_wasteland_normal', 2, '[{"monsterId": "skeleton", "level": 14, "type": "normal"}, {"monsterId": "bone_mage", "level": 16, "type": "elite"}]', FALSE),
('bone_wasteland_normal', 3, '[{"monsterId": "skeleton_general", "level": 20, "type": "boss"}]', TRUE);

-- 亡者峡谷（精英）楼层配置
INSERT INTO `wg_dungeon_floors` (`dungeon_id`, `floor_number`, `enemies`, `is_boss_floor`) VALUES
('bone_wasteland_elite', 1, '[{"monsterId": "skeleton", "level": 15, "type": "normal"}, {"monsterId": "zombie", "level": 16, "type": "normal"}]', FALSE),
('bone_wasteland_elite', 2, '[{"monsterId": "bone_mage", "level": 19, "type": "elite"}, {"monsterId": "skeleton", "level": 17, "type": "normal"}]', FALSE),
('bone_wasteland_elite', 3, '[{"monsterId": "zombie", "level": 20, "type": "normal"}, {"monsterId": "bone_mage", "level": 21, "type": "elite"}]', FALSE),
('bone_wasteland_elite', 4, '[{"monsterId": "skeleton_general", "level": 28, "type": "boss"}]', TRUE);

-- ========================================
-- 插入初始数据 - 火焰山谷副本
-- ========================================
INSERT INTO `wg_dungeon_configs` (`id`, `name`, `area_id`, `difficulty`, `stamina_cost`, `level_requirement`, `total_floors`, `bonus_exp`, `bonus_gold`, `guaranteed_items`, `is_elite`, `sort_order`) VALUES
('fire_valley_normal', '烈焰山脊', 'fire_valley', 'normal', 10, 10, 3, 120, 80, '[{"itemId": 2002, "name": "精钢矿石", "quantity": 4}]', FALSE, 5),
('fire_valley_elite', '熔岩深渊', 'fire_valley', 'elite', 20, 18, 4, 250, 180, '[{"itemId": 2004, "name": "龙鳞碎片", "quantity": 2}, {"itemId": 3005, "name": "灵巧之戒", "quantity": 1}]', TRUE, 6);

-- 烈焰山脊（普通）楼层配置
INSERT INTO `wg_dungeon_floors` (`dungeon_id`, `floor_number`, `enemies`, `is_boss_floor`) VALUES
('fire_valley_normal', 1, '[{"monsterId": "fire_elemental", "level": 21, "type": "normal"}, {"monsterId": "flame_demon", "level": 22, "type": "normal"}]', FALSE),
('fire_valley_normal', 2, '[{"monsterId": "fire_elemental", "level": 24, "type": "normal"}, {"monsterId": "lava_golem", "level": 26, "type": "elite"}]', FALSE),
('fire_valley_normal', 3, '[{"monsterId": "inferno_lord", "level": 30, "type": "boss"}]', TRUE);

-- 熔岩深渊（精英）楼层配置
INSERT INTO `wg_dungeon_floors` (`dungeon_id`, `floor_number`, `enemies`, `is_boss_floor`) VALUES
('fire_valley_elite', 1, '[{"monsterId": "fire_elemental", "level": 25, "type": "normal"}, {"monsterId": "flame_demon", "level": 26, "type": "normal"}]', FALSE),
('fire_valley_elite', 2, '[{"monsterId": "lava_golem", "level": 29, "type": "elite"}, {"monsterId": "fire_elemental", "level": 27, "type": "normal"}]', FALSE),
('fire_valley_elite', 3, '[{"monsterId": "flame_demon", "level": 30, "type": "normal"}, {"monsterId": "lava_golem", "level": 31, "type": "elite"}]', FALSE),
('fire_valley_elite', 4, '[{"monsterId": "inferno_lord", "level": 38, "type": "boss"}]', TRUE);

-- ========================================
-- 插入初始数据 - 冰霜雪原副本
-- ========================================
INSERT INTO `wg_dungeon_configs` (`id`, `name`, `area_id`, `difficulty`, `stamina_cost`, `level_requirement`, `total_floors`, `bonus_exp`, `bonus_gold`, `guaranteed_items`, `is_elite`, `sort_order`) VALUES
('frost_snowfield_normal', '冰霜之巅', 'frost_snowfield', 'normal', 10, 15, 3, 160, 120, '[{"itemId": 2003, "name": "秘法水晶", "quantity": 3}]', FALSE, 7),
('frost_snowfield_elite', '极寒冰狱', 'frost_snowfield', 'elite', 20, 25, 5, 350, 250, '[{"itemId": 2005, "name": "战宠进化石", "quantity": 2}, {"itemId": 3004, "name": "疾风护腿", "quantity": 1}]', TRUE, 8);

-- 冰霜之巅（普通）楼层配置
INSERT INTO `wg_dungeon_floors` (`dungeon_id`, `floor_number`, `enemies`, `is_boss_floor`) VALUES
('frost_snowfield_normal', 1, '[{"monsterId": "ice_giant", "level": 31, "type": "normal"}, {"monsterId": "snow_wolf", "level": 32, "type": "normal"}]', FALSE),
('frost_snowfield_normal', 2, '[{"monsterId": "ice_giant", "level": 34, "type": "normal"}, {"monsterId": "frost_witch", "level": 36, "type": "elite"}]', FALSE),
('frost_snowfield_normal', 3, '[{"monsterId": "frost_dragon", "level": 40, "type": "boss"}]', TRUE);

-- 极寒冰狱（精英）楼层配置
INSERT INTO `wg_dungeon_floors` (`dungeon_id`, `floor_number`, `enemies`, `is_boss_floor`) VALUES
('frost_snowfield_elite', 1, '[{"monsterId": "ice_giant", "level": 35, "type": "normal"}, {"monsterId": "snow_wolf", "level": 36, "type": "normal"}]', FALSE),
('frost_snowfield_elite', 2, '[{"monsterId": "frost_witch", "level": 39, "type": "elite"}, {"monsterId": "ice_giant", "level": 37, "type": "normal"}]', FALSE),
('frost_snowfield_elite', 3, '[{"monsterId": "snow_wolf", "level": 40, "type": "normal"}, {"monsterId": "frost_witch", "level": 41, "type": "elite"}]', FALSE),
('frost_snowfield_elite', 4, '[{"monsterId": "ice_giant", "level": 42, "type": "normal"}, {"monsterId": "snow_wolf", "level": 43, "type": "normal"}]', FALSE),
('frost_snowfield_elite', 5, '[{"monsterId": "frost_dragon", "level": 50, "type": "boss"}]', TRUE);
