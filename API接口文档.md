# 剑之传说 - 前后端接口文档

> 版本：v1.2.0
> 更新日期：2026-04-30
> 状态：认证模块 + 角色模块 + 战宠模块

---

## 通用约定

### Base URL

```
/jeecg-boot/webgame
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
| `409` | 资源冲突（如用户名已存在） |
| `500` | 服务器内部错误 |

---

## 接口列表

| 序号 | 方法 | 路径 | 说明 | 是否需要鉴权 |
|------|------|------|------|:---:|
| 1 | POST | `/auth/login` | 用户登录 | ❌ |
| 2 | POST | `/auth/register` | 用户注册 | ❌ |
| 3 | POST | `/auth/logout` | 用户登出 | ✅ |
| 4 | GET | `/auth/check-username` | 检测用户名是否可用 | ❌ |
| 5 | GET | `/character/list` | 获取角色列表 | ✅ |
| 6 | POST | `/character/create` | 创建角色 | ✅ |
| 7 | GET | `/character/check-name` | 检测角色名是否可用 | ✅ |
| 8 | DELETE | `/character/delete/{id}` | 删除角色 | ✅ |
| 9 | GET | `/pet/list` | 获取角色战宠列表 | ✅ |
| 10 | GET | `/pet/{id}` | 获取战宠详情 | ✅ |
| 11 | PUT | `/pet/rename` | 重命名战宠 | ✅ |
| 12 | PUT | `/pet/set-active` | 设置出战战宠 | ✅ |
| 13 | POST | `/pet/feed` | 喂食经验道具 | ✅ |
| 14 | POST | `/pet/evolve` | 战宠进化 | ✅ |
| 15 | PUT | `/pet/skill-slot` | 装备/替换战宠技能 | ✅ |
| 16 | PUT | `/pet/equip` | 战宠穿戴装备 | ✅ |
| 17 | PUT | `/pet/unequip` | 卸下战宠装备 | ✅ |
| 18 | GET | `/pet/encyclopedia` | 获取战宠图鉴列表 | ✅ |

---

## 1. 用户登录

### `POST /auth/login`

#### 请求参数（Request Body）

```json
{
  "username": "testuser",
  "password": "123456"
}
```

| 字段 | 类型 | 必填 | 校验规则 | 说明 |
|------|------|:---:|------|------|
| `username` | `string` | ✅ | 3-20 个字符 | 用户名 |
| `password` | `string` | ✅ | 6-20 个字符 | 密码（明文传输，建议后端做 hash） |

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

| 字段 | 类型 | 说明 |
|------|------|------|
| `data.id` | `string` | 用户唯一标识（UUID） |
| `data.username` | `string` | 用户名 |
| `data.token` | `string` | JWT Token，前端会存入 localStorage |

#### 失败响应示例

**用户名不存在：**
```json
{
  "code": 401,
  "message": "用户名不存在",
  "data": null
}
```

**密码错误：**
```json
{
  "code": 401,
  "message": "密码错误",
  "data": null
}
```

---

## 2. 用户注册

### `POST /auth/register`

#### 请求参数（Request Body）

```json
{
  "username": "newplayer",
  "password": "abc123"
}
```

| 字段 | 类型 | 必填 | 校验规则 | 说明 |
|------|------|:---:|------|------|
| `username` | `string` | ✅ | 3-20 个字符 | 用户名，需唯一 |
| `password` | `string` | ✅ | 6-20 个字符 | 密码（明文传输，后端做 hash 存储） |

> **备注**：前端已做「确认密码」的一致性校验，不会传 `confirmPassword` 字段给后端。

#### 成功响应

```json
{
  "code": 200,
  "message": "注册成功，请登录",
  "data": null
}
```

> **说明**：注册成功后前端不会自动登录，而是切换到登录 Tab 让用户手动登录。所以注册接口无需返回 token。

#### 失败响应示例

**用户名已存在：**
```json
{
  "code": 409,
  "message": "该用户名已被注册",
  "data": null
}
```

**参数校验失败：**
```json
{
  "code": 400,
  "message": "用户名需要 3-20 个字符",
  "data": null
}
```

---

## 3. 用户登出

### `POST /auth/logout`

> 需要在请求头携带 `Authorization: Bearer <token>`

#### 请求参数

无（Token 从 Header 中获取）

#### 成功响应

```json
{
  "code": 200,
  "message": "已退出登录",
  "data": null
}
```

> **说明**：前端会清除本地 localStorage 中的用户信息。后端可选择将该 Token 加入黑名单，或直接依赖 Token 过期机制。

---

## 4. 检测用户名是否可用

### `GET /auth/check-username`

> 前端在注册表单中，用户输入用户名后会**防抖 500ms** 自动调用此接口，实时显示用户名是否可用。

#### 请求参数（Query String）

```
GET /auth/check-username?username=testuser
```

| 字段 | 类型 | 必填 | 校验规则 | 说明 |
|------|------|:---:|------|------|
| `username` | `string` | ✅ | 3-20 个字符 | 待检测的用户名 |

#### 成功响应（可用）

```json
{
  "code": 200,
  "message": "用户名可用",
  "data": {
    "available": true
  }
}
```

#### 成功响应（已占用）

```json
{
  "code": 200,
  "message": "该用户名已被注册",
  "data": {
    "available": false
  }
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `data.available` | `boolean` | `true` 可用，`false` 已被占用 |

> **说明**：此接口会被频繁调用（用户每次输入都会触发），建议后端做接口限流（如每 IP 每秒最多 5 次）。

---

## 5. 获取角色列表

### `GET /character/list`

> 需要在请求头携带 `Authorization: Bearer <token>`

#### 请求参数

无（用户身份从 JWT Token 中获取）

#### 成功响应

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "userId": "550e8400-e29b-41d4-a716-446655440000",
      "characterName": "剑圣无名",
      "profession": 1,
      "level": 15,
      "experience": 2340,
      "strength": 10,
      "intelligence": 3,
      "agility": 5,
      "hp": 500,
      "mp": 50,
      "physicalAttack": 20,
      "magicAttack": 6,
      "defense": 15,
      "dodgeRate": 5,
      "criticalRate": 3,
      "createTime": "2026-04-20T08:00:00Z",
      "updateTime": "2026-04-28T10:00:00Z"
    }
  ]
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `data` | `CharacterInfo[]` | 角色数组，最多 3 个元素 |
| `data[].id` | `string` | 角色唯一标识（UUID） |
| `data[].userId` | `string` | 所属用户 UUID |
| `data[].characterName` | `string` | 角色名 |
| `data[].profession` | `number` | 职业类型：1-战士, 2-法师, 3-猎人 |
| `data[].level` | `number` | 等级 |
| `data[].experience` | `number` | 经验值 |
| `data[].strength` | `number` | 力量 |
| `data[].intelligence` | `number` | 智力 |
| `data[].agility` | `number` | 敏捷 |
| `data[].hp` | `number` | 生命值 |
| `data[].mp` | `number` | 魔法值 |
| `data[].physicalAttack` | `number` | 物理攻击力 |
| `data[].magicAttack` | `number` | 魔法攻击力 |
| `data[].defense` | `number` | 防御力 |
| `data[].dodgeRate` | `number` | 闪避率 |
| `data[].criticalRate` | `number` | 暴击率 |
| `data[].bonusHp` | `number` | 战宠加成-生命值（默认0） |
| `data[].bonusPhysicalAttack` | `number` | 战宠加成-物理攻击（默认0） |
| `data[].bonusMagicAttack` | `number` | 战宠加成-魔法攻击（默认0） |
| `data[].bonusDefense` | `number` | 战宠加成-防御力（默认0） |
| `data[].createTime` | `string` | 创建时间（ISO 8601） |
| `data[].updateTime` | `string` | 更新时间（ISO 8601） |

