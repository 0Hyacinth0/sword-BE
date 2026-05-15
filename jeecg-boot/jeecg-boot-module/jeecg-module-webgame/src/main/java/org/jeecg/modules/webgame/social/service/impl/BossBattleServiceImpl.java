package org.jeecg.modules.webgame.social.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.webgame.social.entity.WgBossBattle;
import org.jeecg.modules.webgame.social.entity.WgBossConfig;
import org.jeecg.modules.webgame.social.entity.WgBossRevive;
import org.jeecg.modules.webgame.social.mapper.WgBossBattleMapper;
import org.jeecg.modules.webgame.social.mapper.WgBossConfigMapper;
import org.jeecg.modules.webgame.social.mapper.WgBossReviveMapper;
import org.jeecg.modules.webgame.social.service.IBossBattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description: 团队副本Boss战 Service 实现
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Slf4j
@Service
public class BossBattleServiceImpl implements IBossBattleService {
    
    @Autowired
    private WgBossConfigMapper bossConfigMapper;
    
    @Autowired
    private WgBossBattleMapper bossBattleMapper;
    
    @Autowired
    private WgBossReviveMapper bossReviveMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> initBossBattle(String battleId, String bossId) {
        log.info("初始化Boss战斗, battleId: {}, bossId: {}", battleId, bossId);
        
        // 1. 查询Boss配置
        LambdaQueryWrapper<WgBossConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgBossConfig::getBossId, bossId);
        WgBossConfig config = bossConfigMapper.selectOne(wrapper);
        if (config == null) {
            throw new JeecgBootException("Boss配置不存在");
        }
        
        // 2. 创建Boss战斗实例
        WgBossBattle battle = new WgBossBattle();
        battle.setId(UUID.randomUUID().toString());
        battle.setBattleId(battleId);
        battle.setBossId(bossId);
        battle.setCurrentPhase(1);
        battle.setCurrentHp(getBossMaxHp(bossId)); // TODO: 从配置获取
        battle.setEnrageTimer(getEnrageTime(bossId)); // TODO: 从配置获取
        battle.setStartTime(new Date());
        battle.setCreatedAt(new Date());
        battle.setUpdatedAt(new Date());
        
        bossBattleMapper.insert(battle);
        
        // 3. 构建返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("bossId", bossId);
        result.put("bossName", config.getBossName());
        result.put("currentPhase", battle.getCurrentPhase());
        result.put("currentHp", battle.getCurrentHp());
        result.put("maxHp", getBossMaxHp(bossId));
        result.put("enrageTimer", battle.getEnrageTimer());
        result.put("phases", parsePhases(config.getPhasesJson())); // TODO: 解析JSON
        
        log.info("Boss战斗初始化成功, battleId: {}", battleId);
        return result;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> switchPhase(String battleId, Integer newPhase) {
        log.info("切换Boss阶段, battleId: {}, newPhase: {}", battleId, newPhase);
        
        // 1. 查询Boss战斗实例
        LambdaQueryWrapper<WgBossBattle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgBossBattle::getBattleId, battleId);
        WgBossBattle battle = bossBattleMapper.selectOne(wrapper);
        if (battle == null) {
            throw new JeecgBootException("Boss战斗实例不存在");
        }
        
        // 2. 更新阶段
        battle.setCurrentPhase(newPhase);
        battle.setUpdatedAt(new Date());
        bossBattleMapper.updateById(battle);
        
        // 3. 构建返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("currentPhase", newPhase);
        result.put("phaseEffects", getPhaseEffects(newPhase)); // TODO: 实现阶段效果
        
        log.info("Boss阶段切换完成, battleId: {}, phase: {}", battleId, newPhase);
        return result;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> triggerEnrage(String battleId) {
        log.info("触发狂暴, battleId: {}", battleId);
        
        // 1. 查询Boss战斗实例
        LambdaQueryWrapper<WgBossBattle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgBossBattle::getBattleId, battleId);
        WgBossBattle battle = bossBattleMapper.selectOne(wrapper);
        if (battle == null) {
            throw new JeecgBootException("Boss战斗实例不存在");
        }
        
