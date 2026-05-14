-- 副本掉落系统 - 掉落表配置
-- 版本: V3.9.2_25__webgame_dungeon_drops.sql
-- 描述: 创建副本掉落表、掉落条目相关表

-- ========================================
-- 1. 副本掉落表 (wg_drop_tables)
-- ========================================
CREATE TABLE IF NOT EXISTS `wg_drop_tables` (
  `dungeon_id` VARCHAR(64) PRIMARY KEY COMMENT '副本ID',
  `drop_rate_multiplier` DECIMAL(3,2) DEFAULT 1.00 COMMENT '掉落率倍率（普通=1.0, 精英=1.5）',
  FOREIGN KEY (`dungeon_id`) REFERENCES `wg_dungeon_configs`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='副本掉落表';

-- ========================================
-- 2. 掉落条目表 (wg_drop_entries)
-- ========================================
CREATE TABLE IF NOT EXISTS `wg_drop_entries` (
  `id` SERIAL PRIMARY KEY COMMENT '自增主键',
  `dungeon_id` VARCHAR(64) NOT NULL COMMENT '所属副本ID',
  `floor_type` ENUM('floor', 'boss') NOT NULL COMMENT '楼层类型（普通层/Boss层）',
  `item_id` INTEGER NOT NULL COMMENT '物品ID（关联wg_item_template）',
  `drop_rate` DECIMAL(4,3) NOT NULL COMMENT '基础掉落率（0.000-1.000）',
  `min_quantity` INTEGER NOT NULL DEFAULT 1 COMMENT '最小数量',
  `max_quantity` INTEGER NOT NULL DEFAULT 1 COMMENT '最大数量',
  `elite_only` BOOLEAN DEFAULT FALSE COMMENT '是否为精英副本专属掉落',
  FOREIGN KEY (`dungeon_id`) REFERENCES `wg_dungeon_configs`(`id`) ON DELETE CASCADE,
  INDEX idx_dungeon_floor_type (`dungeon_id`, `floor_type`),
  INDEX idx_elite_only (`elite_only`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='掉落条目表';

-- ========================================
-- 插入初始数据 - 迷雾森林副本掉落表
-- ========================================
INSERT INTO `wg_drop_tables` (`dungeon_id`, `drop_rate_multiplier`) VALUES
('mist_forest_normal', 1.00),
('mist_forest_elite', 1.50);

-- 迷雾密林（普通）普通层掉落
INSERT INTO `wg_drop_entries` (`dungeon_id`, `floor_type`, `item_id`, `drop_rate`, `min_quantity`, `max_quantity`) VALUES
('mist_forest_normal', 'floor', 2001, 0.150, 1, 2),
('mist_forest_normal', 'floor', 2002, 0.080, 1, 1);

-- 迷雾密林（普通）Boss层掉落
INSERT INTO `wg_drop_entries` (`dungeon_id`, `floor_type`, `item_id`, `drop_rate`, `min_quantity`, `max_quantity`) VALUES
('mist_forest_normal', 'boss', 2001, 0.300, 2, 3),
('mist_forest_normal', 'boss', 2002, 0.150, 1, 2),
('mist_forest_normal', 'boss', 3001, 0.100, 1, 1);

-- 暗影树海（精英）普通层掉落
INSERT INTO `wg_drop_entries` (`dungeon_id`, `floor_type`, `item_id`, `drop_rate`, `min_quantity`, `max_quantity`) VALUES
('mist_forest_elite', 'floor', 2001, 0.250, 2, 3),
('mist_forest_elite', 'floor', 2002, 0.150, 1, 2),
('mist_forest_elite', 'floor', 2006, 0.200, 1, 2);

-- 暗影树海（精英）Boss层掉落
INSERT INTO `wg_drop_entries` (`dungeon_id`, `floor_type`, `item_id`, `drop_rate`, `min_quantity`, `max_quantity`, `elite_only`) VALUES
('mist_forest_elite', 'boss', 2001, 0.400, 3, 5),
('mist_forest_elite', 'boss', 2002, 0.250, 2, 3),
('mist_forest_elite', 'boss', 2006, 0.200, 2, 3),
('mist_forest_elite', 'boss', 3001, 0.150, 1, 1),
('mist_forest_elite', 'boss', 3006, 0.080, 1, 1, TRUE);

-- ========================================
-- 插入初始数据 - 白骨荒野副本掉落表
-- ========================================
INSERT INTO `wg_drop_tables` (`dungeon_id`, `drop_rate_multiplier`) VALUES
('bone_wasteland_normal', 1.00),
('bone_wasteland_elite', 1.50);

-- 白骨荒野（普通）普通层掉落
INSERT INTO `wg_drop_entries` (`dungeon_id`, `floor_type`, `item_id`, `drop_rate`, `min_quantity`, `max_quantity`) VALUES
('bone_wasteland_normal', 'floor', 2001, 0.180, 1, 2),
('bone_wasteland_normal', 'floor', 2002, 0.100, 1, 1);

-- 白骨荒野（普通）Boss层掉落
INSERT INTO `wg_drop_entries` (`dungeon_id`, `floor_type`, `item_id`, `drop_rate`, `min_quantity`, `max_quantity`) VALUES
('bone_wasteland_normal', 'boss', 2001, 0.350, 2, 4),
('bone_wasteland_normal', 'boss', 2002, 0.200, 1, 2),
('bone_wasteland_normal', 'boss', 3002, 0.120, 1, 1),
('bone_wasteland_normal', 'boss', 3003, 0.050, 1, 1);

-- 亡者峡谷（精英）普通层掉落
INSERT INTO `wg_drop_entries` (`dungeon_id`, `floor_type`, `item_id`, `drop_rate`, `min_quantity`, `max_quantity`) VALUES
('bone_wasteland_elite', 'floor', 2001, 0.280, 2, 3),
('bone_wasteland_elite', 'floor', 2002, 0.180, 1, 2),
('bone_wasteland_elite', 'floor', 2006, 0.220, 1, 2);

-- 亡者峡谷（精英）Boss层掉落
INSERT INTO `wg_drop_entries` (`dungeon_id`, `floor_type`, `item_id`, `drop_rate`, `min_quantity`, `max_quantity`, `elite_only`) VALUES
('bone_wasteland_elite', 'boss', 2001, 0.450, 3, 5),
('bone_wasteland_elite', 'boss', 2002, 0.300, 2, 4),
('bone_wasteland_elite', 'boss', 2006, 0.250, 2, 3),
('bone_wasteland_elite', 'boss', 3002, 0.180, 1, 1),
('bone_wasteland_elite', 'boss', 3003, 0.100, 1, 1),
('bone_wasteland_elite', 'boss', 3006, 0.080, 1, 1, TRUE),
('bone_wasteland_elite', 'boss', 3007, 0.030, 1, 1, TRUE);

-- ========================================
-- 插入初始数据 - 火焰山谷副本掉落表
-- ========================================
INSERT INTO `wg_drop_tables` (`dungeon_id`, `drop_rate_multiplier`) VALUES
('fire_valley_normal', 1.00),
('fire_valley_elite', 1.50);

-- 烈焰山脊（普通）普通层掉落
INSERT INTO `wg_drop_entries` (`dungeon_id`, `floor_type`, `item_id`, `drop_rate`, `min_quantity`, `max_quantity`) VALUES
('fire_valley_normal', 'floor', 2002, 0.200, 1, 2),
('fire_valley_normal', 'floor', 2003, 0.100, 1, 1);

-- 烈焰山脊（普通）Boss层掉落
INSERT INTO `wg_drop_entries` (`dungeon_id`, `floor_type`, `item_id`, `drop_rate`, `min_quantity`, `max_quantity`) VALUES
('fire_valley_normal', 'boss', 2002, 0.400, 2, 4),
('fire_valley_normal', 'boss', 2003, 0.250, 1, 2),
('fire_valley_normal', 'boss', 3001, 0.150, 1, 1),
('fire_valley_normal', 'boss', 3005, 0.080, 1, 1);

-- 熔岩深渊（精英）普通层掉落
INSERT INTO `wg_drop_entries` (`dungeon_id`, `floor_type`, `item_id`, `drop_rate`, `min_quantity`, `max_quantity`) VALUES
('fire_valley_elite', 'floor', 2002, 0.300, 2, 3),
('fire_valley_elite', 'floor', 2003, 0.180, 1, 2),
('fire_valley_elite', 'floor', 2006, 0.250, 1, 2);

-- 熔岩深渊（精英）Boss层掉落
INSERT INTO `wg_drop_entries` (`dungeon_id`, `floor_type`, `item_id`, `drop_rate`, `min_quantity`, `max_quantity`, `elite_only`) VALUES
('fire_valley_elite', 'boss', 2002, 0.500, 3, 5),
('fire_valley_elite', 'boss', 2003, 0.350, 2, 4),
('fire_valley_elite', 'boss', 2006, 0.250, 2, 3),
('fire_valley_elite', 'boss', 3001, 0.200, 1, 1),
('fire_valley_elite', 'boss', 3005, 0.120, 1, 1),
('fire_valley_elite', 'boss', 3007, 0.030, 1, 1, TRUE);

-- ========================================
-- 插入初始数据 - 冰霜雪原副本掉落表
-- ========================================
INSERT INTO `wg_drop_tables` (`dungeon_id`, `drop_rate_multiplier`) VALUES
('frost_snowfield_normal', 1.00),
('frost_snowfield_elite', 1.50);

-- 冰霜之巅（普通）普通层掉落
INSERT INTO `wg_drop_entries` (`dungeon_id`, `floor_type`, `item_id`, `drop_rate`, `min_quantity`, `max_quantity`) VALUES
('frost_snowfield_normal', 'floor', 2003, 0.220, 1, 2),
('frost_snowfield_normal', 'floor', 2004, 0.120, 1, 1);

-- 冰霜之巅（普通）Boss层掉落
INSERT INTO `wg_drop_entries` (`dungeon_id`, `floor_type`, `item_id`, `drop_rate`, `min_quantity`, `max_quantity`) VALUES
('frost_snowfield_normal', 'boss', 2003, 0.450, 2, 4),
('frost_snowfield_normal', 'boss', 2004, 0.300, 1, 2),
('frost_snowfield_normal', 'boss', 3005, 0.180, 1, 1);

-- 极寒冰狱（精英）普通层掉落
INSERT INTO `wg_drop_entries` (`dungeon_id`, `floor_type`, `item_id`, `drop_rate`, `min_quantity`, `max_quantity`) VALUES
('frost_snowfield_elite', 'floor', 2003, 0.320, 2, 3),
('frost_snowfield_elite', 'floor', 2004, 0.200, 1, 2),
('frost_snowfield_elite', 'floor', 2006, 0.280, 1, 2);

-- 极寒冰狱（精英）Boss层掉落
INSERT INTO `wg_drop_entries` (`dungeon_id`, `floor_type`, `item_id`, `drop_rate`, `min_quantity`, `max_quantity`, `elite_only`) VALUES
('frost_snowfield_elite', 'boss', 2003, 0.550, 3, 5),
('frost_snowfield_elite', 'boss', 2004, 0.400, 2, 4),
('frost_snowfield_elite', 'boss', 2006, 0.300, 2, 3),
('frost_snowfield_elite', 'boss', 3005, 0.220, 1, 1),
('frost_snowfield_elite', 'boss', 3006, 0.100, 1, 1, TRUE),
('frost_snowfield_elite', 'boss', 3007, 0.050, 1, 1, TRUE);