> **说明**：前端在角色选择页加载时调用此接口。如果返回空数组，前端自动引导用户创建角色。

#### 失败响应示例

**未认证：**
```json
{
  "code": 401,
  "message": "登录已过期，请重新登录",
  "data": null
}
```

---

## 6. 创建角色

### `POST /character/create`

> 需要在请求头携带 `Authorization: Bearer <token>`

#### 请求参数（Request Body）

```json
{
  "characterName": "暗影刺客",
  "profession": 3
}
```

| 字段 | 类型 | 必填 | 校验规则 | 说明 |
|------|------|:---:|------|------|
| `characterName` | `string` | ✅ | 2-12 个字符，全局唯一 | 角色名 |
| `profession` | `number` | ✅ | 枚举值：1 / 2 / 3 | 职业类型：1-战士, 2-法师, 3-猎人 |

#### 成功响应

```json
{
  "code": 200,
  "message": "角色创建成功",
  "data": {
    "id": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "characterName": "暗影刺客",
    "profession": 3,
    "level": 1,
    "experience": 0,
    "strength": 5,
    "intelligence": 4,
    "agility": 11,
    "hp": 125,
    "mp": 40,
    "physicalAttack": 10,
    "magicAttack": 8,
    "defense": 5,
    "dodgeRate": 5,
    "criticalRate": 3,
    "bonusHp": 0,
    "bonusPhysicalAttack": 0,
    "bonusMagicAttack": 0,
    "bonusDefense": 0,
    "createTime": "2026-04-30T12:00:00Z",
    "updateTime": "2026-04-30T12:00:00Z"
  }
}
```

