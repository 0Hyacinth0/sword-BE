package org.jeecg.modules.webgame.character.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.webgame.character.dto.AddExperienceDTO;
import org.jeecg.modules.webgame.character.dto.AttributePointDTO;
import org.jeecg.modules.webgame.character.dto.CreateCharacterDTO;
import org.jeecg.modules.webgame.character.entity.WgCharacter;
import org.jeecg.modules.webgame.character.mapper.WgCharacterMapper;
import org.jeecg.modules.webgame.character.service.IWgCharacterService;
import org.jeecg.modules.webgame.character.util.CharacterStatsCalculator;
import org.jeecg.modules.webgame.character.util.ExperienceCalculator;
import org.jeecg.modules.webgame.character.vo.AddExperienceResultVO;
import org.jeecg.modules.webgame.character.vo.CharacterVO;
import org.jeecg.modules.webgame.character.vo.EquipmentVO;
import org.jeecg.modules.webgame.equipment.entity.CharacterEquipment;
import org.jeecg.modules.webgame.equipment.mapper.CharacterEquipmentMapper;
import org.jeecg.modules.webgame.item.entity.WgItemTemplate;
import org.jeecg.modules.webgame.item.mapper.WgItemTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 游戏角色Service实现
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Slf4j
@Service
public class WgCharacterServiceImpl extends ServiceImpl<WgCharacterMapper, WgCharacter> implements IWgCharacterService {

    @Autowired
    private CharacterEquipmentMapper characterEquipmentMapper;

    @Autowired
    private WgItemTemplateMapper wgItemTemplateMapper;

