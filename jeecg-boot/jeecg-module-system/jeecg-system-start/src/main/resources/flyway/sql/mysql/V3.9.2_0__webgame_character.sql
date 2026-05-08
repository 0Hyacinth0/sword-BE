-- ============================================
-- WebGame 角色表创建脚本
-- 执行此脚本将创建 wg_character 表
-- ============================================

-- 创建角色表
CREATE TABLE IF NOT EXISTS `wg_character` (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户ID',
  `character_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `profession` int(11) NOT NULL COMMENT '职业类型(1-战士,2-法师,3-猎人)',
  `level` int(11) NOT NULL DEFAULT 1 COMMENT '等级',
  `experience` bigint(20) NOT NULL DEFAULT 0 COMMENT '经验值',
  `strength` int(11) NOT NULL DEFAULT 0 COMMENT '力量',
  `intelligence` int(11) NOT NULL DEFAULT 0 COMMENT '智力',
  `agility` int(11) NOT NULL DEFAULT 0 COMMENT '敏捷',
  `hp` int(11) NOT NULL DEFAULT 100 COMMENT '生命值',
  `max_hp` int(11) NOT NULL DEFAULT 100 COMMENT '最大生命值',
  `mp` int(11) NOT NULL DEFAULT 50 COMMENT '魔法值',
  `max_mp` int(11) NOT NULL DEFAULT 50 COMMENT '最大魔法值',
  `physical_attack` int(11) NOT NULL DEFAULT 0 COMMENT '物理攻击力',
  `magic_attack` int(11) NOT NULL DEFAULT 0 COMMENT '魔法攻击力',
  `defense` int(11) NOT NULL DEFAULT 0 COMMENT '防御力',
  `dodge_rate` double NOT NULL DEFAULT 0 COMMENT '闪避率',
  `critical_rate` double NOT NULL DEFAULT 0 COMMENT '暴击率',
  `bonus_hp` int(11) DEFAULT 0 COMMENT '战宠加成-生命值',
  `bonus_physical_attack` int(11) DEFAULT 0 COMMENT '战宠加成-物理攻击',
  `bonus_magic_attack` int(11) DEFAULT 0 COMMENT '战宠加成-魔法攻击',
  `bonus_defense` int(11) DEFAULT 0 COMMENT '战宠加成-防御力',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` int(1) NOT NULL DEFAULT 0 COMMENT '删除状态(0-正常,1-已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE COMMENT '用户ID索引',
  KEY `idx_character_name` (`character_name`) USING BTREE COMMENT '角色名称索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏角色表';

