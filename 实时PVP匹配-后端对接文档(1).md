# 实时 PVP 匹配 - 后端对接文档

## 概述

PVP 匹配系统支持玩家发起竞技匹配，根据 Elo 积分寻找对手，战斗后结算积分变化。

---

## 1. API 接口

### 1.1 发起匹配

POST /arena/match

请求体：
```json
{
  "score": 2050
}
```

响应：
```json
{
  "code": 200,
  "message": "匹配成功",
  "data": {
    "characterId": "pvp-opp-001",
    "characterName": "火焰法师",
    "profession": "Mage",
    "level": 33,
    "tier": "diamond",
    "subTier": "III",
    "score": 2450
  }
}
```

### 1.2 提交战斗结算

POST /arena/settle

请求体：
```json
{
  "opponentId": "pvp-opp-001",
  "won": true
}
```

响应：
```json
{
  "code": 200,
  "message": "结算成功",
  "data": {
    "scoreChange": 35,
    "oldScore": 2050,
    "newScore": 2085,
    "tierChanged": false,
    "oldTier": { "tier": "platinum", "subTier": "II", "tierName": "◆ 铂金 II", "progress": 0.14, "remainingScore": 115 },
    "newTier": { "tier": "platinum", "subTier": "II", "tierName": "◆ 铂金 II", "progress": 0.48, "remainingScore": 81 }
  }
}
```

---

## 2. 积分算法

简化 Elo：
- 基础变化 ±25 分
- 积分差距调整 `(opponentScore - myScore) / 100`，范围 ±10
- 最终范围 ±15 ~ ±35
- 击败高分对手获得更多积分，输给低分对手扣除更多积分

---

## 3. 前端 Mock 行为

| 功能 | Mock 实现 |
|:---|:---|
| 匹配搜索 | 3-8 秒随机延迟，从 6 人对手池选取 |
| 对手选取 | 优先积分接近的对手（前 3 中随机） |
| 对手属性 | 根据职业/等级生成战斗属性 |
| 对手技能 | 每个职业 3 个技能（物理/魔法/混合） |
| 积分结算 | 前端计算，更新 arena store |

---

## 4. 类型定义

参见 src/types/pvp.ts

---

## 5. 段位体系

| 段位 | 积分范围 |
|:---|:---|
| 青铜 | 0-599 |
| 白银 | 600-1199 |
| 黄金 | 1200-1799 |
| 铂金 | 1800-2399 |
| 钻石 | 2400-2799 |
| 王者 | 2800+ |
