# 剑之传说 - WebGame 后端模块

## 📖 项目简介

这是一个基于 **JeecgBoot** 框架开发的回合制文字冒险网页游戏后端模块。项目采用服务器权威架构，实现了完整的 RPG 游戏功能，包括角色养成、装备系统、战斗系统、副本挑战、PVP 竞技和社交互动等核心玩法。

## ✨ 核心特性

- 🔐 **安全可靠**: JWT 认证 + 密码加密 + 服务器权威计算
- ⚔️ **回合制战斗**: PVE/PVP/副本多种战斗模式
- 🎒 **装备驱动**: 品质体系 + 套装加成 + 强化词条
- 👥 **多人互动**: 组队副本 + 实时对战 + 好友系统
- 📊 **竞技排行**: Elo 积分匹配 + 赛季排行榜
- 💾 **数据持久化**: MySQL + Redis 双层存储

## 📁 项目结构

```
网页小游戏后端/
├── jeecg-boot/                              # JeecgBoot 主项目
│   ├── db/
│   │   └── webgame.sql                     # 📄 游戏数据库脚本
│   ├── jeecg-boot-module/
│   │   └── jeecg-module-webgame/           # 🎮 WebGame 模块
│   │       ├── src/main/java/org/jeecg/modules/webgame/
│   │       │   ├── auth/                    # 认证模块 ✅
│   │       │   ├── character/               # 角色模块 🚧
│   │       │   ├── item/                    # 物品模块 🚧
│   │       │   ├── battle/                  # 战斗模块 🚧
│   │       │   ├── dungeon/                 # 副本模块 🚧
│   │       │   ├── pvp/                     # PVP模块 🚧
│   │       │   ├── social/                  # 社交模块 🚧
│   │       │   └── config/                  # 配置类 ✅
│   │       ├── pom.xml                      # Maven 配置 ✅
│   │       └── README.md                    # 模块说明 ✅
│   └── ...
├── WebGame接口文档.md                       # 📚 完整 API 文档 ✅
├── WebGame模块开发总结.md                   # 📝 开发进度总结 ✅
├── WebGame快速启动指南.md                   # 🚀 启动教程 ✅
└── README_WebGame.md                        # 📘 本文件 ✅
```

## 🗂️ 文档导航

| 文档 | 说明 | 链接 |
|------|------|------|
| 📚 API 接口文档 | 34 个接口的详细说明，包含请求/响应示例 | [查看](./WebGame接口文档.md) |
| 🚀 快速启动指南 | 从零开始搭建运行环境的完整教程 | [查看](./WebGame快速启动指南.md) |
| 📝 开发总结 | 已完成工作和待办事项的详细清单 | [查看](./WebGame模块开发总结.md) |
| 📖 模块说明 | WebGame 模块的技术架构和使用方法 | [查看](./jeecg-boot/jeecg-boot-module/jeecg-module-webgame/README.md) |

## 🎯 功能模块

### ✅ 已完成

#### 1. 认证系统 (auth)
- [x] 用户注册
- [x] 用户登录（JWT）
- [x] 用户登出
- [x] 密码加密存储

**API 路径**: `/webgame/auth`

**已实现接口**:
- `POST /auth/register` - 用户注册
- `POST /auth/login` - 用户登录
- `POST /auth/logout` - 用户登出

#### 2. 数据库设计
- [x] 11 张核心数据表
- [x] 完整的索引和约束
- [x] 注释清晰的建表脚本

**数据表清单**:
- `wg_user` - 游戏用户表
- `wg_character` - 游戏角色表
- `wg_item_template` - 物品模板表
- `wg_player_item` - 玩家背包表
- `wg_monster` - 怪物表
- `wg_battle_log` - 战斗记录表
- `wg_dungeon` - 副本表
- `wg_dungeon_room` - 副本房间表
- `wg_pvp_match` - PVP对战记录表
- `wg_pvp_ranking` - PVP积分排行榜表
- `wg_friend` - 好友关系表

#### 3. 实体类 (Entity)
- [x] 所有数据表对应的实体类
- [x] 完整的字段注释
- [x] MyBatis Plus 注解

### 🚧 开发中

以下模块的 Service 和 Controller 层代码需要实现：

#### 4. 角色系统 (character)
- [ ] 创建角色
- [ ] 属性加点
- [ ] 角色信息查询
- [ ] 职业属性成长公式

**计划接口**:
- `POST /character/create` - 创建角色
- `GET /character/info` - 获取角色信息
- `POST /character/add-points` - 属性加点
- `GET /character/list` - 角色列表

#### 5. 物品系统 (item)
- [ ] 背包管理
- [ ] 装备穿戴
- [ ] 物品使用
- [ ] 套装加成

