# 剑之传说 - WebGame 模块接口文档

> 版本：v1.0.0  
> 更新日期：2026-04-30  
> 模块：webgame (网页小游戏模块)

---

## 通用约定

### Base URL

```
http://localhost:8080/jeecg-boot/webgame
```

### 请求格式

- Content-Type: `application/json`
- 字符编码: `UTF-8`

### 认证方式

- 采用 **JWT (JSON Web Token)** 认证
- 登录成功后，后端返回 `token`
- 后续需要鉴权的接口，前端会在请求头中携带：

```
Authorization: Bearer <token>
```

### 统一响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `code` | `number` | 状态码，200 成功，其他为失败 |
| `message` | `string` | 提示信息，前端会直接展示给用户 |
| `data` | `object \| null` | 业务数据，失败时为 `null` |

### 常用错误码

| code | 含义 |
|------|------|
| `200` | 成功 |
| `400` | 请求参数错误 |
| `401` | 未认证 / Token 过期 |
| `403` | 权限不足 |
| `404` | 资源不存在 |
| `409` | 资源冲突（如用户名已存在） |
| `500` | 服务器内部错误 |

---

## 接口列表总览

### 认证模块 (/webgame/auth)

| 序号 | 方法 | 路径 | 说明 | 是否需要鉴权 |
|------|------|------|------|:---:|
| 1 | POST | `/auth/login` | 用户登录 | ❌ |
| 2 | POST | `/auth/register` | 用户注册 | ❌ |
| 3 | POST | `/auth/logout` | 用户登出 | ✅ |

### 角色模块 (/webgame/character)

| 序号 | 方法 | 路径 | 说明 | 是否需要鉴权 |
|------|------|------|------|:---:|
| 4 | POST | `/character/create` | 创建角色 | ✅ |
| 5 | GET | `/character/info` | 获取角色信息 | ✅ |
| 6 | POST | `/character/add-points` | 属性加点 | ✅ |
| 7 | GET | `/character/list` | 获取角色列表 | ✅ |

### 物品模块 (/webgame/item)

| 序号 | 方法 | 路径 | 说明 | 是否需要鉴权 |
|------|------|------|------|:---:|
| 8 | GET | `/item/bag` | 获取背包列表 | ✅ |
| 9 | POST | `/item/equip` | 装备物品 | ✅ |
| 10 | POST | `/item/unequip` | 卸下装备 | ✅ |
| 11 | POST | `/item/use` | 使用物品 | ✅ |
| 12 | POST | `/item/sell` | 出售物品 | ✅ |
| 13 | GET | `/item/equipment` | 获取已装备列表 | ✅ |

### 战斗模块 (/webgame/battle)

| 序号 | 方法 | 路径 | 说明 | 是否需要鉴权 |
|------|------|------|------|:---:|
| 14 | POST | `/battle/pve` | PVE战斗 | ✅ |
| 15 | POST | `/battle/auto` | 挂机战斗 | ✅ |
| 16 | GET | `/battle/log` | 获取战斗记录 | ✅ |
| 17 | GET | `/battle/monster/{id}` | 获取怪物信息 | ✅ |

### 副本模块 (/webgame/dungeon)

| 序号 | 方法 | 路径 | 说明 | 是否需要鉴权 |
|------|------|------|------|:---:|
| 18 | GET | `/dungeon/list` | 获取副本列表 | ✅ |
| 19 | POST | `/dungeon/challenge` | 挑战副本(单人) | ✅ |
| 20 | POST | `/dungeon/room/create` | 创建副本房间 | ✅ |
| 21 | POST | `/dungeon/room/join` | 加入副本房间 | ✅ |
| 22 | POST | `/dungeon/room/leave` | 离开副本房间 | ✅ |
| 23 | POST | `/dungeon/room/start` | 开始副本挑战 | ✅ |
| 24 | GET | `/dungeon/room/list` | 获取副本房间列表 | ✅ |

### PVP模块 (/webgame/pvp)

