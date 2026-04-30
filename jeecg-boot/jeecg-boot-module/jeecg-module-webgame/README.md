# WebGame 模块 - 网页小游戏后端模块

## 模块简介

本模块是基于 JeecgBoot 框架开发的网页小游戏后端模块，实现了完整的回合制文字冒险游戏功能。

## 功能特性

### 1. 认证系统
- ✅ 用户注册/登录/登出
- ✅ JWT Token 认证
- ✅ 密码加密存储

### 2. 角色系统
- ✅ 三种职业选择（战士、法师、猎人）
- ✅ 属性加点机制
- ✅ 等级成长系统
- ✅ 职业专属属性成长

### 3. 物品与装备系统
- ✅ 背包管理
- ✅ 装备穿戴/卸下
- ✅ 物品使用/出售
- ✅ 品质体系（普通/稀有/史诗/传说）
- ✅ 套装加成机制
- ✅ 装备强化与附加词条

### 4. 战斗系统
- ✅ PVE 回合制战斗
- ✅ 挂机战斗
- ✅ 战斗记录查询
- ✅ 怪物掉落系统
- ✅ 伤害计算（暴击/闪避/防御）

### 5. 副本系统
- ✅ 单人副本
- ✅ 多人副本房间
- ✅ 副本挑战
- ✅ 体力消耗机制

### 6. PVP 竞技系统
- ✅ Elo 积分匹配
- ✅ 实时对战
- ✅ 排行榜
- ✅ 战绩统计

### 7. 社交系统
- ✅ 好友添加/删除
- ✅ 好友申请
- ✅ 在线状态
- ✅ 备注名称

## 技术栈

- **框架**: Spring Boot 3.x + JeecgBoot 3.9.1
- **数据库**: MySQL 5.7+ / PostgreSQL
- **缓存**: Redis
- **ORM**: MyBatis Plus
- **认证**: JWT (JSON Web Token)
- **API 文档**: Swagger 3 (OpenAPI)

## 项目结构

```
jeecg-module-webgame/
├── src/main/java/org/jeecg/modules/webgame/
│   ├── auth/                    # 认证模块
│   │   ├── controller/          # 控制器
│   │   ├── service/             # 服务层
│   │   ├── mapper/              # 数据访问层
│   │   ├── entity/              # 实体类
│   │   ├── dto/                 # 数据传输对象
│   │   └── vo/                  # 视图对象
│   ├── character/               # 角色模块
│   │   ├── controller/
│   │   ├── service/
│   │   ├── mapper/
│   │   ├── entity/
│   │   ├── dto/
│   │   └── vo/
│   ├── item/                    # 物品模块
│   │   ├── controller/
│   │   ├── service/
│   │   ├── mapper/
│   │   └── entity/
│   ├── battle/                  # 战斗模块
│   │   ├── controller/
│   │   ├── service/
│   │   ├── mapper/
│   │   └── entity/
│   ├── dungeon/                 # 副本模块
│   │   ├── controller/
│   │   ├── service/
│   │   ├── mapper/
│   │   └── entity/
│   ├── pvp/                     # PVP模块
│   │   ├── controller/
│   │   ├── service/
│   │   ├── mapper/
│   │   └── entity/
│   └── social/                  # 社交模块
│       ├── controller/
│       ├── service/
│       ├── mapper/
│       └── entity/
└── pom.xml
```

## 快速开始

### 1. 数据库初始化

执行 SQL 脚本创建数据表：

```bash
mysql -u root -p < jeecg-boot/db/webgame.sql
```

### 2. 配置 Redis

确保 `application.yml` 中配置了 Redis：

```yaml
spring:
  redis:
    database: 0
    host: localhost
    port: 6379
    password: 
```

### 3. 启动项目

```bash
cd jeecg-boot/jeecg-module-system/jeecg-system-start
mvn spring-boot:run
```

### 4. 访问 API 文档

启动成功后，访问 Swagger 文档：

```
http://localhost:8080/jeecg-boot/doc.html
```

## API 接口说明

详见 [WebGame接口文档.md](../../WebGame接口文档.md)

### 基础路径

```
http://localhost:8080/jeecg-boot/webgame
```

### 示例接口

#### 用户登录

```bash
curl -X POST http://localhost:8080/jeecg-boot/webgame/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123456"}'
```

#### 创建角色

```bash
curl -X POST http://localhost:8080/jeecg-boot/webgame/character/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"characterName":"剑士小明","profession":1}'
```

## 核心设计

### 1. 服务器权威架构

- 前端只负责发送操作指令和展示结果
- 所有核心逻辑（战斗计算、掉落、属性计算）在后端执行
- 防止客户端作弊

