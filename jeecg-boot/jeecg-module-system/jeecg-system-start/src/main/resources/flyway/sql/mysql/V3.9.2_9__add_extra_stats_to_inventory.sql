-- ============================================
-- WebGame 背包表添加额外属性字段
-- 版本: V3.9.2_9
-- 日期: 2026-05-09
-- ============================================

-- 检查字段是否存在，如果不存在则添加
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
