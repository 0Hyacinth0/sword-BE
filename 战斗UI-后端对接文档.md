# 战斗 UI - 后端对接文档

## 概述

本文档定义回合制战斗界面的 UI 数据结构、组件状态和样式规范。后端返回的战斗状态数据需满足前端渲染需求，确保界面一致性。

---

## 战斗界面结构

```
┌─────────────────────────────────────────────────┐
│  回合指示器（R1 / 当前阶段）                      │
├─────────────────────────────────────────────────┤
│  行动顺序条（预览接下来 5-8 位行动者）            │
├─────────────────────────────────────────────────┤
│           ┌── 敌方区域 ──┐                       │
│  VS 分隔  │  敌方单位卡片 │   我方区域           │
│           └── 敌方单位卡片 │                     │
│           └───────────────┘                       │
├─────────────────────────────────────────────────┤
│  行动面板（玩家回合时显示）                       │
├─────────────────────────────────────────────────┤
│  战斗日志（最近 50 条记录）                       │
└─────────────────────────────────────────────────┘
```

---

## 核心数据结构

### 1. 参战单位 (Combatant)

```typescript
interface Combatant {
  /** 唯一 ID（战斗实例内的临时标识） */
  uid: string
  /** 原始 ID（角色/战宠/怪物 ID） */
  sourceId: string
  /** 显示名称 */
  name: string
  /** 阵营：ally / enemy */
  side: 'ally' | 'enemy'
  /** 类型：player / pet / enemy */
  type: 'player' | 'pet' | 'enemy'
  /** 战斗属性 */
  stats: CombatantStats
  /** 已学会的技能列表 */
  skills: BattleSkill[]
  /** 当前 Buff/Debuff 列表 */
  buffs: BuffEffect[]
  /** 技能冷却 { skillId: remainingTurns } */
  cooldowns: Record<string, number>
  /** 是否存活 */
  isAlive: boolean
  /** 行动值（ATB 系统累积量） */
  actionValue: number
  /** 图标 URL（可选，用于替代默认图标） */
  iconUrl?: string
  /** 等级（用于 UI 显示，可选） */
  level?: number
}
```

### 2. 战斗属性 (CombatantStats)

```typescript
interface CombatantStats {
  maxHp: number
  hp: number
  maxMp: number
  mp: number
  physicalAttack: number
  magicAttack: number
  defense: number
  speed: number
  dodgeRate: number      // 0~1，闪避率
  criticalRate: number   // 0~1，暴击率
}
```

### 3. Buff 效果 (BuffEffect)

```typescript
interface BuffEffect {
  uid: string             // Buff 实例唯一 ID
  name: string            // 显示名称
  isDebuff: boolean       // 是否为 Debuff
  stat: BuffStat          // 影响的属性
  value: number           // 效果值
  duration: number        // 总持续回合
  remainingTurns: number  // 剩余回合
  sourceSkillId?: number  // 来源技能 ID
}

type BuffStat = 'physicalAttack' | 'magicAttack' | 'defense' | 'speed' | 'dodgeRate' | 'criticalRate' | 'maxHp'
```

### 4. 行动顺序预览 (ActionOrderEntry)

用于行动顺序条的预览数据：

```typescript
interface ActionOrderEntry {
  uid: string
  name: string
  side: 'ally' | 'enemy'
  type: 'player' | 'pet' | 'enemy'
  actionValue: number       // 当前行动值
  actionValuePercent: number // 0~1，达到 1 时可行动
  isAlive: boolean
}
```

---

## HP/MP 条颜色规则

遵循 DESIGN.md 第 4.5 节生命值条规范：

| HP 百分比 | 颜色 | CSS 变量 |
|-----------|------|----------|
| ≥50% | 绿色 | `linear-gradient(90deg, #34c759, #32d74b)` |
| 25%~50% | 金色 | `linear-gradient(90deg, #f59e0b, #ffd60a)` |
| <25% | 红色 | `linear-gradient(90deg, #ff3b30, #ff453a)` |

MP 条统一使用蓝色：`linear-gradient(90deg, #0071e3, #2997ff)`

---

## 战斗日志类型着色