    /**
     * 职业基础属性配置（1级初始值）
     * 格式: 职业ID -> {力量, 智力, 敏捷}
     */
    private static final int[][] PROFESSION_BASE_ATTRIBUTES = {
        {},  // 0 - 占位
        {10, 3, 5},   // 1 - 战士
        {2, 12, 4},   // 2 - 法师
        {5, 4, 11}    // 3 - 猎人
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

        // 4. 根据职业初始化基础属性
        int[] baseAttrs = PROFESSION_BASE_ATTRIBUTES[createDTO.getProfession()];
        int str = baseAttrs[0];
        int intelligence = baseAttrs[1];
        int agi = baseAttrs[2];
        
        // 计算衍生属性
        CharacterStatsCalculator.DerivedStats derived = CharacterStatsCalculator.calculateBaseStats(
            str, intelligence, agi, createDTO.getProfession()
        );
        
        WgCharacter character = new WgCharacter();
        character.setUserId(userId);
        character.setCharacterName(createDTO.getCharacterName());
        character.setProfession(createDTO.getProfession());
        character.setLevel(1);
        character.setExperience(0L);
        
        // 设置基础属性
        character.setStrength(str);
        character.setIntelligence(intelligence);
        character.setAgility(agi);
        
        // 设置衍生属性
        character.setHp(derived.getMaxHp());
        character.setMp(derived.getMaxMp());
        character.setPhysicalAttack(derived.getPhysicalAttack());
        character.setMagicAttack(derived.getMagicAttack());
        character.setDefense(derived.getDefense());
        character.setDodgeRate(derived.getDodgeRate());
        character.setCriticalRate(derived.getCriticalRate());
        
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
    public CharacterVO updateAttributes(AttributePointDTO pointDTO) {
        // 1. 查询角色（从pointDTO中获取characterId）
        String characterId = pointDTO.getCharacterId();
        if (characterId == null || characterId.isEmpty()) {
            throw new JeecgBootException("角色ID不能为空");
        }
        
        WgCharacter character = this.getById(characterId);
        if (character == null || character.getDelFlag() == 1) {
            throw new JeecgBootException("角色不存在");
        }

        // 2. 计算总加点数
        int totalPoints = (pointDTO.getStr() == null ? 0 : pointDTO.getStr()) +
                         (pointDTO.getIntelligence() == null ? 0 : pointDTO.getIntelligence()) +
                         (pointDTO.getAgi() == null ? 0 : pointDTO.getAgi());

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
        if (pointDTO.getStr() != null && pointDTO.getStr() > 0) {
            character.setStrength(character.getStrength() + pointDTO.getStr());
        }
        if (pointDTO.getIntelligence() != null && pointDTO.getIntelligence() > 0) {
            character.setIntelligence(character.getIntelligence() + pointDTO.getIntelligence());
        }
        if (pointDTO.getAgi() != null && pointDTO.getAgi() > 0) {
            character.setAgility(character.getAgility() + pointDTO.getAgi());
        }

        // 6. 重新计算衍生属性
        recalculateDerivedStats(character);

        // 7. 保存更新
        character.setUpdateTime(new Date());
        this.updateById(character);

        log.info("属性加点成功: characterId={}, str+={}, intelligence+={}, agi+={}",
                characterId, 
                pointDTO.getStr(),
                pointDTO.getIntelligence(),
                pointDTO.getAgi());

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
        vo.setUserId(character.getUserId());
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
        
        // 战宠加成（后续由战宠模块更新）
        vo.setBonusHp(character.getBonusHp() != null ? character.getBonusHp() : 0);
        vo.setBonusPhysicalAttack(character.getBonusPhysicalAttack() != null ? character.getBonusPhysicalAttack() : 0);
        vo.setBonusMagicAttack(character.getBonusMagicAttack() != null ? character.getBonusMagicAttack() : 0);
        vo.setBonusDefense(character.getBonusDefense() != null ? character.getBonusDefense() : 0);
        
        // 时间字段
        vo.setCreateTime(character.getCreateTime());
        vo.setUpdateTime(character.getUpdateTime());
        
        // 头像字段
        vo.setAvatarUrl(character.getAvatarUrl());
        vo.setPortraitUrl(character.getPortraitUrl());
        
        // 装备信息（查询已穿戴的装备）
        vo.setEquipment(buildEquipmentInfo(character.getId()));
        
        return vo;
    }

    /**
     * 构建装备信息
     */
    private CharacterVO.EquipmentInfo buildEquipmentInfo(String characterId) {
        CharacterVO.EquipmentInfo equipmentInfo = new CharacterVO.EquipmentInfo();
        
        // 查询角色的所有装备
        List<CharacterEquipment> equipments = characterEquipmentMapper.selectByCharacterId(characterId);
        
        // 将装备按槽位分类
        for (CharacterEquipment equip : equipments) {
            EquipmentVO equipVO = buildEquipmentVO(equip);
            if (equipVO != null) {
                switch (equip.getSlotType()) {
                    case "weapon":
                        equipmentInfo.setWeapon(equipVO);
                        break;
                    case "helmet":
                        equipmentInfo.setHelmet(equipVO);
                        break;
                    case "chest":
                        equipmentInfo.setChest(equipVO);
                        break;
                    case "legs":
                        equipmentInfo.setLegs(equipVO);
                        break;
                    case "accessory1":
                        equipmentInfo.setAccessory1(equipVO);
                        break;
                    case "accessory2":
                        equipmentInfo.setAccessory2(equipVO);
                        break;
                    default:
                        log.warn("未知的装备槽位类型: {}", equip.getSlotType());
                        break;
                }
            }
        }
        
        return equipmentInfo;
    }

    /**
     * 构建单个装备VO
     */
    private EquipmentVO buildEquipmentVO(CharacterEquipment equipment) {
        // 查询物品模板
        WgItemTemplate template = wgItemTemplateMapper.selectById(equipment.getItemId());
        if (template == null) {
            return null;
        }

        EquipmentVO vo = new EquipmentVO();
        vo.setId(equipment.getId());
        vo.setName(template.getName());
        vo.setRarity(template.getRarity());
        vo.setSlotType(equipment.getSlotType());
        vo.setSetId(template.getSetId());
        vo.setSetName(template.getSetName());
        vo.setIconUrl(template.getIcon());
        vo.setDescription(template.getDescription());
        vo.setLevelRequirement(template.getLevelRequirement());

        // 解析基础属性JSON
        if (template.getBaseStats() != null) {
            try {
                Map<String, Object> stats = JSON.parseObject(template.getBaseStats(), Map.class);
                vo.setStats(stats);
            } catch (Exception e) {
                log.warn("解析装备属性JSON失败: itemId={}", template.getItemId());
                vo.setStats(new HashMap<>());
            }
        } else {
            vo.setStats(new HashMap<>());
        }

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
     * 公式: 可用点 = (等级 - 1) × 每级点数 - 已分配点数
     */
    private int calculateAvailablePoints(WgCharacter character) {
        // 总获得点数 = (等级 - 1) × 每级点数
        int totalPoints = (character.getLevel() - 1) * POINTS_PER_LEVEL;
        
        // 已分配点数 = (当前力量 + 智力 + 敏捷) - 职业基础属性
        int[] baseAttrs = PROFESSION_BASE_ATTRIBUTES[character.getProfession()];
        int allocatedPoints = (character.getStrength() - baseAttrs[0]) +
                             (character.getIntelligence() - baseAttrs[1]) +
                             (character.getAgility() - baseAttrs[2]);
        
        return Math.max(0, totalPoints - allocatedPoints);
    }

    /**
     * 重新计算衍生属性
     * 根据标准公式：基础属性 + 职业加成
     */
    private void recalculateDerivedStats(WgCharacter character) {
        CharacterStatsCalculator.DerivedStats derived = CharacterStatsCalculator.calculateBaseStats(
            character.getStrength(),
            character.getIntelligence(),
            character.getAgility(),
            character.getProfession()
        );
        
        // 更新衍生属性
        character.setHp(derived.getMaxHp());
        character.setMp(derived.getMaxMp());
        character.setPhysicalAttack(derived.getPhysicalAttack());
        character.setMagicAttack(derived.getMagicAttack());
        character.setDefense(derived.getDefense());
        character.setDodgeRate(derived.getDodgeRate());
        character.setCriticalRate(derived.getCriticalRate());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddExperienceResultVO addExperience(AddExperienceDTO dto) {
        // 1. 校验参数
        if (dto.getExpToAdd() < 0) {
            throw new JeecgBootException("经验值不能为负数");
        }

        // 2. 查询角色
        WgCharacter character = this.getById(dto.getCharacterId());
        if (character == null || character.getDelFlag() == 1) {
            throw new JeecgBootException("角色不存在");
        }

        // 3. 满级处理
        if (character.getLevel() >= ExperienceCalculator.MAX_LEVEL) {
            log.info("角色已满级，不增加经验: characterId={}", dto.getCharacterId());
            AddExperienceResultVO result = new AddExperienceResultVO();
            result.setCharacter(convertToVO(character));
            result.setLevelUp(null);
            return result;
        }

        // 4. 计算升级结果
        int currentLevel = character.getLevel();
        int currentExp = character.getExperience() != null ? character.getExperience().intValue() : 0;
        
        org.jeecg.modules.webgame.character.vo.LevelUpResultVO levelUpResult = 
            ExperienceCalculator.calculateLevelUp(currentLevel, currentExp, dto.getExpToAdd());

        // 5. 更新角色数据
        character.setLevel(levelUpResult.getNewLevel());
        character.setExperience((long) levelUpResult.getOverflowExp());
        
        // 累加可用属性点
        int currentAvailablePoints = calculateAvailablePoints(character);
        // 注意：这里需要重新计算，因为等级可能变化了
        int newAvailablePoints = calculateAvailablePointsAfterLevelUp(
            character, 
            levelUpResult.getLevelsGained()
        );
        
        // 6. 如果有升级，重新计算衍生属性
        if (levelUpResult.getLevelsGained() > 0) {
            recalculateDerivedStats(character);
        }

        // 7. 保存数据库
        character.setUpdateTime(new Date());
        this.updateById(character);

        log.info("增加经验成功: characterId={}, exp+={}, levelUp={}",
                dto.getCharacterId(),
                dto.getExpToAdd(),
                levelUpResult.getLevelsGained() > 0 ? 
                    String.format("%d->%d", levelUpResult.getOldLevel(), levelUpResult.getNewLevel()) : "无"
        );

        // 8. 返回结果
        AddExperienceResultVO result = new AddExperienceResultVO();
        result.setCharacter(convertToVO(character));
        result.setLevelUp(levelUpResult.getLevelsGained() > 0 ? levelUpResult : null);
        
        return result;
    }

    /**
     * 计算升级后的可用属性点
     */
    private int calculateAvailablePointsAfterLevelUp(WgCharacter character, int levelsGained) {
        // 总获得点数 = (等级 - 1) × 每级点数
        int totalPoints = (character.getLevel() - 1) * POINTS_PER_LEVEL;
        
        // 已分配点数 = (当前力量 + 智力 + 敏捷) - 职业基础属性
        int[] baseAttrs = PROFESSION_BASE_ATTRIBUTES[character.getProfession()];
        int allocatedPoints = (character.getStrength() - baseAttrs[0]) +
                             (character.getIntelligence() - baseAttrs[1]) +
                             (character.getAgility() - baseAttrs[2]);
        
        return Math.max(0, totalPoints - allocatedPoints);
    }
}