        // 2. 设置狂暴倒计时为0
        battle.setEnrageTimer(0);
        battle.setUpdatedAt(new Date());
        bossBattleMapper.updateById(battle);
        
        // 3. 构建返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("enraged", true);
        result.put("damageMultiplier", 2.0); // 伤害翻倍
        result.put("message", "Boss进入狂暴状态！");
        
        log.info("Boss狂暴触发, battleId: {}", battleId);
        return result;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> reviveTeammate(String battleId, String reviverId, String revivedId) {
        log.info("复活队友, battleId: {}, reviverId: {}, revivedId: {}", battleId, reviverId, revivedId);
        
        // 1. 检查复活次数限制（简化实现，默认每人最多被复活3次）
        int maxRevives = 3;
        long reviveCount = getReviveCount(battleId, revivedId);
        if (reviveCount >= maxRevives) {
            throw new JeecgBootException("该队友已达到最大复活次数");
        }
        
        // 2. 记录复活
        WgBossRevive revive = new WgBossRevive();
        revive.setId(UUID.randomUUID().toString());
        revive.setBattleId(battleId);
        revive.setRevivedCharacterId(revivedId);
        revive.setReviverCharacterId(reviverId);
        revive.setReviveTime(new Date());
        
        bossReviveMapper.insert(revive);
        
        // 3. 构建返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("revived", true);
        result.put("remainingRevives", maxRevives - reviveCount - 1);
        result.put("hpPercent", 50); // 复活后恢复50%血量
        
        log.info("队友复活成功, battleId: {}, revivedId: {}", battleId, revivedId);
        return result;
    }
    
    @Override
    public Map<String, Object> useAoeSkill(String battleId, String skillId) {
        log.info("使用全屏AOE技能, battleId: {}, skillId: {}", battleId, skillId);
        
        // 1. 查询Boss战斗实例
        LambdaQueryWrapper<WgBossBattle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgBossBattle::getBattleId, battleId);
        WgBossBattle battle = bossBattleMapper.selectOne(wrapper);
        if (battle == null) {
            throw new JeecgBootException("Boss战斗实例不存在");
        }
        
        // 2. 计算AOE伤害（简化实现）
        int baseDamage = 1000;
        double damageMultiplier = battle.getEnrageTimer() != null && battle.getEnrageTimer() <= 0 ? 2.0 : 1.0;
        int actualDamage = (int) (baseDamage * damageMultiplier);
        
        // 3. 构建返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("skillId", skillId);
        result.put("damage", actualDamage);
        result.put("isEnraged", damageMultiplier > 1.0);
        result.put("message", "Boss释放全屏AOE技能！");
        
        log.info("AOE技能使用完成, battleId: {}, damage: {}", battleId, actualDamage);
        return result;
    }
    
    // ==================== 私有辅助方法 ====================
    
    /**
     * 获取Boss最大血量
     */
    private long getBossMaxHp(String bossId) {
        // TODO: 从Boss配置表获取
        // 简化实现：默认100000
        return 100000L;
    }
    
    /**
     * 获取狂暴时间
     */
    private int getEnrageTime(String bossId) {
        // TODO: 从Boss配置表获取
        // 简化实现：默认300秒（5分钟）
        return 300;
    }
    
    /**
     * 解析阶段配置
     */
    private List<Map<String, Object>> parsePhases(String phasesJson) {
        // TODO: 解析JSON字符串
        List<Map<String, Object>> phases = new ArrayList<>();
        // 简化实现：返回空列表
        return phases;
    }
    
    /**
     * 获取阶段效果
     */
    private Map<String, Object> getPhaseEffects(Integer phase) {
        Map<String, Object> effects = new HashMap<>();
        // TODO: 根据阶段返回不同效果
        effects.put("phase", phase);
        effects.put("description", "阶段" + phase + "效果");
        return effects;
    }
    
    /**
     * 获取复活次数
     */
    private long getReviveCount(String battleId, String characterId) {
        LambdaQueryWrapper<WgBossRevive> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WgBossRevive::getBattleId, battleId)
               .eq(WgBossRevive::getRevivedCharacterId, characterId);
        return bossReviveMapper.selectCount(wrapper);
    }
}
