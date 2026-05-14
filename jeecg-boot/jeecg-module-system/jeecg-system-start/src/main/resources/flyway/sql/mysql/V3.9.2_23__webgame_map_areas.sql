-- 世界地图区域系统 - 地图区域配置表
-- 版本: V3.9.2_23__webgame_map_areas.sql
-- 描述: 创建地图区域、区域怪物、区域掉落相关表

-- ========================================
-- 1. 地图区域配置表 (wg_map_areas)
-- ========================================
CREATE TABLE IF NOT EXISTS `wg_map_areas` (
  `id` VARCHAR(32) PRIMARY KEY COMMENT '区域ID',
  `name` VARCHAR(50) NOT NULL COMMENT '区域名称',
  `icon` VARCHAR(10) DEFAULT NULL COMMENT 'emoji图标',
  `level_min` INTEGER NOT NULL COMMENT '等级范围最小值',
  `level_max` INTEGER NOT NULL COMMENT '等级范围最大值',
  `description` TEXT COMMENT '区域描述',
  `unlock_level` INTEGER NOT NULL COMMENT '解锁所需等级',
  `sort_order` INTEGER DEFAULT 0 COMMENT '排序顺序',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  INDEX idx_sort_order (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='地图区域配置表';

-- ========================================
-- 2. 区域怪物配置表 (wg_area_monsters)
-- ========================================
CREATE TABLE IF NOT EXISTS `wg_area_monsters` (
  `id` VARCHAR(32) PRIMARY KEY COMMENT '怪物配置ID',
  `area_id` VARCHAR(32) NOT NULL COMMENT '所属区域ID',
  `monster_template_id` VARCHAR(32) DEFAULT NULL COMMENT '关联怪物模板ID（可选）',
  `name` VARCHAR(50) NOT NULL COMMENT '怪物名称',
  `level` INTEGER NOT NULL COMMENT '怪物等级',
  `type` ENUM('normal', 'elite', 'boss') NOT NULL COMMENT '怪物类型',
  `sort_order` INTEGER DEFAULT 0 COMMENT '排序顺序',
  FOREIGN KEY (`area_id`) REFERENCES `wg_map_areas`(`id`) ON DELETE CASCADE,
  INDEX idx_area_id (`area_id`),
  INDEX idx_sort_order (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='区域怪物配置表';

-- ========================================
-- 3. 区域掉落配置表 (wg_area_drops)
-- ========================================
CREATE TABLE IF NOT EXISTS `wg_area_drops` (
  `id` SERIAL PRIMARY KEY COMMENT '自增ID',
  `area_id` VARCHAR(32) NOT NULL COMMENT '所属区域ID',
  `item_id` INTEGER NOT NULL COMMENT '物品ID（关联wg_item_template）',
  `rarity` ENUM('Rare', 'Epic', 'Legendary') NOT NULL COMMENT '稀有度',
  `drop_rate` DECIMAL(5,4) DEFAULT 0.1000 COMMENT '基础掉落率',
  FOREIGN KEY (`area_id`) REFERENCES `wg_map_areas`(`id`) ON DELETE CASCADE,
  INDEX idx_area_id (`area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='区域掉落配置表';

-- ========================================
-- 插入初始数据 - 4个区域配置
-- ========================================
INSERT INTO `wg_map_areas` (`id`, `name`, `icon`, `level_min`, `level_max`, `description`, `unlock_level`, `sort_order`) VALUES
('mist_forest', '迷雾森林', '🌲', 1, 10, '笼罩在浓雾中的古老森林，栖息着各种低阶魔物。', 1, 1),
('bone_wasteland', '白骨荒野', '💀', 11, 20, '被亡灵气息笼罩的荒芜之地，到处是骷髅和僵尸。', 11, 2),
('fire_valley', '火焰山谷', '🔥', 21, 30, '炽热的火山地带，火元素和炎魔在此游荡。', 21, 3),
('frost_snowfield', '冰霜雪原', '❄️', 31, 40, '终年积雪的寒冷高原，冰巨人和霜龙统治此地。', 31, 4);

-- ========================================
-- 插入初始数据 - 迷雾森林怪物
-- ========================================
INSERT INTO `wg_area_monsters` (`id`, `area_id`, `name`, `level`, `type`, `sort_order`) VALUES
('slime', 'mist_forest', '史莱姆', 1, 'normal', 1),
('fairy', 'mist_forest', '小精灵', 3, 'normal', 2),
('forest_spider', 'mist_forest', '森林蜘蛛', 7, 'elite', 3),
('ancient_tree_guardian', 'mist_forest', '古树守卫', 10, 'boss', 4);

-- ========================================
-- 插入初始数据 - 白骨荒野怪物
-- ========================================
INSERT INTO `wg_area_monsters` (`id`, `area_id`, `name`, `level`, `type`, `sort_order`) VALUES
('skeleton', 'bone_wasteland', '骷髅战士', 11, 'normal', 1),
('zombie', 'bone_wasteland', '僵尸', 13, 'normal', 2),
('bone_mage', 'bone_wasteland', '骨法师', 17, 'elite', 3),
('skeleton_general', 'bone_wasteland', '骷髅将军', 20, 'boss', 4);

-- ========================================
-- 插入初始数据 - 火焰山谷怪物
-- ========================================
INSERT INTO `wg_area_monsters` (`id`, `area_id`, `name`, `level`, `type`, `sort_order`) VALUES
('fire_elemental', 'fire_valley', '火元素', 21, 'normal', 1),
('flame_demon', 'fire_valley', '炎魔', 23, 'normal', 2),
('lava_golem', 'fire_valley', '熔岩石魔', 27, 'elite', 3),
('inferno_lord', 'fire_valley', '炼狱领主', 30, 'boss', 4);

-- ========================================
-- 插入初始数据 - 冰霜雪原怪物
-- ========================================
INSERT INTO `wg_area_monsters` (`id`, `area_id`, `name`, `level`, `type`, `sort_order`) VALUES
('ice_giant', 'frost_snowfield', '冰巨人', 31, 'normal', 1),
('snow_wolf', 'frost_snowfield', '雪狼', 33, 'normal', 2),
('frost_witch', 'frost_snowfield', '冰霜女巫', 37, 'elite', 3),
('frost_dragon', 'frost_snowfield', '霜龙', 40, 'boss', 4);

-- ========================================
-- 插入初始数据 - 迷雾森林掉落
-- ========================================
INSERT INTO `wg_area_drops` (`area_id`, `item_id`, `rarity`, `drop_rate`) VALUES
('mist_forest', 2001, 'Rare', 0.2000),
('mist_forest', 2002, 'Rare', 0.1500),
('mist_forest', 3001, 'Epic', 0.0500);

-- ========================================
-- 插入初始数据 - 白骨荒野掉落
-- ========================================
INSERT INTO `wg_area_drops` (`area_id`, `item_id`, `rarity`, `drop_rate`) VALUES
('bone_wasteland', 2001, 'Rare', 0.2000),
('bone_wasteland', 2002, 'Rare', 0.1500),
('bone_wasteland', 3002, 'Epic', 0.0500),
('bone_wasteland', 3003, 'Legendary', 0.0200);

-- ========================================
-- 插入初始数据 - 火焰山谷掉落
-- ========================================
INSERT INTO `wg_area_drops` (`area_id`, `item_id`, `rarity`, `drop_rate`) VALUES
('fire_valley', 2002, 'Rare', 0.2000),
('fire_valley', 2003, 'Epic', 0.1000),
('fire_valley', 3001, 'Epic', 0.0500),
('fire_valley', 3005, 'Legendary', 0.0200);

-- ========================================
-- 插入初始数据 - 冰霜雪原掉落
-- ========================================
INSERT INTO `wg_area_drops` (`area_id`, `item_id`, `rarity`, `drop_rate`) VALUES
('frost_snowfield', 2003, 'Epic', 0.1500),
('frost_snowfield', 2004, 'Epic', 0.1000),
('frost_snowfield', 3005, 'Legendary', 0.0300);
