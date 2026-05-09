-- ============================================
-- WebGame 角色表修复脚本 - 添加缺失字段
-- 执行此脚本将为 wg_character 表添加 max_hp 和 max_mp 字段
-- ============================================

-- 添加 max_hp 字段（如果不存在）
SET @dbname = DATABASE();
SET @tablename = 'wg_character';
SET @columnname = 'max_hp';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  'SELECT 1',
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN ', @columnname, ' int(11) NOT NULL DEFAULT 100 COMMENT ''最大生命值'' AFTER hp')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 添加 max_mp 字段（如果不存在）
SET @columnname = 'max_mp';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  'SELECT 1',
  CONCAT('ALTER TABLE ', @tablename, ' ADD COLUMN ', @columnname, ' int(11) NOT NULL DEFAULT 50 COMMENT ''最大魔法值'' AFTER mp')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;