### 2. 职业属性成长公式

**战士 (Warrior)**
- 1点力量 = 5生命值 + 2物理攻击力
- 每级自动增长：力量+2, 智力+0, 敏捷+1

**法师 (Mage)**
- 1点智力 = 5魔法值 + 2魔法攻击力
- 每级自动增长：力量+0, 智力+3, 敏捷+0

**猎人 (Hunter)**
- 1点敏捷 = 1物理攻击力 + 闪避/暴击率加成
- 每级自动增长：力量+1, 智力+0, 敏捷+3

### 3. 装备品质体系

| 品质 | 颜色 | 基础属性 | 词条数 | 特殊效果 |
|------|------|----------|--------|----------|
| 普通 | 白色 | 100% | 0 | 无 |
| 稀有 | 蓝色 | 120% | 1-2 | 无 |
| 史诗 | 紫色 | 150% | 3-4 | 无 |
| 传说 | 橙色 | 200% | 4+ | 有被动技能 |

### 4. 战斗流程

```
回合开始 
  ↓
判断Buff/Debuff
  ↓
行动选择 (攻击/技能/物品)
  ↓
命中判定 (考虑闪避)
  ↓
暴击判定
  ↓
伤害计算 (攻击方攻击力 - 防守方防御力)
  ↓
扣血与死亡判定
  ↓
回合结束
```

### 5. PVP 匹配算法

采用 Elo 积分系统：

```
新积分 = 旧积分 + K * (实际得分 - 期望得分)

其中：
- K = 32 (系数)
- 实际得分：胜利=1, 平局=0.5, 失败=0
- 期望得分 = 1 / (1 + 10^((对手积分-自己积分)/400))
```

## 开发规范

### 1. 命名规范

- 实体类：`Wg` + 业务名称 (如 `WgUser`, `WgCharacter`)
- Controller：`Wg` + 业务名称 + `Controller`
- Service：`IWg` + 业务名称 + `Service` (接口), `Wg` + 业务名称 + `ServiceImpl` (实现)
- Mapper：`Wg` + 业务名称 + `Mapper`

### 2. 返回格式

统一使用 JeecgBoot 的 `Result` 类：

```java
return Result.OK("操作成功", data);
return Result.error("操作失败");
```

### 3. 异常处理

使用 `JeecgBootException` 抛出业务异常：

```java
if (user == null) {
    throw new JeecgBootException("用户不存在");
}
```

### 4. 日志记录

使用 Slf4j 记录日志：

```java
@Slf4j
public class XxxServiceImpl {
    public void method() {
        log.info("操作成功: {}", param);
        log.error("操作失败", exception);
    }
}
```

## 测试建议

### 1. 单元测试

重点测试：
- 伤害计算公式
- 装备属性汇总
- 掉落概率算法
- Elo 积分计算

### 2. 接口测试

使用 Postman 或 JUnit 测试：
- 装备强化并发安全性
- 物品消耗原子性
- 战斗结算一致性

### 3. 性能测试

- 模拟多用户同时战斗
- 排行榜查询性能
- Redis 缓存命中率

## 后续优化方向

1. **WebSocket 实时通信**
   - 多人副本房间状态同步
   - PVP 实时对战
   - 聊天功能

2. **离线收益系统**
   - 根据战斗力计算离线收益
   - 限制最大离线时长

3. **成就系统**
   - 首次通关奖励
   - 累计击杀统计
   - PVP 连胜奖励

4. **公会系统**
   - 公会创建与管理
   - 公会战
   - 公会副本

5. **商城系统**
   - 钻石购买
   - 道具售卖
   - 限时活动

## 常见问题

### Q1: 如何修改初始属性？

修改 `WgUserServiceImpl.register()` 方法中的初始化代码：

```java
user.setLevel(1);
user.setExperience(0L);
user.setGold(1000L);  // 修改初始金币
user.setDiamond(100);  // 修改初始钻石
```

### Q2: 如何调整掉落概率？

在 `wg_monster` 表的 `drop_items` 字段中配置 JSON：

```json
[
  {"itemTemplateId": "template-001", "dropRate": 0.3},
  {"itemTemplateId": "template-002", "dropRate": 0.1}
]
```

### Q3: Token 过期时间在哪里配置？

在 `WgUserServiceImpl.login()` 方法中：

```java
redisUtil.set("webgame:token:" + token, user.getId(), 86400); // 24小时
```

## 联系方式

如有问题，请提交 Issue 或联系开发团队。

## License

本项目遵循 JeecgBoot 的开源协议。