> **说明**：创建成功后，后端根据 `profession` 初始化对应职业的基础属性值。前端会将新角色追加到本地角色列表中。

#### 失败响应示例

**角色数量已满：**
```json
{
  "code": 403,
  "message": "每个账号最多创建 3 个角色",
  "data": null
}
```

**角色名已存在：**
```json
{
  "code": 409,
  "message": "该角色名已被使用",
  "data": null
}
```

**职业类型非法：**
```json
{
  "code": 400,
  "message": "无效的职业类型",
  "data": null
}
```

---

## 7. 检测角色名是否可用

### `GET /character/check-name`

> 需要在请求头携带 `Authorization: Bearer <token>`

> 前端在角色创建表单中，用户输入角色名后会**防抖 500ms** 自动调用此接口，实时显示角色名是否可用。

#### 请求参数（Query String）

```
GET /character/check-name?name=暗影刺客
```

| 字段 | 类型 | 必填 | 校验规则 | 说明 |
|------|------|:---:|------|------|
| `name` | `string` | ✅ | 2-12 个字符 | 待检测的角色名 |

#### 成功响应（可用）

```json
{
  "code": 200,
  "message": "角色名可用",
  "data": {
    "available": true
  }
}
```

#### 成功响应（已占用）

```json
{
  "code": 200,
  "message": "该角色名已被使用",
  "data": {
    "available": false
  }
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `data.available` | `boolean` | `true` 可用，`false` 已被占用 |

> **说明**：此接口会被频繁调用（用户每次输入都会触发），建议后端做接口限流（如每 IP 每秒最多 5 次）。

---

## 8. 删除角色

### `DELETE /character/delete/{id}`

> 需要在请求头携带 `Authorization: Bearer <token>`

#### 请求参数（Path Variable）

```
DELETE /character/delete/a1b2c3d4-e5f6-7890-abcd-ef1234567890
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|:---:|------|
| `id` | `string` | ✅ | 要删除的角色 UUID |

#### 成功响应

```json
{
  "code": 200,
  "message": "角色已删除",
  "data": null
}
```

> **说明**：前端在删除前会弹出确认弹窗，确认后才调用此接口。删除成功后前端会从本地角色列表中移除该角色。

#### 失败响应示例

**角色不存在：**
```json
{
  "code": 404,
  "message": "角色不存在",
  "data": null
}
```

**无权删除他人角色：**
```json
{
  "code": 403,
  "message": "无权操作该角色",
  "data": null
}
```

---

## 9. 获取角色战宠列表

### `GET /pet/list`

> 需要在请求头携带 `Authorization: Bearer <token>`

#### 请求参数（Query String）