| 序号 | 方法 | 路径 | 说明 | 是否需要鉴权 |
|------|------|------|------|:---:|
| 25 | POST | `/pvp/match` | 匹配对手 | ✅ |
| 26 | POST | `/pvp/cancel-match` | 取消匹配 | ✅ |
| 27 | GET | `/pvp/ranking` | 获取排行榜 | ✅ |
| 28 | GET | `/pvp/my-record` | 获取个人战绩 | ✅ |
| 29 | GET | `/pvp/history` | 获取对战历史 | ✅ |

### 社交模块 (/webgame/social)

| 序号 | 方法 | 路径 | 说明 | 是否需要鉴权 |
|------|------|------|------|:---:|
| 30 | POST | `/social/friend/add` | 添加好友 | ✅ |
| 31 | POST | `/social/friend/remove` | 删除好友 | ✅ |
| 32 | GET | `/social/friend/list` | 获取好友列表 | ✅ |
| 33 | POST | `/social/friend/respond` | 响应好友申请 | ✅ |
| 34 | GET | `/social/friend/requests` | 获取好友申请列表 | ✅ |

---

## 详细接口说明

## 一、认证模块

### 1. 用户登录

**`POST /auth/login`**

#### 请求参数

```json
{
  "username": "testuser",
  "password": "123456"
}
```

| 字段 | 类型 | 必填 | 校验规则 | 说明 |
|------|------|:---:|------|------|
| `username` | `string` | ✅ | 3-20 个字符 | 用户名 |
| `password` | `string` | ✅ | 6-20 个字符 | 密码 |

