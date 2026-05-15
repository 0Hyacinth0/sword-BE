# 团队副本 Boss 战 - 后端对接文档

## 概述

团队副本 Boss 战在现有多人副本基础上增加 Boss 专属机制：狂暴计时、阶段切换、全屏 AOE、队友复活。

---

## 1. Boss 配置 API

### 1.1 获取 Boss 配置

```
GET /boss/:bossId/config
```

**响应：**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "bossId": "boss-shadow-dragon",
    "bossName": "暗影龙",
    "phases": [
      { "phase": 1, "hpThreshold": 1.0, "attackMultiplier": 1.0, "skillIds": [9001, 9003] },
      { "phase": 2, "hpThreshold": 0.7, "attackMultiplier": 1.2, "skillIds": [9001, 9003, 9011] },
      { "phase": 3, "hpThreshold": 0.3, "attackMultiplier": 1.5, "skillIds": [9011, 9012] }
    ],
    "enrage": { "enrageRound": 20, "attackMultiplier": 2.5, "speedMultiplier": 2.0 },
    "revive": { "reviveHpPercent": 30, "mpCost": 25, "maxRevives": 3 },
    "aoeSkillIds": [9011]
  }
}
```

---

## 2. 复活 API

### 2.1 复活队友

```
POST /battle/revive
```

**请求：**

```json
{
  "battleId": "battle-001",
  "targetUid": "ally-member-002"
}
```

**响应：**

```json
{
  "code": 200,
  "message": "复活成功",
  "data": {
    "targetUid": "ally-member-002",
    "newHp": 36,
    "reviveCount": 1
  }
}
```

**后端逻辑：**
1. 校验 battleId 存在且为 Boss 战斗
2. 校验 reviveCount < maxRevives
3. 校验玩家 MP >= mpCost
4. 设置目标 HP = maxHp × reviveHpPercent%
5. 增加 reviveCount
6. 扣减玩家 MP

---

## 3. 阶段/狂暴通知（可选 WebSocket）

```json
{
  "type": "boss_phase_change",
  "battleId": "battle-001",
  "newPhase": 2,
  "attackMultiplier": 1.2
}

{
  "type": "boss_enrage",
  "battleId": "battle-001",
  "attackMultiplier": 2.5,
  "speedMultiplier": 2.0
}
```

---

## 4. 前端 Mock 行为

| 功能 | Mock 实现 |
|:---|:---|:---|
| Boss 配置 | 前端 boss_config.ts 预定义 |
| 阶段切换 | battleEngine.ts 每回合检测 HP 百分比 |
| 狂暴触发 | battleEngine.ts 检测回合数 |
| 复活处理 | bossMechanics.ts 本地执行 |
| AOE 技能 | 复用现有 targetType='all_enemies' |

后端对接后，Boss 配置从 API 获取，复活请求发送到后端校验。

---

## 5. 类型定义

参见 `src/types/boss.ts`

## 6. 团队 Boss 本配置

参见 `src/config/dungeon_config.ts` 的 `TEAM_BOSS_DUNGEONS` 数组