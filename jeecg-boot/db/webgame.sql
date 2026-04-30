-- ----------------------------
-- 网页小游戏数据库表结构
-- ----------------------------

-- ----------------------------
-- Table structure for wg_user (游戏用户表)
-- ----------------------------
DROP TABLE IF EXISTS `wg_user`;
CREATE TABLE `wg_user` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码(加密存储)',
  `salt` varchar(20) DEFAULT NULL COMMENT '盐值',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `role_id` varchar(36) DEFAULT NULL COMMENT '角色ID',
  `level` int DEFAULT 1 COMMENT '等级',
  `experience` bigint DEFAULT 0 COMMENT '经验值',
  `gold` bigint DEFAULT 1000 COMMENT '金币',
  `diamond` int DEFAULT 100 COMMENT '钻石',
  `stamina` int DEFAULT 100 COMMENT '体力值',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` int DEFAULT 0 COMMENT '删除状态(0-正常,1-已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游戏用户表';

-- ----------------------------
-- Table structure for wg_character (游戏角色表)
-- ----------------------------
DROP TABLE IF EXISTS `wg_character`;
CREATE TABLE `wg_character` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `user_id` varchar(36) NOT NULL COMMENT '用户ID',
  `character_name` varchar(50) NOT NULL COMMENT '角色名称',
  `profession` int NOT NULL COMMENT '职业类型(1-战士,2-法师,3-猎人)',
  `level` int DEFAULT 1 COMMENT '等级',
  `experience` bigint DEFAULT 0 COMMENT '经验值',
  `strength` int DEFAULT 0 COMMENT '力量',
  `intelligence` int DEFAULT 0 COMMENT '智力',
  `agility` int DEFAULT 0 COMMENT '敏捷',
  `hp` int DEFAULT 100 COMMENT '生命值',
  `mp` int DEFAULT 50 COMMENT '魔法值',
  `physical_attack` int DEFAULT 10 COMMENT '物理攻击力',
  `magic_attack` int DEFAULT 0 COMMENT '魔法攻击力',
  `defense` int DEFAULT 5 COMMENT '防御力',
  `dodge_rate` double DEFAULT 0 COMMENT '闪避率',
  `critical_rate` double DEFAULT 0 COMMENT '暴击率',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` int DEFAULT 0 COMMENT '删除状态(0-正常,1-已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='游戏角色表';

-- ----------------------------
-- Table structure for wg_item_template (物品模板表)
-- ----------------------------
DROP TABLE IF EXISTS `wg_item_template`;
CREATE TABLE `wg_item_template` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `item_name` varchar(100) NOT NULL COMMENT '物品名称',
  `item_type` int NOT NULL COMMENT '物品类型(1-装备,2-消耗品,3-材料)',
  `equip_slot` int DEFAULT NULL COMMENT '装备部位(1-武器,2-头部,3-胸部,4-腿部,5-饰品)',
  `quality` int DEFAULT 1 COMMENT '品质(1-普通白色,2-稀有蓝色,3-史诗紫色,4-传说橙色)',
  `base_stat_percent` double DEFAULT 1.0 COMMENT '基础属性加成百分比',
  `affix_count` int DEFAULT 0 COMMENT '附加词条数',
  `has_special_passive` int DEFAULT 0 COMMENT '是否特殊被动',
  `description` text COMMENT '物品描述',
  `icon_url` varchar(255) DEFAULT NULL COMMENT '图标URL',
  `sell_price` int DEFAULT 0 COMMENT '出售价格',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_item_type` (`item_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物品模板表';

