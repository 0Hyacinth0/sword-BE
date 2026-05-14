-- 精英副本增强系统 - 精英技能组配置
-- 版本: V3.9.2_26__webgame_elite_skills.sql
-- 描述: 创建精英技能组、精英技能条目相关表

-- ========================================
-- 1. 精英技能组表 (wg_elite_skill_groups)
-- ========================================
CREATE TABLE IF NOT EXISTS `wg_elite_skill_groups` (
  `id` SERIAL PRIMARY KEY COMMENT '技能组ID',
  `name` VARCHAR(64) NOT NULL COMMENT '技能组名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='精英技能组表';

-- ========================================
-- 2. 精英技能组条目表 (wg_elite_skill_group_entries)
-- ========================================
CREATE TABLE IF NOT EXISTS `wg_elite_skill_group_entries` (
  `id` SERIAL PRIMARY KEY COMMENT '自增ID',
  `group_id` BIGINT UNSIGNED NOT NULL COMMENT '技能组ID',
  `skill_id` INT NOT NULL COMMENT '技能ID（关联skills表）',
  `priority` INT DEFAULT 0 COMMENT 'AI使用优先级',
  FOREIGN KEY (`group_id`) REFERENCES `wg_elite_skill_groups`(`id`) ON DELETE CASCADE,
  INDEX idx_group_id (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='精英技能组条目表';

-- ========================================
-- 插入初始数据 - 精英技能组
-- ========================================
INSERT INTO `wg_elite_skill_groups` (`id`, `name`) VALUES
(1, '精英怪物通用技能组');

-- ========================================
-- 插入初始数据 - 精英技能组条目
-- 技能 ID 说明：
-- 9010: 横扫 (active_attack, power=130, cooldown=3)
-- 9011: 战意高昂 (active_buff, cooldown=4)
-- 9012: 毒雾 (active_attack, power=80, cooldown=3)
-- ========================================
INSERT INTO `wg_elite_skill_group_entries` (`group_id`, `skill_id`, `priority`) VALUES
(1, 9010, 1),
(1, 9011, 2),
(1, 9012, 3);