#### 成功响应

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "username": "testuser",
    "token": "eyJhbGciOiJIUzI1NiIs..."
  }
}
```

### 2. 用户注册

**`POST /auth/register`**

#### 请求参数

```json
{
  "username": "newplayer",
  "password": "abc123"
}
```

| 字段 | 类型 | 必填 | 校验规则 | 说明 |
|------|------|:---:|------|------|
| `username` | `string` | ✅ | 3-20 个字符 | 用户名，需唯一 |
| `password` | `string` | ✅ | 6-20 个字符 | 密码 |

#### 成功响应

```json
{
  "code": 200,
  "message": "注册成功，请登录",
  "data": null
}
```

### 3. 用户登出

**`POST /auth/logout`**

需要在请求头携带 `Authorization: Bearer <token>`

#### 成功响应

```json
{
  "code": 200,
  "message": "已退出登录",
  "data": null
}
```

---

## 二、角色模块

### 4. 创建角色

**`POST /character/create`**

#### 请求参数

```json
{
  "characterName": "剑士小明",
  "profession": 1
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|:---:|------|
| `characterName` | `string` | ✅ | 角色名称 |
| `profession` | `int` | ✅ | 职业类型(1-战士,2-法师,3-猎人) |

#### 成功响应

```json
{
  "code": 200,
  "message": "角色创建成功",
  "data": {
    "id": "char-uuid-001",
    "characterName": "剑士小明",
    "profession": 1,
    "professionName": "战士",
    "level": 1,
    "strength": 10,
    "intelligence": 0,
    "agility": 0,
    "hp": 150,
    "mp": 0,
    "physicalAttack": 20,
    "magicAttack": 0,
    "defense": 10,
    "dodgeRate": 0.05,
    "criticalRate": 0.1
  }
}
```

### 5. 获取角色信息

**`GET /character/info`**

#### 成功响应

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "id": "char-uuid-001",
    "characterName": "剑士小明",
    "profession": 1,
    "professionName": "战士",
    "level": 10,
    "experience": 5000,
    "nextLevelExp": 6000,
    "availablePoints": 5,
    "strength": 50,
    "intelligence": 0,
    "agility": 10,
    "hp": 500,
    "maxHp": 500,
    "mp": 0,
    "maxMp": 0,
    "physicalAttack": 120,
    "magicAttack": 0,
    "defense": 80,
    "dodgeRate": 0.05,
    "criticalRate": 0.15
  }
}
```

### 6. 属性加点

**`POST /character/add-points`**

#### 请求参数

```json
{
  "strengthPoint": 3,
  "intelligencePoint": 0,
  "agilityPoint": 2
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|:---:|------|
| `strengthPoint` | `int` | ❌ | 力量加点 |
| `intelligencePoint` | `int` | ❌ | 智力加点 |
| `agilityPoint` | `int` | ❌ | 敏捷加点 |

> 注意：三个属性点之和不能超过可用属性点总数

#### 成功响应

```json
{
  "code": 200,
  "message": "加点成功",
  "data": {
    "strength": 53,
    "intelligence": 0,
    "agility": 12,
    "availablePoints": 0,
    "hp": 515,
    "physicalAttack": 126,
    "defense": 85
  }
}
```

### 7. 获取角色列表

**`GET /character/list`**

#### 成功响应

```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": "char-uuid-001",
      "characterName": "剑士小明",
      "profession": 1,
      "professionName": "战士",
      "level": 10
    },
    {
      "id": "char-uuid-002",
      "characterName": "法师小红",
      "profession": 2,
      "professionName": "法师",
      "level": 8
    }
  ]
}
```

---

## 三、物品模块

### 8. 获取背包列表

**`GET /item/bag`**

支持分页和分类筛选

#### 请求参数 (Query Params)

| 参数 | 类型 | 必填 | 说明 |
|------|------|:---:|------|
| `itemType` | `int` | ❌ | 物品类型(1-装备,2-消耗品,3-材料)，不传则返回全部 |
| `pageNo` | `int` | ❌ | 页码，默认1 |
| `pageSize` | `int` | ❌ | 每页数量，默认20 |

#### 成功响应

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "records": [
      {
        "id": "item-uuid-001",
        "itemTemplateId": "template-001",
        "itemName": "铁剑",
        "quantity": 1,
        "isEquipped": 0,
        "enhanceLevel": 0,
        "quality": 1,
        "equipSlot": 1,
        "setId": null
      },
      {
        "id": "item-uuid-002",
        "itemTemplateId": "template-002",
        "itemName": "生命药水",
        "quantity": 10,
        "isEquipped": 0,
        "enhanceLevel": 0,
        "quality": 1,
        "equipSlot": null,
        "setId": null
      }
    ],
    "total": 25,
    "size": 20,
    "current": 1,
    "pages": 2
  }
}
```

### 9. 装备物品

**`POST /item/equip`**

#### 请求参数

```json
{
  "itemId": "item-uuid-001"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|:---:|------|
| `itemId` | `string` | ✅ | 物品ID |

#### 成功响应

```json
{
  "code": 200,
  "message": "装备成功",
  "data": null
}
```

### 10. 卸下装备

**`POST /item/unequip`**

#### 请求参数

```json
{
  "itemId": "item-uuid-001"
}
```

#### 成功响应

```json
{
  "code": 200,
  "message": "卸下成功",
  "data": null
}
```

### 11. 使用物品

**`POST /item/use`**

#### 请求参数

```json
{
  "itemId": "item-uuid-002",
  "quantity": 1
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|:---:|------|
| `itemId` | `string` | ✅ | 物品ID |
| `quantity` | `int` | ❌ | 使用数量，默认1 |

#### 成功响应

```json
{
  "code": 200,
  "message": "使用成功",
  "data": {
    "remainingQuantity": 9,
    "effect": "恢复生命值100点"
  }
}
```

### 12. 出售物品

**`POST /item/sell`**

#### 请求参数

```json
{
  "itemId": "item-uuid-001",
  "quantity": 1
}
```

#### 成功响应

```json
{
  "code": 200,
  "message": "出售成功",
  "data": {
    "goldGained": 50,
    "totalGold": 1050
  }
}
```

### 13. 获取已装备列表

**`GET /item/equipment`**

#### 成功响应

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "weapon": {
      "id": "item-uuid-001",
      "itemName": "铁剑",
      "quality": 1,
      "enhanceLevel": 0,
      "affixes": []
    },
    "head": null,
    "chest": {
      "id": "item-uuid-003",
      "itemName": "皮甲",
      "quality": 2,
      "enhanceLevel": 1,
      "affixes": [
        {"type": "defense", "value": 5}
      ]
    },
    "legs": null,
    "accessory1": null,
    "accessory2": null,
    "setBonus": []
  }
}
```

---

## 四、战斗模块

### 14. PVE战斗

**`POST /battle/pve`**

#### 请求参数

```json
{
  "monsterId": "monster-001",
  "skillId": "skill-001"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|:---:|------|
| `monsterId` | `string` | ✅ | 怪物ID |
| `skillId` | `string` | ❌ | 技能ID，不传则普通攻击 |

#### 成功响应

```json
{
  "code": 200,
  "message": "战斗胜利",
  "data": {
    "result": 1,
    "rounds": 5,
    "expGained": 100,
    "goldGained": 50,
    "dropItems": [
      {
        "itemId": "item-uuid-010",
        "itemName": "钢剑",
        "quality": 2
      }
    ],
    "battleLog": [
      {"round": 1, "attacker": "player", "damage": 50, "defender": "monster"},
      {"round": 1, "attacker": "monster", "damage": 20, "defender": "player"}
    ]
  }
}
```

### 15. 挂机战斗

**`POST /battle/auto`**

#### 请求参数

```json
{
  "area": "迷雾森林",
  "duration": 60
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|:---:|------|
| `area` | `string` | ✅ | 区域名称 |
| `duration` | `int` | ❌ | 挂机时长(分钟)，默认60 |

#### 成功响应

```json
{
  "code": 200,
  "message": "挂机完成",
  "data": {
    "totalBattles": 30,
    "wins": 28,
    "losses": 2,
    "totalExp": 2800,
    "totalGold": 1400,
    "dropItems": [
      {"itemId": "item-uuid-011", "itemName": "草药", "quantity": 5}
    ]
  }
}
```

### 16. 获取战斗记录

**`GET /battle/log`**

#### 请求参数 (Query Params)

| 参数 | 类型 | 必填 | 说明 |
|------|------|:---:|------|
| `battleType` | `int` | ❌ | 战斗类型(1-PVE,2-PVP,3-副本) |
| `pageNo` | `int` | ❌ | 页码 |
| `pageSize` | `int` | ❌ | 每页数量 |

#### 成功响应

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "records": [
      {
        "id": "log-001",
        "battleType": 1,
        "monsterName": "哥布林",
        "result": 1,
        "expGained": 100,
        "goldGained": 50,
        "createTime": "2026-04-30 10:30:00"
      }
    ],
    "total": 50
  }
}
```

### 17. 获取怪物信息

**`GET /battle/monster/{id}`**

#### 成功响应

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "id": "monster-001",
    "monsterName": "哥布林",
    "area": "迷雾森林",
    "minLevel": 1,
    "maxLevel": 5,
    "hp": 100,
    "physicalAttack": 15,
    "magicAttack": 0,
    "defense": 5,
    "expReward": 50,
    "goldReward": 25,
    "dropItems": [
      {"itemTemplateId": "template-010", "itemName": "哥布林牙齿", "dropRate": 0.3}
    ],
    "isBoss": 0
  }
}
```