```
GET /pet/list?characterId=a1b2c3d4-e5f6-7890-abcd-ef1234567890
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|:---:|------|
| `characterId` | `string` | ✅ | 角色 UUID |

#### 成功响应

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": "p1a2b3c4-d5e6-7890-abcd-ef1234567890",
      "characterId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "petTypeId": 1001,
      "nickname": "小火焰",
      "level": 15,
      "experience": 1200,
      "isActive": true,
      "rarity": 3,
      "hp": 320,
      "attack": 45,
      "defense": 22,
      "speed": 35,
      "skillSlots": [2001, 2005, 0],
      "equipSlots": {
        "armor": "eq-uuid-001",
        "accessory": null
      },
      "bonusToOwner": {
        "hp": 50,
        "attack": 10,
        "defense": 5
      },
      "createTime": "2026-04-25T08:00:00Z",
      "updateTime": "2026-04-30T10:00:00Z"
    }
  ]
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `data` | `PetInfo[]` | 战宠数组，最多 3 个元素 |
| `data[].id` | `string` | 战宠实例 UUID |
| `data[].characterId` | `string` | 所属角色 UUID |
| `data[].petTypeId` | `number` | 战宠类型 ID（关联图鉴） |
| `data[].nickname` | `string` | 战宠昵称，默认与类型名相同 |
| `data[].level` | `number` | 等级 |
| `data[].experience` | `number` | 当前经验值 |
| `data[].isActive` | `boolean` | 是否为当前出战战宠 |
| `data[].rarity` | `number` | 品质：1-N, 2-R, 3-SR, 4-SSR |
| `data[].hp` | `number` | 当前生命值（含装备加成） |
| `data[].attack` | `number` | 当前攻击力（含装备加成） |
| `data[].defense` | `number` | 当前防御力（含装备加成） |
| `data[].speed` | `number` | 当前速度（含装备加成） |
| `data[].skillSlots` | `number[]` | 技能槽，长度固定 3，0 表示空槽 |
| `data[].equipSlots` | `object` | 装备槽：`armor` 护甲位、`accessory` 饰品位，`null` 表示空 |
| `data[].bonusToOwner` | `object` | 给主人的属性加成（仅出战时生效） |
| `data[].createTime` | `string` | 获得时间（ISO 8601） |
| `data[].updateTime` | `string` | 更新时间（ISO 8601） |

> **说明**：`isActive` 最多只有一只为 `true`。如果返回空数组表示该角色尚未获得任何战宠。`bonusToOwner` 仅出战战宠的加成会实际作用于角色属性。

#### 失败响应示例

```json
{
  "code": 404,
  "message": "角色不存在",
  "data": null
}
```

---

## 10. 获取战宠详情

### `GET /pet/{id}`

> 需要在请求头携带 `Authorization: Bearer <token>`

#### 请求参数（Path Variable）

```
GET /pet/p1a2b3c4-d5e6-7890-abcd-ef1234567890
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|:---:|------|
| `id` | `string` | ✅ | 战宠实例 UUID |

#### 成功响应

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": "p1a2b3c4-d5e6-7890-abcd-ef1234567890",
    "characterId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "petTypeId": 1001,
    "nickname": "小火焰",
    "level": 15,
    "experience": 1200,
    "isActive": true,
    "rarity": 3,
    "hp": 320,
    "attack": 45,
    "defense": 22,
    "speed": 35,
    "skillSlots": [2001, 2005, 0],
    "learnedSkills": [2001, 2005, 2010, 2018],
    "equipSlots": {
      "armor": "eq-uuid-001",
      "accessory": null
    },
    "bonusToOwner": {
      "hp": 50,
      "attack": 10,
      "defense": 5
    },
    "evolveInfo": {
      "canEvolve": false,
      "evolveToTypeId": 1002,
      "evolveRequiredLevel": 25,
      "evolveRequiredItems": [
        { "itemId": 3001, "itemName": "火之精华", "required": 3, "owned": 1 }
      ]
    },
    "createTime": "2026-04-25T08:00:00Z",
    "updateTime": "2026-04-30T10:00:00Z"
  }
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `data.learnedSkills` | `number[]` | 已学会的所有技能 ID（包含已装备和未装备的） |
| `data.evolveInfo` | `object` | 进化相关信息 |
| `data.evolveInfo.canEvolve` | `boolean` | 当前是否满足进化条件 |
| `data.evolveInfo.evolveToTypeId` | `number` | 进化目标类型 ID，0 表示已最终形态 |
| `data.evolveInfo.evolveRequiredLevel` | `number` | 进化所需等级 |
| `data.evolveInfo.evolveRequiredItems` | `object[]` | 进化所需材料及持有情况 |

> **说明**：详情接口比列表接口多返回 `learnedSkills`（全部已学技能）和 `evolveInfo`（进化信息），用于战宠详情/养成页面展示。

#### 失败响应示例

```json
{
  "code": 404,
  "message": "战宠不存在",
  "data": null
}
```