**计划接口**:
- `GET /item/bag` - 获取背包
- `POST /item/equip` - 装备物品
- `POST /item/unequip` - 卸下装备
- `POST /item/use` - 使用物品
- `POST /item/sell` - 出售物品
- `GET /item/equipment` - 已装备列表

#### 6. 战斗系统 (battle)
- [ ] PVE 战斗引擎
- [ ] 挂机战斗
- [ ] 伤害计算
- [ ] 掉落算法

**计划接口**:
- `POST /battle/pve` - PVE战斗
- `POST /battle/auto` - 挂机战斗
- `GET /battle/log` - 战斗记录
- `GET /battle/monster/{id}` - 怪物信息

#### 7. 副本系统 (dungeon)
- [ ] 单人副本
- [ ] 多人房间
- [ ] 副本挑战

**计划接口**:
- `GET /dungeon/list` - 副本列表
- `POST /dungeon/challenge` - 挑战副本
- `POST /dungeon/room/create` - 创建房间
- `POST /dungeon/room/join` - 加入房间
- `POST /dungeon/room/start` - 开始挑战

#### 8. PVP 系统 (pvp)
- [ ] Elo 匹配
- [ ] 实时对战
- [ ] 排行榜

**计划接口**:
- `POST /pvp/match` - 匹配对手
- `GET /pvp/ranking` - 排行榜
- `GET /pvp/my-record` - 个人战绩
- `GET /pvp/history` - 对战历史

#### 9. 社交系统 (social)
- [ ] 好友添加
- [ ] 好友列表
- [ ] 在线状态

**计划接口**:
- `POST /social/friend/add` - 添加好友
- `GET /social/friend/list` - 好友列表
- `POST /social/friend/remove` - 删除好友

## 🛠️ 技术栈

### 后端
- **框架**: Spring Boot 3.x + JeecgBoot 3.9.1
- **语言**: Java 17+
- **ORM**: MyBatis Plus
- **数据库**: MySQL 5.7+ / PostgreSQL
- **缓存**: Redis 6.0+
- **认证**: JWT (JSON Web Token)
- **文档**: Swagger 3 (OpenAPI)

### 前端（计划）
- **框架**: Vue 3 + TypeScript
- **UI库**: Tailwind CSS
- **状态管理**: Pinia
- **HTTP客户端**: Axios
- **WebSocket**: Socket.io

## 🚀 快速开始

### ⚠️ 重要：配置网关路由

**首次使用前必须配置网关路由！** WebGame 模块需要通过网关访问。

#### 方式一：Nacos 控制台配置（推荐）

1. 访问 Nacos 控制台：http://localhost:8848/nacos
2. 登录（默认账号：nacos / nacos）
3. 进入 **配置管理** → **配置列表**
4. 找到 `jeecg-gateway-router.json` (DEFAULT_GROUP)
5. 点击 **编辑**，在 JSON 数组中添加以下路由：

```json
{
  "id": "webgame",
  "order": 2,
  "predicates": [{
    "name": "Path",
    "args": { "_genkey_0": "/webgame/**" }
  }],
  "filters": [],
  "uri": "lb://jeecg-system"
},
{
  "id": "webgame-websocket",
  "order": 5,
  "predicates": [{
    "name": "Path",
    "args": { "_genkey_0": "/webgame/ws/**" }
  }],
  "filters": [],
  "uri": "lb:ws://jeecg-system"
}
```

6. 点击 **发布** 保存配置

详细步骤请查看：[WebGame网关配置指南.md](./WebGame网关配置指南.md)

#### 方式二：执行 SQL 脚本

```bash
# Windows PowerShell
.\setup_webgame_gateway.ps1

# Linux/Mac
chmod +x setup_webgame_gateway.sh
./setup_webgame_gateway.sh
```

或者手动执行：
```bash
mysql -u root -p nacos < jeecg-boot/db/webgame_gateway_route.sql
```

> ⚠️ **注意**：SQL 方式需要重启网关服务才能生效

---

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 5.7+
- Redis 6.0+

### 五步启动

```bash
# 1. 初始化数据库
mysql -u root -p < jeecg-boot/db/webgame.sql

# 2. 配置数据库连接
# 编辑 application-dev.yml，修改 MySQL 和 Redis 配置

# 3. 编译项目
cd jeecg-boot
mvn clean install -DskipTests

# 4. 启动服务
cd jeecg-module-system/jeecg-system-start
mvn spring-boot:run

# 5. 访问文档
# 浏览器打开: http://localhost:8080/jeecg-boot/doc.html
```

详细教程请查看：[WebGame快速启动指南.md](./WebGame快速启动指南.md)

### 测试接口