---

## 五、副本模块

### 18. 获取副本列表

**`GET /dungeon/list`**

#### 请求参数 (Query Params)

| 参数 | 类型 | 必填 | 说明 |
|------|------|:---:|------|
| `dungeonType` | `int` | ❌ | 副本类型(1-单人,2-多人) |

#### 成功响应

```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": "dungeon-001",
      "dungeonName": "哥布林洞穴",
      "dungeonType": 1,
      "minLevel": 5,
      "recommendedPower": 200,
      "maxPlayers": 1,
      "staminaCost": 20,
      "description": "哥布林聚集的洞穴，适合新手挑战"
    },
    {
      "id": "dungeon-002",
      "dungeonName": "黑暗城堡",
      "dungeonType": 2,
      "minLevel": 20,
      "recommendedPower": 1000,
      "maxPlayers": 4,
      "staminaCost": 50,
      "description": "被黑暗力量笼罩的城堡，需要组队挑战"
    }
  ]
}
```

### 19. 挑战副本(单人)

**`POST /dungeon/challenge`**

#### 请求参数

```json
{
  "dungeonId": "dungeon-001"
}
```

#### 成功响应

```json
{
  "code": 200,
  "message": "副本挑战成功",
  "data": {
    "result": 1,
    "rounds": 10,
    "expGained": 500,
    "goldGained": 200,
    "dropItems": [
      {"itemId": "item-uuid-020", "itemName": "勇者之剑", "quality": 3}
    ]
  }
}
```

