package org.jeecg.modules.webgame.battle.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.webgame.battle.dto.BattleActionDTO;
import org.jeecg.modules.webgame.battle.dto.StartBattleDTO;
import org.jeecg.modules.webgame.battle.entity.WgBattle;
import org.jeecg.modules.webgame.battle.entity.WgLevelConfig;
import org.jeecg.modules.webgame.battle.mapper.WgBattleMapper;
import org.jeecg.modules.webgame.battle.mapper.WgLevelConfigMapper;
import org.jeecg.modules.webgame.battle.service.IBattleService;
import org.jeecg.modules.webgame.battle.util.BattleEngine;
import org.jeecg.modules.webgame.battle.util.BattleEngine.DamageResult;
import org.jeecg.modules.webgame.battle.vo.*;
import org.jeecg.modules.webgame.character.entity.WgCharacter;
import org.jeecg.modules.webgame.character.mapper.WgCharacterMapper;
import org.jeecg.modules.webgame.pet.entity.CharacterPet;
import org.jeecg.modules.webgame.pet.entity.PetSkill;
import org.jeecg.modules.webgame.pet.entity.PetType;
import org.jeecg.modules.webgame.pet.mapper.CharacterPetMapper;
import org.jeecg.modules.webgame.pet.mapper.PetSkillMapper;
import org.jeecg.modules.webgame.pet.mapper.PetTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description: 战斗服务实现
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Slf4j
@Service
public class BattleServiceImpl implements IBattleService {

    @Autowired
    private WgBattleMapper battleMapper;

    @Autowired
    private WgCharacterMapper characterMapper;

    @Autowired
    private CharacterPetMapper characterPetMapper;

    @Autowired
    private PetSkillMapper petSkillMapper;

    @Autowired
    private PetTypeMapper petTypeMapper;

    @Autowired
    private WgLevelConfigMapper levelConfigMapper;

