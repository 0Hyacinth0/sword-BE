-- ============================================
-- WebGame 战斗系统 - 怪物模板表
-- 用于存储所有怪物的基础配置信息
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_monster_template` (
  `monster_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '怪物模板ID',
  `name` VARCHAR(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '怪物名称',
  `level` INT NOT NULL DEFAULT 1 COMMENT '怪物等级',
  `element` TINYINT DEFAULT 0 COMMENT '元素属性(0-无,1-火,2-水,3-风,4-地,5-光,6-暗)',
  
  -- 基础属性
  `base_hp` INT NOT NULL DEFAULT 100 COMMENT '基础生命值',
  `base_mp` INT NOT NULL DEFAULT 30 COMMENT '基础魔法值',
  `base_physical_attack` INT NOT NULL DEFAULT 15 COMMENT '基础物理攻击',
  `base_magic_attack` INT NOT NULL DEFAULT 5 COMMENT '基础魔法攻击',
  `base_defense` INT NOT NULL DEFAULT 8 COMMENT '基础防御力',
  `base_speed` INT NOT NULL DEFAULT 12 COMMENT '基础速度',
  `base_dodge_rate` DECIMAL(5,4) DEFAULT 0.0500 COMMENT '基础闪避率(0~1)',
  `base_critical_rate` DECIMAL(5,4) DEFAULT 0.0500 COMMENT '基础暴击率(0~1)',
  
  -- 成长系数（每级增加）
  `hp_growth` INT DEFAULT 10 COMMENT '生命成长',
  `physical_attack_growth` INT DEFAULT 2 COMMENT '物攻成长',
  `magic_attack_growth` INT DEFAULT 1 COMMENT '魔攻成长',
  `defense_growth` INT DEFAULT 1 COMMENT '防御成长',
  `speed_growth` INT DEFAULT 1 COMMENT '速度成长',
  
  -- 技能配置
  `skill_ids` JSON DEFAULT NULL COMMENT '技能ID列表(JSON数组)',
  
  -- 掉落配置
  `exp_reward` INT DEFAULT 50 COMMENT '经验奖励',
  `gold_reward` INT DEFAULT 20 COMMENT '金币奖励',
  `drop_rate` DECIMAL(5,4) DEFAULT 0.1500 COMMENT '掉落概率(0~1)',
  
  -- 其他
  `icon_url` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图标URL',
  `description` VARCHAR(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '怪物描述',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`monster_id`) USING BTREE,
  KEY `idx_level` (`level`) USING BTREE COMMENT '等级索引',
  KEY `idx_element` (`element`) USING BTREE COMMENT '元素索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='怪物模板表';
