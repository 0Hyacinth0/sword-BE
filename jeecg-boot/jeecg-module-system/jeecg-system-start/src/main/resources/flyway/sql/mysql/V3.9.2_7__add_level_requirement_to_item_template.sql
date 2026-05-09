-- ========================================
-- 装备系统完善 - 添加等级需求字段
-- 版本: V3.9.2_7
-- 日期: 2026-05-09
-- ========================================

-- 1. 为 wg_item_template 表添加 level_requirement 字段
ALTER TABLE `wg_item_template` 
ADD COLUMN `level_requirement` INT DEFAULT 1 COMMENT '等级需求' AFTER `description`;

-- 2. 更新现有装备数据的等级需求（示例数据）
UPDATE `wg_item_template` SET `level_requirement` = 1 WHERE `item_id` LIKE 'equip-%' AND `type` = 1;