    @Override
    public BattleStateVO startBattle(StartBattleDTO dto) {
        log.info("发起战斗, characterId={}, enemyGroupId={}", dto.getCharacterId(), dto.getEnemyGroupId());

        // 1. 校验角色存在
        WgCharacter character = characterMapper.selectById(dto.getCharacterId());
        if (character == null) {
            throw new RuntimeException("角色不存在");
        }

        // 2. 创建战斗记录
        WgBattle battle = new WgBattle();
        battle.setBattleId(UUID.randomUUID().toString());
        battle.setCharacterId(dto.getCharacterId());
        battle.setActivePetId(dto.getActivePetId());
        battle.setStatus("in_progress");
        battle.setCurrentRound(1);
        battle.setBattlePhase("ROUND_START");
        battle.setStartTime(new Date());
        battle.setLastActionTime(new Date());
        battleMapper.insert(battle);

        // 3. 构建战斗状态
        BattleStateVO battleState = buildInitialBattleState(battle, character, dto);

        // 4. 保存战斗状态快照
        battle.setBattleStateJson(JSON.toJSONString(battleState));
        battleMapper.updateById(battle);

        log.info("战斗创建成功, battleId={}", battle.getBattleId());
        return battleState;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BattleStateVO submitAction(BattleActionDTO dto) {
        log.info("提交行动, battleId={}, type={}", dto.getBattleId(), dto.getType());

        // 1. 查询战斗记录
        WgBattle battle = battleMapper.selectByBattleId(dto.getBattleId());
        if (battle == null || !"in_progress".equals(battle.getStatus())) {
            throw new RuntimeException("战斗不存在或已结束");
        }

        // 2. 校验行动合法性
        validateAction(battle, dto);

        // 3. 执行行动逻辑（简化版）
        BattleStateVO battleState = executeAction(battle, dto);

        // 4. 更新战斗记录
        battle.setCurrentRound(battleState.getRound());
        battle.setBattlePhase(battleState.getPhase());
        battle.setLastActionTime(new Date());
        battle.setBattleStateJson(JSON.toJSONString(battleState));
        battleMapper.updateById(battle);

        // 5. 如果战斗结束，更新状态
        if ("victory".equals(battleState.getOutcome()) || "defeat".equals(battleState.getOutcome())) {
            battle.setStatus("completed");
            battle.setOutcome(battleState.getOutcome());
            battle.setEndTime(new Date());
            battleMapper.updateById(battle);
        }

        return battleState;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BattleEndVO endBattle(String battleId) {
        log.info("结束战斗, battleId={}", battleId);

        WgBattle battle = battleMapper.selectByBattleId(battleId);
        if (battle == null || !"completed".equals(battle.getStatus())) {
            throw new RuntimeException("战斗不存在或未结束");
        }

        BattleEndVO result = new BattleEndVO();
        result.setOutcome(battle.getOutcome());

        // 计算奖励
        if ("victory".equals(battle.getOutcome())) {
            BattleRewardsVO rewards = calculateRewards(battle);
            result.setRewards(rewards);

            // TODO: 发放奖励到角色（经验、金币、物品）
            // TODO: 更新角色等级
            // TODO: 战宠经验分配
            // TODO: 战宠HP恢复
        } else {
            result.setRewards(new BattleRewardsVO());
            result.getRewards().setExp(0);
            result.getRewards().setGold(0);
            result.getRewards().setItems(new ArrayList<>());
        }

        // 战斗统计
        BattleEndVO.BattleStatisticsVO statistics = new BattleEndVO.BattleStatisticsVO();
        statistics.setTotalRounds(battle.getCurrentRound());
        statistics.setTotalDamageDealt(battle.getTotalDamageDealt());
        statistics.setTotalDamageTaken(battle.getTotalDamageTaken());
        statistics.setTotalHealed(battle.getTotalHealed());
        statistics.setCriticalHits(battle.getCriticalHits());
        statistics.setDodgeCount(battle.getDodgeCount());
        statistics.setEnemiesKilled(battle.getEnemiesKilled());
        result.setStatistics(statistics);

        // 战宠HP恢复标记
        result.setPetHpRestored(battle.getActivePetId() != null);

        log.info("战斗结算完成, outcome={}", battle.getOutcome());
        return result;
    }

    @Override
    public SkillListVO getCharacterSkills(String characterId) {
        log.info("获取角色技能列表, characterId={}", characterId);

        WgCharacter character = characterMapper.selectById(characterId);
        if (character == null) {
            throw new RuntimeException("角色不存在");
        }

        // 根据职业获取技能（简化版，实际应根据职业和等级过滤）
        List<PetSkill> allSkills = petSkillMapper.selectAllPetSkills();

        SkillListVO result = new SkillListVO();
        List<BattleSkillVO> activeSkills = new ArrayList<>();
        List<BattleSkillVO> passiveSkills = new ArrayList<>();

        for (PetSkill skill : allSkills) {
            BattleSkillVO vo = convertToSkillVO(skill);
            if ("passive".equals(skill.getType())) {
                passiveSkills.add(vo);
            } else {
                activeSkills.add(vo);
            }
        }

        result.setActiveSkills(activeSkills);
        result.setPassiveSkills(passiveSkills);

        return result;
    }

    @Override
    public BattleStateVO getBattleState(String battleId) {
        WgBattle battle = battleMapper.selectByBattleId(battleId);
        if (battle == null) {
            throw new RuntimeException("战斗不存在");
        }

        if (battle.getBattleStateJson() != null) {
            return JSON.parseObject(battle.getBattleStateJson(), BattleStateVO.class);
        }

        throw new RuntimeException("战斗状态数据丢失");
    }

    // ==================== 私有方法 ====================

    /**
     * 构建初始战斗状态
     */
    private BattleStateVO buildInitialBattleState(WgBattle battle, WgCharacter character, StartBattleDTO dto) {
        BattleStateVO state = new BattleStateVO();
        state.setBattleId(battle.getBattleId());
        state.setPhase("ACTION_SELECT");
        state.setRound(1);
        state.setWaitingForPlayerAction(true);
        state.setLog(new ArrayList<>());
        state.setCombatants(new ArrayList<>());
        state.setActionOrder(new ArrayList<>());

        // 添加日志
        BattleLogEntryVO startLog = new BattleLogEntryVO();
        startLog.setRound(1);
        startLog.setType("system");
        startLog.setMessage("战斗开始！");
        startLog.setTimestamp(System.currentTimeMillis());
        state.getLog().add(startLog);

        // TODO: 根据enemyGroupId生成敌人
        // TODO: 添加角色和战宠到combatants
        // TODO: 按速度排序生成actionOrder

        // 简化版：添加一个测试敌人
        CombatantVO enemy = new CombatantVO();
        enemy.setUid("enemy-1");
        enemy.setSourceId("monster-001");
        enemy.setName("暗影狼");
        enemy.setSide("enemy");
        enemy.setType("enemy");
        enemy.setIsAlive(true);
        enemy.setActionValue(0);

        CombatantVO.CombatantStatsVO stats = new CombatantVO.CombatantStatsVO();
        stats.setMaxHp(80);
        stats.setHp(80);
        stats.setMaxMp(20);
        stats.setMp(20);
        stats.setPhysicalAttack(15);
        stats.setMagicAttack(3);
        stats.setDefense(6);
        stats.setSpeed(12);
        stats.setDodgeRate(0.08);
        stats.setCriticalRate(0.05);
        enemy.setStats(stats);
        enemy.setSkills(new ArrayList<>());
        enemy.setBuffs(new ArrayList<>());
        enemy.setCooldowns(new HashMap<>());

        state.getCombatants().add(enemy);

        // TODO: 添加角色Combatant
        // TODO: 如果有战宠，添加战宠Combatant

        return state;
    }

    /**
     * 校验行动合法性
     */
    private void validateAction(WgBattle battle, BattleActionDTO dto) {
        // TODO: 校验行动者是否存活
        // TODO: 校验目标是否合法
        // TODO: 校验MP是否足够
        // TODO: 校验技能是否在冷却中
    }

    /**
     * 执行行动逻辑
     */
    private BattleStateVO executeAction(WgBattle battle, BattleActionDTO dto) {
        // 解析战斗状态
        BattleStateVO state = JSON.parseObject(battle.getBattleStateJson(), BattleStateVO.class);
        
        if (state == null || state.getCombatants() == null) {
            throw new RuntimeException("战斗状态数据异常");
        }
        
        // 添加行动日志
        BattleLogEntryVO actionLog = new BattleLogEntryVO();
        actionLog.setRound(state.getRound());
        actionLog.setTimestamp(System.currentTimeMillis());
        actionLog.setActorUid(dto.getActorUid());
        
        // 根据行动类型执行不同逻辑
        switch (dto.getType()) {
            case "attack":
                executeAttack(state, battle, dto, actionLog);
                break;
            case "skill":
                executeSkill(state, dto, actionLog);
                break;
            case "defend":
                executeDefend(state, dto, actionLog);
                break;
            case "flee":
                executeFlee(state, dto, actionLog);
                break;
            default:
                throw new RuntimeException("未知的行动类型: " + dto.getType());
        }
        
        state.getLog().add(actionLog);
        
        // 检查战斗是否结束
        String outcome = BattleEngine.checkBattleEnd(state.getCombatants());
        if (outcome != null) {
            state.setOutcome(outcome);
            state.setPhase("BATTLE_END");
            state.setWaitingForPlayerAction(false);
            
            // 添加结束日志
            BattleLogEntryVO endLog = new BattleLogEntryVO();
            endLog.setRound(state.getRound());
            endLog.setType("system");
            endLog.setMessage("victory".equals(outcome) ? "战斗胜利！" : "战斗失败！");
            endLog.setTimestamp(System.currentTimeMillis());
            state.getLog().add(endLog);
        } else {
            // 继续下一回合
            state.setPhase("ACTION_SELECT");
            state.setWaitingForPlayerAction(true);
        }
        
        return state;
    }

    /**
     * 执行普通攻击
     */
    private void executeAttack(BattleStateVO state, WgBattle battle, BattleActionDTO dto, BattleLogEntryVO actionLog) {
        CombatantVO attacker = findCombatant(state, dto.getActorUid());
        CombatantVO defender = findCombatant(state, dto.getTargetUid());
        
        if (attacker == null || defender == null) {
            throw new RuntimeException("找不到参战单位");
        }
        
        // 计算伤害
        DamageResult damageResult = BattleEngine.calculateDamage(attacker, defender, 100);
        
        // 应用伤害
        if (!damageResult.getIsDodged()) {
            int newHp = Math.max(0, defender.getStats().getHp() - damageResult.getValue());
            defender.getStats().setHp(newHp);
            
            // 检查是否死亡
            if (newHp <= 0) {
                defender.setIsAlive(false);
                if ("enemy".equals(defender.getSide())) {
                    battle.setEnemiesKilled(battle.getEnemiesKilled() != null ? battle.getEnemiesKilled() + 1 : 1);
                }
            }
            
            // 更新统计数据
            battle.setTotalDamageDealt(battle.getTotalDamageDealt() != null ? 
                battle.getTotalDamageDealt() + damageResult.getValue() : damageResult.getValue());
        }
        
        // 生成日志
        actionLog.setType(damageResult.getIsDodged() ? "dodge" : 
                         damageResult.getIsCritical() ? "critical" : "damage");
        actionLog.setTargetUid(dto.getTargetUid());
        
        if (damageResult.getIsDodged()) {
            actionLog.setMessage(defender.getName() + " 闪避了 " + attacker.getName() + " 的攻击！");
        } else if (damageResult.getIsCritical()) {
            actionLog.setMessage(attacker.getName() + " 暴击 " + defender.getName() + 
                               "，造成 " + damageResult.getValue() + " 点伤害！");
            battle.setCriticalHits(battle.getCriticalHits() != null ? 
                battle.getCriticalHits() + 1 : 1);
        } else {
            actionLog.setMessage(attacker.getName() + " 攻击 " + defender.getName() + 
                               "，造成 " + damageResult.getValue() + " 点伤害");
        }
        
        // 记录伤害结果（用于动画）
        if (state.getLastDamageResults() == null) {
            state.setLastDamageResults(new ArrayList<>());
        }
        BattleStateVO.DamageResultVO animResult = new BattleStateVO.DamageResultVO();
        animResult.setTargetUid(damageResult.getTargetUid());
        animResult.setValue(damageResult.getValue());
        animResult.setIsCritical(damageResult.getIsCritical());
        animResult.setIsDodged(damageResult.getIsDodged());
        animResult.setIsHeal(false);
        animResult.setSourceType("attack");
        state.getLastDamageResults().add(animResult);
    }

    /**
     * 执行技能
     */
    private void executeSkill(BattleStateVO state, BattleActionDTO dto, BattleLogEntryVO actionLog) {
        if (dto.getSkillId() == null) {
            throw new RuntimeException("技能ID不能为空");
        }
        
        // TODO: 查询技能配置
        // TODO: 检查MP和冷却
        // TODO: 根据技能类型执行不同逻辑
        
        actionLog.setType("system");
        actionLog.setMessage("技能系统待实现");
    }

    /**
     * 执行防御
     */
    private void executeDefend(BattleStateVO state, BattleActionDTO dto, BattleLogEntryVO actionLog) {
        CombatantVO defender = findCombatant(state, dto.getActorUid());
        
        actionLog.setType("action");
        actionLog.setMessage(defender.getName() + " 进入防御姿态");
        
        // TODO: 添加防御Buff
    }

    /**
     * 执行逃跑
     */
    private void executeFlee(BattleStateVO state, BattleActionDTO dto, BattleLogEntryVO actionLog) {
        CombatantVO actor = findCombatant(state, dto.getActorUid());
        
        double fleeChance = BattleEngine.calculateFleeChance(actor.getStats().getSpeed());
        boolean success = Math.random() < fleeChance;
        
        actionLog.setType("flee");
        
        if (success) {
            actionLog.setMessage(actor.getName() + " 成功逃离了战斗！");
            state.setOutcome("fled");
            state.setPhase("BATTLE_END");
            state.setWaitingForPlayerAction(false);
        } else {
            actionLog.setMessage(actor.getName() + " 逃跑失败！");
        }
    }

    /**
     * 查找参战单位
     */
    private CombatantVO findCombatant(BattleStateVO state, String uid) {
        if (state.getCombatants() == null) {
            return null;
        }
        return state.getCombatants().stream()
                .filter(c -> c.getUid().equals(uid))
                .findFirst()
                .orElse(null);
    }

    /**
     * 计算战斗奖励
     */
    private BattleRewardsVO calculateRewards(WgBattle battle) {
        BattleRewardsVO rewards = new BattleRewardsVO();
        rewards.setExp(150);
        rewards.setGold(45);
        rewards.setItems(new ArrayList<>());

        // TODO: 根据击败的敌人计算经验和金币
        // TODO: 随机掉落物品
        // TODO: 战宠经验分配（30%）
        // TODO: 判断是否升级

        return rewards;
    }

    /**
     * 转换技能VO
     */
    private BattleSkillVO convertToSkillVO(PetSkill skill) {
        BattleSkillVO vo = new BattleSkillVO();
        vo.setId(skill.getSkillId());
        vo.setName(skill.getName());
        vo.setType(skill.getType());
        vo.setPower(skill.getPower());
        vo.setCooldown(skill.getCooldown());
        vo.setMpCost(skill.getMpCost());
        vo.setTargetType(skill.getTargetType());
        vo.setElement(skill.getElement());
        vo.setDescription(skill.getDescription());
        return vo;
    }
}