### 20. 创建副本房间

**`POST /dungeon/room/create`**

#### 请求参数

```json
{
  "dungeonId": "dungeon-002",
  "roomName": "黑暗城堡开荒队"
}
```

#### 成功响应

```json
{
  "code": 200,
  "message": "房间创建成功",
  "data": {
    "roomId": "room-001",
    "dungeonId": "dungeon-002",
    "roomName": "黑暗城堡开荒队",
    "hostUserId": "user-001",
    "currentPlayers": 1,
    "maxPlayers": 4,
    "status": 1
  }
}
```

### 21. 加入副本房间

**`POST /dungeon/room/join`**

#### 请求参数

```json
{
  "roomId": "room-001"
}
```

#### 成功响应

```json
{
  "code": 200,
  "message": "加入房间成功",
  "data": {
    "roomId": "room-001",
    "currentPlayers": 2,
    "players": [
      {"userId": "user-001", "characterName": "剑士小明", "level": 25},
      {"userId": "user-002", "characterName": "法师小红", "level": 23}
    ]
  }
}
```

### 22. 离开副本房间

**`POST /dungeon/room/leave`**

#### 请求参数

```json
{
  "roomId": "room-001"
}
```

#### 成功响应

```json
{
  "code": 200,
  "message": "离开房间成功",
  "data": null
}
```

### 23. 开始副本挑战

**`POST /dungeon/room/start`**

仅房主可调用

#### 请求参数

```json
{
  "roomId": "room-001"
}
```

#### 成功响应

```json
{
  "code": 200,
  "message": "副本挑战开始",
  "data": {
    "roomId": "room-001",
    "status": 2,
    "startTime": "2026-04-30 15:00:00"
  }
}
```

### 24. 获取副本房间列表

**`GET /dungeon/room/list`**

#### 请求参数 (Query Params)

| 参数 | 类型 | 必填 | 说明 |
|------|------|:---:|------|
| `dungeonId` | `string` | ❌ | 副本ID |
| `status` | `int` | ❌ | 房间状态 |

#### 成功响应

```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "roomId": "room-001",
      "dungeonId": "dungeon-002",
      "roomName": "黑暗城堡开荒队",
      "hostUserName": "剑士小明",
      "currentPlayers": 2,
      "maxPlayers": 4,
      "status": 1
    }
  ]
}
```

---

## 六、PVP模块

### 25. 匹配对手

**`POST /pvp/match`**

#### 成功响应

```json
{
  "code": 200,
  "message": "匹配成功",
  "data": {
    "matchId": "match-001",
    "opponent": {
      "userId": "user-003",
      "characterName": "猎人小李",
      "level": 20,
      "rating": 1250
    },
    "estimatedWaitTime": 5
  }
}
```

### 26. 取消匹配

**`POST /pvp/cancel-match`**

#### 成功响应

```json
{
  "code": 200,
  "message": "已取消匹配",
  "data": null
}
```

### 27. 获取排行榜

**`GET /pvp/ranking`**

#### 请求参数 (Query Params)

| 参数 | 类型 | 必填 | 说明 |
|------|------|:---:|------|
| `season` | `string` | ❌ | 赛季，默认当前赛季 |
| `pageNo` | `int` | ❌ | 页码 |
| `pageSize` | `int` | ❌ | 每页数量 |

