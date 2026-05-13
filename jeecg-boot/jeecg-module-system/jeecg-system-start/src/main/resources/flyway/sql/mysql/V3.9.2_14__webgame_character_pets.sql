-- ============================================
-- WebGame 战宠系统 - 角色战宠实例表
-- 用于存储角色拥有的战宠实例数据
-- ============================================

CREATE TABLE IF NOT EXISTS `wg_character_pets` (
  `id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '战宠实例唯一标识(UUID)',
  `character_id` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色ID',
  `pet_type_id` INT NOT NULL COMMENT '战宠类型ID',
  `nickname` VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '战宠昵称',
  `level` INT NOT NULL DEFAULT 1 COMMENT '等级',
  `exp` INT NOT NULL DEFAULT 0 COMMENT '当前经验值',
  `max_exp` INT NOT NULL DEFAULT 100 COMMENT '升级所需经验值',
  `rarity` TINYINT NOT NULL COMMENT '稀有度(1-N,2-R,3-SR,4-SSR)',
  `is_active` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否出战(0-否,1-是)',
  
  -- 技能槽位(最多3个)
  `skill_slot_1` INT DEFAULT NULL COMMENT '技能槽位1(关联wg_pet_skills.skill_id)',
  `skill_slot_2` INT DEFAULT NULL COMMENT '技能槽位2(关联wg_pet_skills.skill_id)',
  `skill_slot_3` INT DEFAULT NULL COMMENT '技能槽位3(关联wg_pet_skills.skill_id)',
  
  -- 装备槽位(护甲和饰品)
  `equip_armor` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '护甲装备ID(关联wg_character_items.id)',
  `equip_accessory` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '饰品装备ID(关联wg_character_items.id)',
  
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_character_id` (`character_id`) USING BTREE COMMENT '角色ID索引',
  KEY `idx_pet_type_id` (`pet_type_id`) USING BTREE COMMENT '战宠类型索引',
  KEY `idx_is_active` (`is_active`) USING BTREE COMMENT '出战状态索引',
  CONSTRAINT `fk_pet_character` FOREIGN KEY (`character_id`) REFERENCES `wg_character` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_pet_type` FOREIGN KEY (`pet_type_id`) REFERENCES `wg_pet_types` (`pet_type_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_pet_skill_1` FOREIGN KEY (`skill_slot_1`) REFERENCES `wg_pet_skills` (`skill_id`) ON DELETE SET NULL,
  CONSTRAINT `fk_pet_skill_2` FOREIGN KEY (`skill_slot_2`) REFERENCES `wg_pet_skills` (`skill_id`) ON DELETE SET NULL,
  CONSTRAINT `fk_pet_skill_3` FOREIGN KEY (`skill_slot_3`) REFERENCES `wg_pet_skills` (`skill_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色战宠实例表';
