package org.jeecg.modules.webgame.character.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.webgame.character.dto.AttributePointDTO;
import org.jeecg.modules.webgame.character.dto.CreateCharacterDTO;
import org.jeecg.modules.webgame.character.entity.WgCharacter;
import org.jeecg.modules.webgame.character.mapper.WgCharacterMapper;
import org.jeecg.modules.webgame.character.service.IWgCharacterService;
import org.jeecg.modules.webgame.character.vo.CharacterVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 游戏角色Service实现
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Slf4j
@Service
public class WgCharacterServiceImpl extends ServiceImpl<WgCharacterMapper, WgCharacter> implements IWgCharacterService {

    /**
     * 职业基础属性配置
     * 格式: 职业ID -> {力量, 智力, 敏捷, HP, MP, 物攻, 魔攻, 防御, 闪避, 暴击}
     */
    private static final double[][] PROFESSION_BASE_STATS = {
        {},  // 0 - 占位
        {10, 2, 5, 150, 30, 15, 0, 8, 0.02, 0.05},   // 1 - 战士
        {3, 12, 6, 80, 100, 5, 18, 4, 0.05, 0.08},    // 2 - 法师
        {6, 5, 10, 100, 50, 12, 5, 6, 0.08, 0.10}     // 3 - 猎人
    };

    /**
     * 职业名称映射
     */
    private static final String[] PROFESSION_NAMES = {
        "", "战士", "法师", "猎人"
    };

    /**
     * 每级获得的属性点
     */
    private static final int POINTS_PER_LEVEL = 5;

    /**
     * 每个用户最多可创建的角色数量
     */
    private static final int MAX_CHARACTERS_PER_USER = 3;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CharacterVO createCharacter(String userId, CreateCharacterDTO createDTO) {
        // 1. 校验职业类型
        if (createDTO.getProfession() < 1 || createDTO.getProfession() > 3) {
            throw new JeecgBootException("职业类型无效，请选择正确的职业");
        }

        // 2. 检查用户是否已有角色（每个用户限创3个角色）
        LambdaQueryWrapper<WgCharacter> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WgCharacter::getUserId, userId);
        queryWrapper.eq(WgCharacter::getDelFlag, 0);
        Long count = this.count(queryWrapper);
        if (count >= MAX_CHARACTERS_PER_USER) {
            throw new JeecgBootException("每个用户最多只能创建" + MAX_CHARACTERS_PER_USER + "个角色");
        }

        // 3. 检查角色名是否重复
        LambdaQueryWrapper<WgCharacter> nameQuery = new LambdaQueryWrapper<>();
        nameQuery.eq(WgCharacter::getCharacterName, createDTO.getCharacterName());
        nameQuery.eq(WgCharacter::getDelFlag, 0);
        if (this.count(nameQuery) > 0) {
            throw new JeecgBootException("角色名称已存在，请更换名称");
        }

        // 4. 根据职业初始化属性
        double[] baseStats = PROFESSION_BASE_STATS[createDTO.getProfession()];
        
        WgCharacter character = new WgCharacter();
        character.setUserId(userId);
        character.setCharacterName(createDTO.getCharacterName());
        character.setProfession(createDTO.getProfession());
        character.setLevel(1);
        character.setExperience(0L);
        
        // 设置基础属性
        character.setStrength((int)baseStats[0]);
        character.setIntelligence((int)baseStats[1]);
        character.setAgility((int)baseStats[2]);
        character.setHp((int)baseStats[3]);
        character.setMp((int)baseStats[4]);
        character.setPhysicalAttack((int)baseStats[5]);
        character.setMagicAttack((int)baseStats[6]);
        character.setDefense((int)baseStats[7]);
        character.setDodgeRate(baseStats[8]);
        character.setCriticalRate(baseStats[9]);
        
        character.setCreateTime(new Date());
        character.setUpdateTime(new Date());
        character.setDelFlag(0);

        // 5. 保存角色
        this.save(character);
        log.info("创建角色成功: userId={}, characterName={}, profession={}", 
                userId, createDTO.getCharacterName(), createDTO.getProfession());