#### 成功响应

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "season": "2026-S1",
    "records": [
      {
        "rank": 1,
        "userId": "user-010",
        "characterName": "战神张三",
        "rating": 2500,
        "wins": 200,
        "losses": 50,
        "totalMatches": 250
      },
      {
        "rank": 2,
        "userId": "user-011",
        "characterName": "法神李四",
        "rating": 2450,
        "wins": 190,
        "losses": 60,
        "totalMatches": 250
      }
    ],
    "total": 1000
  }
}
```

### 28. 获取个人战绩

**`GET /pvp/my-record`**

#### 成功响应

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "userId": "user-001",
    "characterName": "剑士小明",
    "rating": 1500,
    "maxRating": 1600,
    "wins": 80,
    "losses": 70,
    "draws": 5,
    "totalMatches": 155,
    "currentRank": 150,
    "winRate": 0.516
  }
}
```

### 29. 获取对战历史

**`GET /pvp/history`**

#### 成功响应

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "records": [
      {
        "matchId": "match-001",
        "opponentName": "猎人小李",
        "result": 1,
        "rounds": 8,
        "ratingChange": 15,
        "createTime": "2026-04-30 16:00:00"
      }
    ],
    "total": 155
  }
}
```

---

## 七、社交模块

### 30. 添加好友

**`POST /social/friend/add`**

#### 请求参数

```json
{
  "friendUserId": "user-002",
  "remarkName": "小红"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|:---:|------|
| `friendUserId` | `string` | ✅ | 好友用户ID |
| `remarkName` | `string` | ❌ | 备注名称 |

#### 成功响应

```json
{
  "code": 200,
  "message": "好友申请已发送",
  "data": null
}
```

### 31. 删除好友

**`POST /social/friend/remove`**

#### 请求参数

```json
{
  "friendUserId": "user-002"
}
```

#### 成功响应

```json
{
  "code": 200,
  "message": "已删除好友",
  "data": null
}
```

### 32. 获取好友列表

**`GET /social/friend/list`**

#### 成功响应

```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "userId": "user-002",
      "username": "xiaohong",
      "nickname": "小红",
      "remarkName": "小红",
      "level": 18,
      "onlineStatus": 1,
      "lastLoginTime": "2026-04-30 17:00:00"
    },
    {
      "userId": "user-003",
      "username": "xiaoli",
      "nickname": "小李",
      "remarkName": null,
      "level": 20,
      "onlineStatus": 0,
      "lastLoginTime": "2026-04-29 20:00:00"
    }
  ]
}
```

### 33. 响应好友申请

**`POST /social/friend/respond`**

#### 请求参数

```json
{
  "requestId": "req-001",
  "accept": true
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|:---:|------|
| `requestId` | `string` | ✅ | 申请ID |
| `accept` | `boolean` | ✅ | 是否接受 |

#### 成功响应

```json
{
  "code": 200,
  "message": "已接受好友申请",
  "data": null
}
```

### 34. 获取好友申请列表

**`GET /social/friend/requests`**

#### 成功响应

```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "id": "req-001",
      "applicantUserId": "user-004",
      "applicantName": "小王",
      "applicantLevel": 15,
      "createTime": "2026-04-30 18:00:00"
    }
  ]
}
```

---

## 数据库表结构

详见 `jeecg-boot/db/webgame.sql`

主要表清单：
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

---

## 补充说明

1. **安全性**
   - 所有密码均经过加密存储（使用盐值+哈希）
   - JWT Token 有效期建议设置为 24 小时
   - 敏感接口需要进行签名验证

2. **性能优化**
   - 排行榜数据建议使用 Redis 缓存
   - 频繁读取的物品模板数据应缓存
   - 战斗计算在后端进行，前端只负责展示

3. **WebSocket 实时通信**
   - 多人副本房间状态同步
   - PVP 实时对战
   - 聊天功能
   - WebSocket 连接地址：`ws://localhost:8080/jeecg-boot/webgame/ws`

4. **CORS 配置**
   - 开发环境前端地址：`http://localhost:5173`
   - 生产环境需配置实际的域名

5. **数值平衡**
   - 职业属性成长公式需在服务端严格校验
   - 战斗伤害计算公式需要单元测试覆盖
   - 掉落概率需要伪随机算法保证公平性