```json
{
  "code": 403,
  "message": "无权查看该战宠",
  "data": null
}
```

---

## 11. 重命名战宠

### `PUT /pet/rename`

> 需要在请求头携带 `Authorization: Bearer <token>`

#### 请求参数（Request Body）

```json
{
  "petId": "p1a2b3c4-d5e6-7890-abcd-ef1234567890",
  "nickname": "烈焰宝宝"
}
```

| 字段 | 类型 | 必填 | 校验规则 | 说明 |
|------|------|:---:|------|------|
| `petId` | `string` | ✅ | UUID 格式 | 战宠实例 ID |
| `nickname` | `string` | ✅ | 2-8 个字符 | 新昵称 |

#### 成功响应

```json
{
  "code": 200,
  "message": "命名成功",
  "data": null
}
```

#### 失败响应示例

```json
{
  "code": 400,
  "message": "昵称需要 2-8 个字符",
  "data": null
}
```

```json
{
  "code": 403,
  "message": "无权操作该战宠",
  "data": null
}
```

---

## 12. 设置出战战宠

### `PUT /pet/set-active`

> 需要在请求头携带 `Authorization: Bearer <token>`

#### 请求参数（Request Body）

```json
{
  "petId": "p1a2b3c4-d5e6-7890-abcd-ef1234567890"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|:---:|------|
| `petId` | `string` | ✅ | 要设为出战的战宠 UUID |

> **说明**：调用后，后端将该角色下其他战宠的 `isActive` 设为 `false`，目标战宠设为 `true`。若要取消出战（不带战宠上场），传 `petId` 为空字符串或 `null`。

#### 成功响应

```json
{
  "code": 200,
  "message": "出战设置成功",
  "data": null
}
```

#### 失败响应示例

```json
{
  "code": 404,
  "message": "战宠不存在",
  "data": null
}
```

---

## 13. 喂食经验道具

### `POST /pet/feed`

> 需要在请求头携带 `Authorization: Bearer <token>`

#### 请求参数（Request Body）

```json
{
  "petId": "p1a2b3c4-d5e6-7890-abcd-ef1234567890",
  "itemId": 4001,
  "quantity": 3
}
```

| 字段 | 类型 | 必填 | 校验规则 | 说明 |
|------|------|:---:|------|------|
| `petId` | `string` | ✅ | UUID 格式 | 战宠实例 ID |
| `itemId` | `number` | ✅ | 有效的经验道具 ID | 经验道具 ID |
| `quantity` | `number` | ✅ | 1-99 | 使用数量 |

#### 成功响应

```json
{
  "code": 200,
  "message": "喂食成功",
  "data": {
    "level": 16,
    "experience": 800,
    "levelUp": true,
    "newLearnedSkill": 2010
  }
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `data.level` | `number` | 喂食后等级 |
| `data.experience` | `number` | 喂食后经验值 |
| `data.levelUp` | `boolean` | 是否触发了升级 |
| `data.newLearnedSkill` | `number \| null` | 升级后自动学会的新技能 ID，未学会新技能时为 `null` |

> **说明**：后端需校验玩家背包中该道具数量是否足够，使用后扣除对应数量。如果升级触发自动习得技能（达到技能习得等级），后端将新技能加入 `learnedSkills`。

#### 失败响应示例

```json
{
  "code": 400,
  "message": "道具数量不足",
  "data": null
}
```

```json
{
  "code": 400,
  "message": "战宠已达最大等级",
  "data": null
}
```

---

## 14. 战宠进化

### `POST /pet/evolve`

> 需要在请求头携带 `Authorization: Bearer <token>`

#### 请求参数（Request Body）

```json
{
  "petId": "p1a2b3c4-d5e6-7890-abcd-ef1234567890"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|:---:|------|
| `petId` | `string` | ✅ | 要进化的战宠 UUID |

> **说明**：后端校验：①等级是否达到进化要求 ②背包中进化材料是否充足。满足条件后扣除材料，更新战宠的 `petTypeId` 为进化后形态，重新计算属性（保留百分比成长），可能习得进化专属技能。

#### 成功响应

```json
{
  "code": 200,
  "message": "进化成功",
  "data": {
    "petTypeId": 1002,
    "nickname": "小火焰",
    "level": 25,
    "hp": 520,
    "attack": 78,
    "defense": 40,
    "speed": 50,
    "newLearnedSkill": 2050
  }
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `data.petTypeId` | `number` | 进化后的战宠类型 ID |
| `data.newLearnedSkill` | `number \| null` | 进化习得的专属技能 ID |

#### 失败响应示例

```json
{
  "code": 400,
  "message": "等级不足，进化需要 Lv.25",
  "data": null
}
```

```json
{
  "code": 400,
  "message": "进化材料不足：火之精华 x3",
  "data": null
}
```

```json
{
  "code": 400,
  "message": "该战宠已是最终形态",
  "data": null
}
```

---

## 15. 装备/替换战宠技能

### `PUT /pet/skill-slot`

> 需要在请求头携带 `Authorization: Bearer <token>`

#### 请求参数（Request Body）

```json
{
  "petId": "p1a2b3c4-d5e6-7890-abcd-ef1234567890",
  "slotIndex": 2,
  "skillId": 2010
}
```

| 字段 | 类型 | 必填 | 校验规则 | 说明 |
|------|------|:---:|------|------|
| `petId` | `string` | ✅ | UUID 格式 | 战宠实例 ID |
| `slotIndex` | `number` | ✅ | 0-2 | 技能槽位索引 |
| `skillId` | `number` | ✅ | 有效的已学技能 ID | 要装备的技能 ID，传 0 表示卸下该槽位 |

> **说明**：后端需校验 `skillId` 是否在该战宠的 `learnedSkills` 中，且不能与其他槽位重复装备同一技能。

#### 成功响应

```json
{
  "code": 200,
  "message": "技能装备成功",
  "data": null
}
```

#### 失败响应示例

```json
{
  "code": 400,
  "message": "该技能尚未学会",
  "data": null
}
```

```json
{
  "code": 400,
  "message": "该技能已在其他槽位装备",
  "data": null
}
```

---

## 16. 战宠穿戴装备

### `PUT /pet/equip`

> 需要在请求头携带 `Authorization: Bearer <token>`

#### 请求参数（Request Body）

```json
{
  "petId": "p1a2b3c4-d5e6-7890-abcd-ef1234567890",
  "slotType": "armor",
  "equipmentId": "eq-uuid-001"
}
```

| 字段 | 类型 | 必填 | 校验规则 | 说明 |
|------|------|:---:|------|------|
| `petId` | `string` | ✅ | UUID 格式 | 战宠实例 ID |
| `slotType` | `string` | ✅ | 枚举：`armor` / `accessory` | 装备槽位类型 |
| `equipmentId` | `string` | ✅ | 有效的装备 UUID | 要穿戴的装备 ID |

> **说明**：后端需校验：①装备在角色背包中 ②装备类型为战宠可用 ③目标槽位是否已有装备（若有则自动替换回背包）。

#### 成功响应

```json
{
  "code": 200,
  "message": "装备穿戴成功",
  "data": {
    "replacedEquipmentId": null
  }
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `data.replacedEquipmentId` | `string \| null` | 被替换下的旧装备 ID，无替换时为 `null` |

#### 失败响应示例

```json
{
  "code": 400,
  "message": "装备不存在或不在背包中",
  "data": null
}
```

```json
{
  "code": 400,
  "message": "该装备类型不适用于战宠",
  "data": null
}
```

---

## 17. 卸下战宠装备

### `PUT /pet/unequip`

> 需要在请求头携带 `Authorization: Bearer <token>`

#### 请求参数（Request Body）

```json
{
  "petId": "p1a2b3c4-d5e6-7890-abcd-ef1234567890",
  "slotType": "armor"
}
```

| 字段 | 类型 | 必填 | 校验规则 | 说明 |
|------|------|:---:|------|------|
| `petId` | `string` | ✅ | UUID 格式 | 战宠实例 ID |
| `slotType` | `string` | ✅ | 枚举：`armor` / `accessory` | 要卸下的装备槽位 |

> **说明**：卸下后装备回到角色背包。后端需校验背包是否有空位。

#### 成功响应

```json
{
  "code": 200,
  "message": "装备已卸下",
  "data": null
}
```

#### 失败响应示例

```json
{
  "code": 400,
  "message": "该槽位没有装备",
  "data": null
}
```

```json
{
  "code": 400,
  "message": "背包已满，无法卸下装备",
  "data": null
}
```

---

## 18. 获取战宠图鉴列表

### `GET /pet/encyclopedia`

> 需要在请求头携带 `Authorization: Bearer <token>`

> 此接口返回所有战宠类型的静态数据，前端用于图鉴展示。建议后端做 Redis 缓存，避免频繁查询。

#### 请求参数

无

#### 成功响应

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "petTypeId": 1001,
      "name": "火焰精灵",
      "element": 1,
      "rarity": 3,
      "description": "诞生于火山深处的精灵，浑身燃烧着永不熄灭的火焰。",
      "baseHp": 80,
      "baseAttack": 25,
      "baseDefense": 12,
      "baseSpeed": 18,
      "hpGrowth": 16,
      "attackGrowth": 3,
      "defenseGrowth": 1.3,
      "speedGrowth": 1.1,
      "evolveTo": 1002,
      "evolveLevel": 25,
      "icon": "/assets/pet/1001.png",
      "owned": true
    }
  ]
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `data` | `PetTypeInfo[]` | 所有战宠类型数组 |
| `data[].petTypeId` | `number` | 战宠类型 ID |
| `data[].name` | `string` | 战宠名称 |
| `data[].element` | `number` | 属性：1-火, 2-水, 3-风, 4-地, 5-光, 6-暗 |
| `data[].rarity` | `number` | 品质：1-N, 2-R, 3-SR, 4-SSR |
| `data[].description` | `string` | 背景描述 |
| `data[].baseHp` | `number` | 基础生命 |
| `data[].baseAttack` | `number` | 基础攻击 |
| `data[].baseDefense` | `number` | 基础防御 |
| `data[].baseSpeed` | `number` | 基础速度 |
| `data[].hpGrowth` | `number` | 每级生命成长 |
| `data[].attackGrowth` | `number` | 每级攻击成长 |
| `data[].defenseGrowth` | `number` | 每级防御成长 |
| `data[].speedGrowth` | `number` | 每级速度成长 |
| `data[].evolveTo` | `number` | 进化目标类型 ID，0 为最终形态 |
| `data[].evolveLevel` | `number` | 进化所需等级，0 为不可进化 |
| `data[].icon` | `string` | 图标资源路径 |
| `data[].owned` | `boolean` | 当前角色是否已拥有该类型 |

> **说明**：`owned` 字段用于前端图鉴的"已收集/未收集"状态展示。未收集的战宠显示为剪影或问号。

---

## 前端数据结构参考

### 战宠信息对象

```typescript
/** 战宠实例信息 */
interface PetInfo {
  id: string                  // 战宠实例 UUID
  characterId: string         // 所属角色 UUID
  petTypeId: number           // 战宠类型 ID
  nickname: string            // 战宠昵称
  level: number               // 等级
  experience: number          // 当前经验值
  isActive: boolean           // 是否为出战战宠
  rarity: number              // 品质：1-N, 2-R, 3-SR, 4-SSR
  hp: number                  // 生命值
  attack: number              // 攻击力
  defense: number             // 防御力
  speed: number               // 速度
  skillSlots: number[]        // 技能槽（3 个位置，0 表示空）
  equipSlots: PetEquipSlots   // 装备槽
  bonusToOwner: PetBonus      // 给主人的属性加成
  createTime: string          // 获得时间（ISO 8601）
  updateTime: string          // 更新时间（ISO 8601）
}

/** 战宠装备槽 */
interface PetEquipSlots {
  armor: string | null        // 护甲位，装备 UUID 或 null
  accessory: string | null    // 饰品位，装备 UUID 或 null
}

/** 战宠给主人的属性加成 */
interface PetBonus {
  hp: number                  // 额外生命值
  attack: number              // 额外攻击力
  defense: number             // 额外防御力
}

/** 战宠进化信息 */
interface PetEvolveInfo {
  canEvolve: boolean          // 是否满足进化条件
  evolveToTypeId: number      // 进化目标类型 ID
  evolveRequiredLevel: number // 进化所需等级
  evolveRequiredItems: EvolveItem[] // 进化所需材料
}

/** 进化所需材料 */
interface EvolveItem {
  itemId: number              // 材料 ID
  itemName: string            // 材料名称
  required: number            // 所需数量
  owned: number               // 已持有数量
}
```

### 战宠属性编号对照表

**属性（element）：**

| 值 | 属性 |
|:---:|------|
| `1` | 火 |
| `2` | 水 |
| `3` | 风 |
| `4` | 地 |
| `5` | 光 |
| `6` | 暗 |

**品质（rarity）：**

| 值 | 品质 | 颜色标识 | 属性倍率 | 主人加成倍率 |
|:---:|------|------|:---:|:---:|
| `1` | 普通 (N) | 白色 | 100% | 100% |
| `2` | 稀有 (R) | 蓝色 | 120% | 120% |
| `3` | 史诗 (SR) | 紫色 | 150% | 150% |
| `4` | 传说 (SSR) | 橙色 | 200% | 200% |

**装备槽位（slotType）：**

| 值 | 说明 |
|------|------|
| `armor` | 护甲位 |
| `accessory` | 饰品位 |

### 前端校验规则汇总（战宠模块）

| 字段 | 前端校验 | 建议后端也校验 |
|------|----------|:---:|
| 战宠昵称 | 非空，2-8 字符 | ✅ |
| 喂食数量 | 1-99 | ✅ |
| 技能槽位索引 | 0-2 | ✅ |
| 技能 ID | 已学会的技能 | ✅ |
| 装备槽位类型 | armor / accessory | ✅ |
| 背包战宠数量 | ≤ 3 | ✅（后端必须再次校验） |

---

## 补充说明

### 前端存储的用户对象

```typescript
interface UserInfo {
  id: string       // 用户 UUID
  username: string // 用户名
  token: string    // JWT Token
}
```

前端会将此对象以 `JSON.stringify` 存入 `localStorage`，key 为 `auth_user`。

### 角色信息对象

```typescript
interface CharacterInfo {
  id: string                // 角色 UUID
  userId: string            // 所属用户 UUID
  characterName: string     // 角色名
  profession: number        // 职业类型：1-战士, 2-法师, 3-猎人
  level: number             // 等级
  experience: number        // 经验值
  strength: number          // 力量
  intelligence: number      // 智力
  agility: number           // 敏捷
  hp: number                // 生命值
  mp: number                // 魔法值
  physicalAttack: number    // 物理攻击力
  magicAttack: number       // 魔法攻击力
  defense: number           // 防御力
  dodgeRate: number         // 闪避率
  criticalRate: number      // 暴击率
  bonusHp: number           // 战宠加成-生命值
  bonusPhysicalAttack: number  // 战宠加成-物理攻击
  bonusMagicAttack: number     // 战宠加成-魔法攻击
  bonusDefense: number         // 战宠加成-防御力
  createTime: string        // 创建时间（ISO 8601）
  updateTime: string        // 更新时间（ISO 8601）
}
```

> **字段命名说明**：后端使用 `snake_case`（如 `character_name`），JeecgBoot 自动转为 `camelCase`（如 `characterName`），前端统一使用 `camelCase`。

### 职业编号对照表

| profession 值 | 职业 | 主属性 | 初始属性 (STR/INT/AGI) |
|:---:|------|------|------|
| `1` | 战士 (Warrior) | 力量 | 10 / 3 / 5 |
| `2` | 法师 (Mage) | 智力 | 2 / 12 / 4 |
| `3` | 猎人 (Hunter) | 敏捷 | 5 / 4 / 11 |

### 前端校验规则汇总

| 字段 | 前端校验 | 建议后端也校验 |
|------|----------|:---:|
| 用户名 | 非空，3-20 字符 | ✅ |
| 密码 | 非空，6-20 字符 | ✅ |
| 确认密码 | 与密码一致（仅前端校验，不传后端） | — |
| 角色名 | 非空，2-12 字符 | ✅ |
| 职业类型 | 枚举值 1/2/3 | ✅ |
| 角色数量 | ≤ 3 个（前端隐藏创建入口） | ✅（后端必须再次校验） |

---

## 补充说明

1. **密码安全**：当前前端明文传输密码，建议上线前改为 HTTPS，或前端做一层加密后传输
2. **Token 有效期**：建议后端设置合理的 JWT 过期时间（如 24h），前端收到 401 时自动跳转登录页
3. **CORS**：后端需配置跨域允许，开发环境前端地址为 `http://localhost:5173`