        // 6. 返回角色信息
        return getCharacterInfo(character.getId());
    }

    @Override
    public CharacterVO getCharacterInfo(String characterId) {
        // 1. 查询角色
        WgCharacter character = this.getById(characterId);
        if (character == null || character.getDelFlag() == 1) {
            throw new JeecgBootException("角色不存在");
        }

        // 2. 转换为 VO 并计算衍生属性
        return convertToVO(character);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CharacterVO addAttributePoints(String characterId, AttributePointDTO pointDTO) {
        // 1. 查询角色
        WgCharacter character = this.getById(characterId);
        if (character == null || character.getDelFlag() == 1) {
            throw new JeecgBootException("角色不存在");
        }

        // 2. 计算总加点数
        int totalPoints = (pointDTO.getStrengthPoint() == null ? 0 : pointDTO.getStrengthPoint()) +
                         (pointDTO.getIntelligencePoint() == null ? 0 : pointDTO.getIntelligencePoint()) +
                         (pointDTO.getAgilityPoint() == null ? 0 : pointDTO.getAgilityPoint());

        if (totalPoints <= 0) {
            throw new JeecgBootException("加点数量必须大于0");
        }

        // 3. 计算可用属性点
        int availablePoints = calculateAvailablePoints(character);

        // 4. 校验加点数是否足够
        if (totalPoints > availablePoints) {
            throw new JeecgBootException("可用属性点不足，当前可用: " + availablePoints);
        }

        // 5. 更新属性
        if (pointDTO.getStrengthPoint() != null && pointDTO.getStrengthPoint() > 0) {
            character.setStrength(character.getStrength() + pointDTO.getStrengthPoint());
        }
        if (pointDTO.getIntelligencePoint() != null && pointDTO.getIntelligencePoint() > 0) {
            character.setIntelligence(character.getIntelligence() + pointDTO.getIntelligencePoint());
        }
        if (pointDTO.getAgilityPoint() != null && pointDTO.getAgilityPoint() > 0) {
            character.setAgility(character.getAgility() + pointDTO.getAgilityPoint());
        }

        // 6. 重新计算衍生属性
        recalculateDerivedStats(character);

        // 7. 保存更新
        character.setUpdateTime(new Date());
        this.updateById(character);

        log.info("属性加点成功: characterId={}, strength+={}, intelligence+={}, agility+={}",
                characterId, 
                pointDTO.getStrengthPoint(),
                pointDTO.getIntelligencePoint(),
                pointDTO.getAgilityPoint());

        // 8. 返回更新后的角色信息
        return convertToVO(character);
    }

    @Override
    public List<CharacterVO> getCharacterList(String userId) {
        // 1. 查询用户的所有角色
        LambdaQueryWrapper<WgCharacter> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WgCharacter::getUserId, userId);
        queryWrapper.eq(WgCharacter::getDelFlag, 0);
        queryWrapper.orderByDesc(WgCharacter::getCreateTime);
        
        List<WgCharacter> characters = this.list(queryWrapper);

        // 2. 转换为 VO 列表
        return characters.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCharacter(String characterId, String userId) {
        // 1. 查询角色
        WgCharacter character = this.getById(characterId);
        if (character == null || character.getDelFlag() == 1) {
            throw new JeecgBootException("角色不存在");
        }

        // 2. 验证权限：只能删除自己的角色
        if (!character.getUserId().equals(userId)) {
            throw new JeecgBootException("无权删除该角色");
        }

        // 3. 软删除角色
        character.setDelFlag(1);
        character.setUpdateTime(new Date());
        this.updateById(character);

        log.info("删除角色成功: characterId={}, userId={}, characterName={}",
                characterId, userId, character.getCharacterName());
    }

    /**
     * 转换为 VO 并计算衍生属性
     */
    private CharacterVO convertToVO(WgCharacter character) {
        CharacterVO vo = new CharacterVO();
        
        // 基础信息
        vo.setId(character.getId());
        vo.setCharacterName(character.getCharacterName());
        vo.setProfession(character.getProfession());
        vo.setProfessionName(PROFESSION_NAMES[character.getProfession()]);
        vo.setLevel(character.getLevel());
        vo.setExperience(character.getExperience());
        
        // 计算下一级所需经验
        vo.setNextLevelExp(calculateNextLevelExp(character.getLevel()));
        
        // 计算可用属性点
        vo.setAvailablePoints(calculateAvailablePoints(character));
        
        // 基础属性
        vo.setStrength(character.getStrength());
        vo.setIntelligence(character.getIntelligence());
        vo.setAgility(character.getAgility());
        
        // 重新计算衍生属性
        vo.setHp(character.getHp());
        vo.setMaxHp(character.getHp());  // HP 就是最大 HP
        vo.setMp(character.getMp());
        vo.setMaxMp(character.getMp());  // MP 就是最大 MP
        vo.setPhysicalAttack(character.getPhysicalAttack());
        vo.setMagicAttack(character.getMagicAttack());
        vo.setDefense(character.getDefense());
        vo.setDodgeRate(character.getDodgeRate());
        vo.setCriticalRate(character.getCriticalRate());
        
        return vo;
    }

    /**
     * 计算下一级所需经验
     * 公式: 下一级经验 = 当前等级 * 100
     */
    private Long calculateNextLevelExp(Integer level) {
        return (long) level * 100;
    }

    /**
     * 计算可用属性点
     * 公式: 可用点 = (等级 - 1) * 每级点数 - 已分配点数
     */
    private int calculateAvailablePoints(WgCharacter character) {
        // 总获得点数 = (等级 - 1) * 每级点数
        int totalPoints = (character.getLevel() - 1) * POINTS_PER_LEVEL;
        
        // 已分配点数 = (当前力量 + 智力 + 敏捷) - 基础属性
        double[] baseStats = PROFESSION_BASE_STATS[character.getProfession()];
        int allocatedPoints = (character.getStrength() - (int)baseStats[0]) +
                             (character.getIntelligence() - (int)baseStats[1]) +
                             (character.getAgility() - (int)baseStats[2]);
        
        return totalPoints - allocatedPoints;
    }

    /**
     * 重新计算衍生属性
     * 根据基础属性（力量、智力、敏捷）计算 HP、MP、攻击力等
     */
    private void recalculateDerivedStats(WgCharacter character) {
        double[] baseStats = PROFESSION_BASE_STATS[character.getProfession()];
        
        // 计算属性增量
        int strengthDiff = character.getStrength() - (int)baseStats[0];
        int intelligenceDiff = character.getIntelligence() - (int)baseStats[1];
        int agilityDiff = character.getAgility() - (int)baseStats[2];
        
        // 根据职业特性计算衍生属性
        switch (character.getProfession()) {
            case 1: // 战士
                // 战士: 1点力量 = 10HP + 1物攻, 1点敏捷 = 0.5防御
                character.setHp((int)(baseStats[3] + strengthDiff * 10));
                character.setMp((int)baseStats[4]);  // 战士 MP 不变
                character.setPhysicalAttack((int)(baseStats[5] + strengthDiff));
                character.setMagicAttack((int)baseStats[6]);
                character.setDefense((int)(baseStats[7] + agilityDiff / 2));
                character.setDodgeRate(baseStats[8] + agilityDiff * 0.001);
                character.setCriticalRate(baseStats[9] + strengthDiff * 0.002);
                break;
                
            case 2: // 法师
                // 法师: 1点智力 = 8MP + 2魔攻, 1点敏捷 = 0.3防御 + 0.5%闪避
                character.setHp((int)baseStats[3]);  // 法师 HP 不变
                character.setMp((int)(baseStats[4] + intelligenceDiff * 8));
                character.setPhysicalAttack((int)baseStats[5]);
                character.setMagicAttack((int)(baseStats[6] + intelligenceDiff * 2));
                character.setDefense((int)(baseStats[7] + agilityDiff / 3));
                character.setDodgeRate(baseStats[8] + agilityDiff * 0.005);
                character.setCriticalRate(baseStats[9] + intelligenceDiff * 0.003);
                break;
                
            case 3: // 猎人
                // 猎人: 1点敏捷 = 5HP + 1物攻 + 1%闪避, 1点力量 = 0.5物攻
                character.setHp((int)(baseStats[3] + agilityDiff * 5));
                character.setMp((int)baseStats[4]);  // 猎人 MP 不变
                character.setPhysicalAttack((int)(baseStats[5] + agilityDiff + strengthDiff / 2));
                character.setMagicAttack((int)baseStats[6]);
                character.setDefense((int)(baseStats[7] + agilityDiff / 4));
                character.setDodgeRate(baseStats[8] + agilityDiff * 0.01);
                character.setCriticalRate(baseStats[9] + agilityDiff * 0.005);
                break;
        }
    }
}