| 日志类型 | 颜色 | 说明 |
|----------|------|------|
| `system` | 灰色斜体 | 系统消息 |
| `action` | 主色 | 行动描述 |
| `damage` | 红色 | 伤害输出 |
| `heal` | 绿色 | 治疗回复 |
| `buff` | 蓝色 | Buff/Debuff 应用 |
| `death` | 红色加粗 | 单位阵亡 |
| `dodge` | 灰色 | 闪避成功 |
| `critical` | 金色加粗 | 暴击命中 |
| `miss` | 灰色 | 攻击未命中 |
| `flee` | 金色 | 逃跑成功 |
| `reward` | 绿色加粗 | 获得奖励 |

---

## 战斗阶段 UI 状态

| 阶段 | 标签颜色 | 行动面板状态 |
|------|----------|--------------|
| `ROUND_START` | 绿色 | 隐藏 |
| `BUFF_SETTLEMENT` | 紫色 | 隐藏 |
| `ACTION_SELECT` | 蓝色 | 显示（玩家回合） |
| `DAMAGE_CALCULATION` | 蓝色 | 隐藏 |
| `DODGE_CRIT_CHECK` | 蓝色 | 隐藏 |
| `SETTLEMENT` | 金色 | 隐藏 |
| `ROUND_END` | 金色 | 隐藏 |
| `BATTLE_END` | 红色 | 隐藏，显示结算弹窗 |

---

## 技能按钮状态判定

前端根据以下条件判定技能按钮是否可用：

```
disabled = skill.type === 'passive'
        || currentMp < skill.mpCost
        || cooldowns[skill.id] > 0
```

后端需确保返回正确的 `currentMp` 和 `cooldowns` 数据。

---

## 目标选择数据

行动面板的目标选择区域需要后端返回：

```typescript
interface TargetSelection {
  /** 可选目标列表 */
  targets: Combatant[]
  /** 默认目标 UID（可选，前端会自动选第一个存活的敌人） */
  defaultTargetUid?: string
}
```

目标筛选规则：
- 攻击/单体攻击技能：存活敌人列表
- 单体治疗技能：存活友方列表（不含自己）
- 自身技能：无目标选择

---

## 伤害飘字动画

前端在收到伤害结果后触发飘字动画：

```typescript
interface DamageResult {
  targetUid: string
  value: number
  isCritical: boolean
  isDodged: boolean
  isHeal: boolean
  sourceType: 'attack' | 'skill' | 'item' | 'buff'
}
```

| 结果 | 显示内容 | 样式 |
|------|----------|------|
| 普通伤害 | `-{value}` | 红色，24px |
| 暴击伤害 | `-{value}` | 金色，32px，带光晕 |
| 闪避 | `MISS` | 灰色，18px |
| 治疗 | `+{value}` | 绿色，24px |

---

## 战斗结算数据

```typescript
interface BattleResult {
  outcome: 'victory' | 'defeat' | 'fled'
  rewards: {
    exp: number
    gold: number
    items: Array<{
      itemId: number
      name: string
      quantity: number
    }>
  } | null
}
```

---

## UI 元素尺寸规范

遵循 DESIGN.md 第 4.3 节卡片规范：

| 元素 | 圆角 | 内边距 |
|------|------|--------|
| 参战单位卡片 | `14px` | `12px 16px` |
| 行动面板 | `16px` | `16px 20px` |
| 战斗日志面板 | `14px` | `12px 16px` |
| 结算弹窗 | `20px` | `32px 36px` |
| 行动顺序卡片 | `10px` | `8px 12px` |

---

## 响应式布局

| 断点 | 调整 |
|------|------|
| ≤960px | 减少内边距，战场紧凑 |
| ≤720px | 战场单列布局，行动顺序横向滚动 |

---

## 注意事项

1. **实时更新**：战斗状态变化时，需及时推送 `BattleState` 更新
2. **Buff 可见性**：所有 Buff/Debuff 必须返回给前端展示
3. **冷却显示**：技能冷却需精确到回合数，前端显示 `CD: n`
4. **行动值同步**：ATB 系统的 `actionValue` 需每 tick 更新
5. **死亡状态**：单位死亡后 `isAlive = false`，前端自动渲染遮罩

---

*文档版本: 1.0.0*
*最后更新: 2026-05-07*