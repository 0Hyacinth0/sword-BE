-- ============================================
-- WebGame 战宠系统 - 战宠类型配置表
-- 用于存储所有战宠类型的静态配置信息
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_pet_types` (
  `pet_type_id` INT NOT NULL AUTO_INCREMENT COMMENT '战宠类型ID',
  `name` VARCHAR(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '战宠名称',
  `element` TINYINT NOT NULL COMMENT '元素类型(1-火,2-水,3-风,4-地,5-光,6-暗)',
  `rarity` TINYINT NOT NULL COMMENT '稀有度(1-N,2-R,3-SR,4-SSR)',
  `base_hp` INT NOT NULL DEFAULT 0 COMMENT '基础生命值',
  `base_attack` INT NOT NULL DEFAULT 0 COMMENT '基础攻击力',
  `base_defense` INT NOT NULL DEFAULT 0 COMMENT '基础防御力',
  `base_speed` INT NOT NULL DEFAULT 0 COMMENT '基础速度',
  `hp_growth` INT NOT NULL DEFAULT 0 COMMENT '生命成长值(每级增加)',
  `attack_growth` INT NOT NULL DEFAULT 0 COMMENT '攻击成长值(每级增加)',
  `defense_growth` INT NOT NULL DEFAULT 0 COMMENT '防御成长值(每级增加)',
  `speed_growth` INT NOT NULL DEFAULT 0 COMMENT '速度成长值(每级增加)',
  `description` VARCHAR(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '战宠描述',
  `evolve_to` INT DEFAULT NULL COMMENT '进化目标类型ID,null为最终形态',
  `evolve_level` INT DEFAULT NULL COMMENT '进化所需等级',
  `icon_url` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图标URL',
  `silhouette_url` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '剪影URL(未收集时显示)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`pet_type_id`) USING BTREE,
  KEY `idx_element` (`element`) USING BTREE COMMENT '元素类型索引',
  KEY `idx_rarity` (`rarity`) USING BTREE COMMENT '稀有度索引',
  KEY `idx_evolve_to` (`evolve_to`) USING BTREE COMMENT '进化目标索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='战宠类型配置表';