-- ----------------------------
-- Table structure for wg_player_item (玩家背包表)
-- ----------------------------
DROP TABLE IF EXISTS `wg_player_item`;
CREATE TABLE `wg_player_item` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `user_id` varchar(36) NOT NULL COMMENT '用户ID',
  `item_template_id` varchar(36) NOT NULL COMMENT '物品模板ID',
  `item_name` varchar(100) DEFAULT NULL COMMENT '物品名称(冗余字段,方便查询)',
  `quantity` int DEFAULT 1 COMMENT '数量',
  `is_equipped` int DEFAULT 0 COMMENT '是否装备中(0-未装备,1-已装备)',
  `enhance_level` int DEFAULT 0 COMMENT '强化等级',
  `affixes` text COMMENT '附加属性JSON',
  `set_id` varchar(36) DEFAULT NULL COMMENT '套装ID',
  `obtain_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '获得时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_equipped` (`is_equipped`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='玩家背包表';

-- ----------------------------
-- Table structure for wg_monster (怪物表)
-- ----------------------------
DROP TABLE IF EXISTS `wg_monster`;
CREATE TABLE `wg_monster` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `monster_name` varchar(100) NOT NULL COMMENT '怪物名称',
  `area` varchar(50) DEFAULT NULL COMMENT '所属区域',
  `min_level` int DEFAULT 1 COMMENT '最低等级',
  `max_level` int DEFAULT 1 COMMENT '最高等级',
  `hp` int DEFAULT 100 COMMENT '生命值',
  `physical_attack` int DEFAULT 10 COMMENT '物理攻击力',
  `magic_attack` int DEFAULT 0 COMMENT '魔法攻击力',
  `defense` int DEFAULT 5 COMMENT '防御力',
  `exp_reward` int DEFAULT 10 COMMENT '经验值奖励',
  `gold_reward` int DEFAULT 5 COMMENT '金币奖励',
  `drop_items` text COMMENT '掉落物品ID列表(JSON格式)',
  `is_boss` int DEFAULT 0 COMMENT '是否BOSS(0-普通,1-BOSS)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_area` (`area`),
  KEY `idx_level` (`min_level`, `max_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='怪物表';

-- ----------------------------
-- Table structure for wg_battle_log (战斗记录表)
-- ----------------------------
DROP TABLE IF EXISTS `wg_battle_log`;
CREATE TABLE `wg_battle_log` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `user_id` varchar(36) NOT NULL COMMENT '用户ID',
  `character_id` varchar(36) NOT NULL COMMENT '角色ID',
  `battle_type` int NOT NULL COMMENT '战斗类型(1-PVE,2-PVP,3-副本)',
  `monster_id` varchar(36) DEFAULT NULL COMMENT '怪物ID(PVE时使用)',
  `opponent_id` varchar(36) DEFAULT NULL COMMENT '对手玩家ID(PVP时使用)',
  `dungeon_id` varchar(36) DEFAULT NULL COMMENT '副本ID(副本时使用)',
  `result` int DEFAULT NULL COMMENT '战斗结果(1-胜利,2-失败,3-平局)',
  `exp_gained` int DEFAULT 0 COMMENT '获得经验值',
  `gold_gained` int DEFAULT 0 COMMENT '获得金币',
  `drop_items` text COMMENT '掉落物品ID列表(JSON格式)',
  `rounds` int DEFAULT 0 COMMENT '战斗回合数',
  `battle_detail` text COMMENT '战斗详情JSON',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='战斗记录表';

-- ----------------------------
-- Table structure for wg_dungeon (副本表)
-- ----------------------------
DROP TABLE IF EXISTS `wg_dungeon`;
CREATE TABLE `wg_dungeon` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `dungeon_name` varchar(100) NOT NULL COMMENT '副本名称',
  `dungeon_type` int NOT NULL COMMENT '副本类型(1-单人,2-多人)',
  `min_level` int DEFAULT 1 COMMENT '最低等级要求',
  `recommended_power` int DEFAULT 0 COMMENT '推荐战斗力',
  `max_players` int DEFAULT 1 COMMENT '最大人数',
  `stamina_cost` int DEFAULT 10 COMMENT '体力消耗',
  `monsters` text COMMENT '怪物列表(JSON格式)',
  `rewards` text COMMENT '奖励物品列表(JSON格式)',
  `description` text COMMENT '副本描述',
  `is_enabled` int DEFAULT 1 COMMENT '是否启用(0-禁用,1-启用)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='副本表';

-- ----------------------------
-- Table structure for wg_dungeon_room (副本房间表)
-- ----------------------------
DROP TABLE IF EXISTS `wg_dungeon_room`;
CREATE TABLE `wg_dungeon_room` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `dungeon_id` varchar(36) NOT NULL COMMENT '副本ID',
  `host_user_id` varchar(36) NOT NULL COMMENT '房主用户ID',
  `room_name` varchar(100) DEFAULT NULL COMMENT '房间名称',
  `current_players` int DEFAULT 1 COMMENT '当前人数',
  `max_players` int DEFAULT 1 COMMENT '最大人数',
  `status` int DEFAULT 1 COMMENT '房间状态(1-等待中,2-进行中,3-已结束)',
  `players` text COMMENT '玩家列表(JSON格式)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`),
  KEY `idx_dungeon_id` (`dungeon_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='副本房间表';

-- ----------------------------
-- Table structure for wg_pvp_match (PVP对战记录表)
-- ----------------------------
DROP TABLE IF EXISTS `wg_pvp_match`;
CREATE TABLE `wg_pvp_match` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `player1_user_id` varchar(36) NOT NULL COMMENT '玩家1用户ID',
  `player1_character_id` varchar(36) NOT NULL COMMENT '玩家1角色ID',
  `player2_user_id` varchar(36) NOT NULL COMMENT '玩家2用户ID',
  `player2_character_id` varchar(36) NOT NULL COMMENT '玩家2角色ID',
  `winner_user_id` varchar(36) DEFAULT NULL COMMENT '胜利者用户ID',
  `result` int DEFAULT NULL COMMENT '对战结果(1-玩家1胜利,2-玩家2胜利,3-平局)',
  `rounds` int DEFAULT 0 COMMENT '战斗回合数',
  `battle_detail` text COMMENT '战斗详情JSON',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`),
  KEY `idx_player1` (`player1_user_id`),
  KEY `idx_player2` (`player2_user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='PVP对战记录表';

-- ----------------------------
-- Table structure for wg_pvp_ranking (PVP积分排行榜表)
-- ----------------------------
DROP TABLE IF EXISTS `wg_pvp_ranking`;
CREATE TABLE `wg_pvp_ranking` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `user_id` varchar(36) NOT NULL COMMENT '用户ID',
  `character_id` varchar(36) NOT NULL COMMENT '角色ID',
  `rating` int DEFAULT 1000 COMMENT '当前积分',
  `max_rating` int DEFAULT 1000 COMMENT '历史最高积分',
  `wins` int DEFAULT 0 COMMENT '胜利场次',
  `losses` int DEFAULT 0 COMMENT '失败场次',
  `draws` int DEFAULT 0 COMMENT '平局场次',
  `total_matches` int DEFAULT 0 COMMENT '总场次',
  `current_rank` int DEFAULT NULL COMMENT '当前排名',
  `season` varchar(50) DEFAULT NULL COMMENT '赛季',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_season` (`user_id`, `season`),
  KEY `idx_rating` (`rating`),
  KEY `idx_rank` (`current_rank`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='PVP积分排行榜表';

-- ----------------------------
-- Table structure for wg_friend (好友关系表)
-- ----------------------------
DROP TABLE IF EXISTS `wg_friend`;
CREATE TABLE `wg_friend` (
  `id` varchar(36) NOT NULL COMMENT '主键ID',
  `user_id` varchar(36) NOT NULL COMMENT '用户ID',
  `friend_user_id` varchar(36) NOT NULL COMMENT '好友用户ID',
  `remark_name` varchar(50) DEFAULT NULL COMMENT '备注名称',
  `status` int DEFAULT 1 COMMENT '好友状态(1-正常,2-拉黑)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_friend` (`user_id`, `friend_user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='好友关系表';