```bash
# 注册用户
curl -X POST http://localhost:8080/jeecg-boot/webgame/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123456"}'

# 登录获取 Token
curl -X POST http://localhost:8080/jeecg-boot/webgame/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123456"}'
```

## 📊 数据库设计

### 核心表关系

```
wg_user (用户)
  ├── wg_character (角色) - 一对多
  │     ├── wg_player_item (装备) - 一对多
  │     └── wg_battle_log (战斗记录) - 一对多
  ├── wg_player_item (背包) - 一对多
  ├── wg_pvp_ranking (PVP排行) - 一对一
  └── wg_friend (好友) - 多对多

wg_item_template (物品模板)
  └── wg_player_item (玩家物品) - 一对多

wg_monster (怪物)
  └── wg_battle_log (战斗记录) - 一对多

wg_dungeon (副本)
  └── wg_dungeon_room (副本房间) - 一对多
```

### 关键字段说明

**wg_user - 用户资源**
- `gold` - 金币（基础货币）
- `diamond` - 钻石（高级货币）
- `stamina` - 体力值（限制玩法）

**wg_character - 角色属性**
- `profession` - 职业（1战士/2法师/3猎人）
- `strength/intelligence/agility` - 三维属性
- `hp/mp` - 生命/魔法值
- `physical_attack/magic_attack` - 攻击力

**wg_player_item - 装备系统**
- `quality` - 品质（1白/2蓝/3紫/4橙）
- `is_equipped` - 是否装备中
- `enhance_level` - 强化等级
- `affixes` - 附加词条（JSON）
- `set_id` - 套装ID

## 🎮 游戏设计

### 职业系统

| 职业 | 主属性 | 成长特点 | 定位 |
|------|--------|----------|------|
| 战士 | 力量 | 1力=5HP+2物攻 | 坦克/物理输出 |
| 法师 | 智力 | 1智=5MP+2魔攻 | 爆发/群体伤害 |
| 猎人 | 敏捷 | 1敏=1物攻+闪避暴击 | 高机动/单体爆发 |

### 装备品质

| 品质 | 颜色 | 属性加成 | 词条数 | 特殊效果 |
|------|------|----------|--------|----------|
| 普通 | 白色 | 100% | 0 | 无 |
| 稀有 | 蓝色 | 120% | 1-2 | 无 |
| 史诗 | 紫色 | 150% | 3-4 | 无 |
| 传说 | 橙色 | 200% | 4+ | 被动技能 |

### 战斗流程

```
回合开始 → Buff判定 → 行动选择 → 命中判定 → 暴击判定 
→ 伤害计算 → 扣血结算 → 死亡判定 → 回合结束
```

### PVP 匹配

采用 **Elo 积分系统**：
- 新玩家初始积分：1000
- 胜利 +15~25 分（根据对手强度）
- 失败 -10~20 分
- 赛季制重置

## 📈 开发进度

### 总体进度：约 30%

- ✅ 数据库设计：100%
- ✅ 实体类：100%
- ✅ 认证模块：100%
- 🚧 业务模块：0%（Service/Controller 待实现）
- 🚧 WebSocket：0%
- 🚧 单元测试：0%

### 下一步计划

**优先级 P0**（核心玩法）:
1. 实现角色管理系统
2. 实现物品背包系统
3. 实现 PVE 战斗引擎

**优先级 P1**（重要功能）:
4. 实现单人副本
5. 实现 PVP 匹配和排行榜

**优先级 P2**（增强体验）:
6. 实现多人副本房间
7. 实现好友社交系统
8. WebSocket 实时通信

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

### 开发规范

1. **命名规范**
   - 实体类：`Wg` + 业务名（如 `WgUser`）
   - Controller：`Wg` + 业务名 + `Controller`
   - Service：`IWg` + 业务名 + `Service`

2. **代码风格**
   - 使用 Lombok 简化代码
   - 统一异常处理（`JeecgBootException`）
   - 完整的日志记录（Slf4j）

3. **提交规范**
   - feat: 新功能
   - fix: 修复bug
   - docs: 文档更新
   - refactor: 重构代码

## 📄 License

本项目基于 JeecgBoot 开源协议。

## 📞 联系方式

- 📧 Email: jeecgos@163.com
- 🌐 官网: http://www.jeecg.com
- 📚 文档: https://doc.jeecg.com

## 🙏 致谢

感谢以下开源项目：

- [JeecgBoot](https://github.com/jeecgboot/jeecg-boot) - 强大的低代码平台
- [Spring Boot](https://spring.io/projects/spring-boot) - Java 开发框架
- [MyBatis Plus](https://baomidou.com) - ORM 增强工具
- [Redis](https://redis.io) - 高性能缓存数据库

---

**祝开发顺利！🎮⚔️**

*最后更新: 2026-04-30*
