# 非实时 PVP（挑战好友镜像） - 后端对接文档

## 概述

好友面板新增"挑战"功能，玩家可挑战好友的镜像进行异步 PVP 对战。纯娱乐玩法，不结算积分。

---

## 1. 机制说明

| 项目 | 说明 |
|:---|:---|
| 入口 | 好友面板好友卡片"挑战"按钮 |
| 对手数据 | 基于好友 profession + level 前端 Mock 生成镜像属性 |
| 积分结算 | 无（纯娱乐） |
| 战斗模式 | 复用现有战斗引擎，AI 控制对手行动 |

---

## 2. 前端实现

- 镜像属性通过 `generateFriendMirrorCombatant(friend)` 生成
- 复用 `generateOpponentStats` 和 `getOpponentSkills` 函数
- 战斗使用 `battleStore.startWildBattle()` 发起

---

## 3. 后端对接

**无需后端新增 API**。当前实现使用前端 Mock 数据生成好友镜像属性。

**后续可扩展（建议后端提供）：**

| 接口 | 方法 | 说明 |
|:---|:---:|:---|
| `/friend/{characterId}/mirror` | GET | 获取好友真实属性快照（替代前端 Mock） |
| `/pvp/async-challenge` | POST | 记录异步挑战记录 |

### 建议的 `/friend/{characterId}/mirror` 响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "characterId": "uuid",
    "characterName": "好友角色名",
    "profession": "Warrior",
    "level": 25,
    "stats": {
      "maxHp": 900,
      "maxMp": 400,
      "physicalAttack": 200,
      "magicAttack": 100,
      "defense": 160,
      "speed": 10,
      "dodgeRate": 0.05,
      "criticalRate": 0.08
    },
    "skills": [
      { "id": 1, "name": "猛击", "mpCost": 8, "power": 1.3, "targetType": "single_enemy", "cooldown": 0, "element": 0 }
    ]
  }
}
```

---

## 4. 前端文件清单

| 文件 | 职责 |
|:---|:---|
| `src/config/pvp_config.ts` | generateFriendMirrorCombatant 镜像生成 |
| `src/stores/social.ts` | startAsyncPvpBattle 发起战斗 |
| `src/components/social/FriendPanel.vue` | 挑战按钮 + 确认弹窗 |
| `src/views/HomeView.vue` | 连接战斗视图跳转 |
