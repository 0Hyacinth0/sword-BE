package org.jeecg.modules.webgame.pet.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.webgame.character.entity.WgCharacter;
import org.jeecg.modules.webgame.character.mapper.WgCharacterMapper;
import org.jeecg.modules.webgame.item.entity.CharacterInventory;
import org.jeecg.modules.webgame.item.entity.WgItemTemplate;
import org.jeecg.modules.webgame.item.mapper.CharacterInventoryMapper;
import org.jeecg.modules.webgame.item.mapper.WgItemTemplateMapper;
import org.jeecg.modules.webgame.pet.dto.*;
import org.jeecg.modules.webgame.pet.entity.*;
import org.jeecg.modules.webgame.pet.mapper.*;
import org.jeecg.modules.webgame.pet.service.IPetService;
import org.jeecg.modules.webgame.pet.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 战宠服务实现类
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Slf4j
@Service
public class PetServiceImpl implements IPetService {

    @Autowired
    private CharacterPetMapper characterPetMapper;

    @Autowired
    private PetTypeMapper petTypeMapper;

    @Autowired
    private PetSkillMapper petSkillMapper;

    @Autowired
    private PetEvolveItemMapper petEvolveItemMapper;

    @Autowired
    private WgCharacterMapper characterMapper;

    @Autowired
    private CharacterInventoryMapper characterItemMapper;

    @Autowired
    private WgItemTemplateMapper itemTemplateMapper;

    private static final int MAX_PET_CAPACITY = 3;

    @Override
    public PetListVO getPetList(String characterId, String userId) {
        // 校验角色存在且属于该用户
        validateCharacterOwnership(characterId, userId);

        // 查询角色的所有战宠
        List<CharacterPet> pets = characterPetMapper.selectByCharacterId(characterId);

        // 转换为VO
        List<PetInfoVO> petInfoList = pets.stream()
                .map(this::convertToPetInfoVO)
                .collect(Collectors.toList());

        // 构建响应
        PetListVO result = new PetListVO();
        result.setPets(petInfoList);

        PetListVO.PetCapacityVO capacity = new PetListVO.PetCapacityVO();
        capacity.setMax(MAX_PET_CAPACITY);
        capacity.setCurrent(pets.size());
        result.setCapacity(capacity);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PetListVO setActivePet(SetActivePetDTO dto) {
        // 校验角色存在且属于该用户
        validateCharacterOwnership(dto.getCharacterId(), dto.getUserId());

        // 校验战宠存在且属于该角色
        CharacterPet pet = validatePetOwnership(dto.getPetId(), dto.getCharacterId());

        // 取消所有战宠的出战状态
        characterPetMapper.clearActiveStatus(dto.getCharacterId());

        // 设置目标战宠为出战状态
        characterPetMapper.setActiveStatus(dto.getPetId());

        // 返回更新后的战宠列表
        return getPetList(dto.getCharacterId(), dto.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PetListVO feedPet(FeedPetDTO dto) {
        // 校验角色存在且属于该用户
        validateCharacterOwnership(dto.getCharacterId(), dto.getUserId());

        // 校验战宠存在且属于该角色
        CharacterPet pet = validatePetOwnership(dto.getPetId(), dto.getCharacterId());

        // 校验背包物品存在且属于该角色
        CharacterInventory inventoryItem = characterItemMapper.selectById(dto.getInventoryId());
        if (inventoryItem == null || !dto.getCharacterId().equals(inventoryItem.getCharacterId())) {
            throw new RuntimeException("背包物品不存在或不属于该角色");
        }

        // 校验物品数量足够
        if (inventoryItem.getQuantity() < dto.getQuantity()) {
            throw new RuntimeException("物品数量不足");
        }

        // 校验物品是消耗品或材料（type=2或3）
        WgItemTemplate itemTemplate = itemTemplateMapper.selectById(inventoryItem.getItemId());
        if (itemTemplate == null) {
            throw new RuntimeException("物品模板不存在");
        }

        // 校验物品类型：2-消耗品，3-材料
        if (itemTemplate.getType() != 2 && itemTemplate.getType() != 3) {
            throw new RuntimeException("该物品不是消耗品或材料");
        }

        // 从物品名称或描述中解析经验值（简化处理，假设所有消耗品都加1000经验）
        // TODO: 实际应该从物品配置的base_stats或其他字段读取
        int expPerItem = 1000; // 默认每个物品加1000经验
        int totalExp = expPerItem * dto.getQuantity();

        // 扣减背包物品数量
        int remainingQuantity = inventoryItem.getQuantity() - dto.getQuantity();
        if (remainingQuantity <= 0) {
            characterItemMapper.deleteById(dto.getInventoryId());
        } else {
            inventoryItem.setQuantity(remainingQuantity);
            characterItemMapper.updateById(inventoryItem);
        }

        // 增加战宠经验值并处理升级
        addPetExp(pet, totalExp);

        // 返回更新后的战宠列表
        return getPetList(dto.getCharacterId(), dto.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PetListVO evolvePet(EvolvePetDTO dto) {
        // 校验角色存在且属于该用户
        validateCharacterOwnership(dto.getCharacterId(), dto.getUserId());

        // 校验战宠存在且属于该角色
        CharacterPet pet = validatePetOwnership(dto.getPetId(), dto.getCharacterId());

        // 查询战宠类型配置
        PetType currentType = petTypeMapper.selectByPetTypeId(pet.getPetTypeId());
        if (currentType == null) {
            throw new RuntimeException("战宠类型配置不存在");
        }

        // 校验是否可以进化
        if (currentType.getEvolveTo() == null || currentType.getEvolveLevel() == null) {
            throw new RuntimeException("该战宠无法进化");
        }

        // 校验等级是否足够
        if (pet.getLevel() < currentType.getEvolveLevel()) {
            throw new RuntimeException("等级不足，需要" + currentType.getEvolveLevel() + "级");
        }

        // 查询进化材料配置
        List<PetEvolveItem> evolveItems = petEvolveItemMapper.selectByPetTypeId(pet.getPetTypeId());
        if (evolveItems == null || evolveItems.isEmpty()) {
            throw new RuntimeException("进化材料配置不存在");
        }

        // 校验背包拥有足够的进化材料并扣减
        for (PetEvolveItem evolveItem : evolveItems) {
            CharacterInventory inventoryItem = characterItemMapper.selectOne(
                    new LambdaQueryWrapper<CharacterInventory>()
                            .eq(CharacterInventory::getCharacterId, dto.getCharacterId())
                            .eq(CharacterInventory::getItemId, evolveItem.getItemId())
            );

            if (inventoryItem == null || inventoryItem.getQuantity() < evolveItem.getQuantity()) {
                throw new RuntimeException("进化材料不足：" + getItemName(evolveItem.getItemId()));
            }

            // 扣减材料
            int remainingQuantity = inventoryItem.getQuantity() - evolveItem.getQuantity();
            if (remainingQuantity <= 0) {
                characterItemMapper.deleteById(inventoryItem.getId());
            } else {
                inventoryItem.setQuantity(remainingQuantity);
                characterItemMapper.updateById(inventoryItem);
            }
        }

        // 更新战宠类型为进化目标
        pet.setPetTypeId(currentType.getEvolveTo());

        // 品质+1（最高SSR=4）
        if (pet.getRarity() < 4) {
            pet.setRarity(pet.getRarity() + 1);
        }

        // 保留80%当前经验进度
        pet.setExp((int) Math.floor(pet.getExp() * 0.8));

        // 更新战宠
        characterPetMapper.updateById(pet);

        // 返回更新后的战宠列表
        return getPetList(dto.getCharacterId(), dto.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PetListVO renamePet(RenamePetDTO dto) {
        // 校验角色存在且属于该用户
        validateCharacterOwnership(dto.getCharacterId(), dto.getUserId());

        // 校验战宠存在且属于该角色
        CharacterPet pet = validatePetOwnership(dto.getPetId(), dto.getCharacterId());

        // 校验昵称长度
        if (dto.getNickname() == null || dto.getNickname().length() < 1 || dto.getNickname().length() > 12) {
            throw new RuntimeException("昵称长度为1-12个字符");
        }

        // 更新昵称
        pet.setNickname(dto.getNickname());
        characterPetMapper.updateById(pet);

        // 返回更新后的战宠列表
        return getPetList(dto.getCharacterId(), dto.getUserId());
    }

    @Override
    public PetInfoVO getPetDetail(String petId, String userId) {
        // 查询战宠
        CharacterPet pet = characterPetMapper.selectByPetId(petId);
        if (pet == null) {
            throw new RuntimeException("战宠不存在");
        }

        // 校验角色所有权
        validateCharacterOwnership(pet.getCharacterId(), userId);

        // 转换为VO
        return convertToPetInfoVO(pet);
    }

    @Override
    public PetTypeListVO getAllPetTypes() {
        // 查询所有战宠类型
        List<PetType> petTypes = petTypeMapper.selectAllPetTypes();

        // 转换为VO
        List<PetTypeConfigVO> typeConfigs = petTypes.stream()
                .map(this::convertToPetTypeConfigVO)
                .collect(Collectors.toList());

        // 构建响应
        PetTypeListVO result = new PetTypeListVO();
        result.setTotal(typeConfigs.size());
        result.setTypes(typeConfigs);

        return result;
    }

    @Override
    public OwnedPetTypesVO getOwnedPetTypes(String characterId, String userId) {
        // 校验角色存在且属于该用户
        validateCharacterOwnership(characterId, userId);

        // 查询角色的所有战宠
        List<CharacterPet> pets = characterPetMapper.selectByCharacterId(characterId);

        // 提取唯一的战宠类型ID
        List<Integer> ownedTypeIds = pets.stream()
                .map(CharacterPet::getPetTypeId)
                .distinct()
                .collect(Collectors.toList());

        // 构建响应
        OwnedPetTypesVO result = new OwnedPetTypesVO();
        result.setOwnedTypeIds(ownedTypeIds);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PetListVO equipPetItem(EquipPetItemDTO dto) {
        // 校验角色存在且属于该用户
        validateCharacterOwnership(dto.getCharacterId(), dto.getUserId());

        // 校验战宠存在且属于该角色
        CharacterPet pet = validatePetOwnership(dto.getPetId(), dto.getCharacterId());

        // 校验槽位类型
        if (!"armor".equals(dto.getSlotType()) && !"accessory".equals(dto.getSlotType())) {
            throw new RuntimeException("无效的槽位类型");
        }

        // 校验背包物品存在且属于该角色
        CharacterInventory inventoryItem = characterItemMapper.selectById(dto.getInventoryId());
        if (inventoryItem == null || !dto.getCharacterId().equals(inventoryItem.getCharacterId())) {
            throw new RuntimeException("背包物品不存在或不属于该角色");
        }

        // 校验物品类别为装备（type=1）
        WgItemTemplate itemTemplate = itemTemplateMapper.selectById(inventoryItem.getItemId());
        if (itemTemplate == null || itemTemplate.getType() != 1) {
            throw new RuntimeException("该物品不是装备");
        }

        // 如果槽位已有装备，先将旧装备回到背包
        String oldEquipId = "armor".equals(dto.getSlotType()) ? pet.getEquipArmor() : pet.getEquipAccessory();
        if (oldEquipId != null) {
            returnOldEquipmentToInventory(dto.getCharacterId(), oldEquipId);
        }

        // 从背包移除该装备记录
        characterItemMapper.deleteById(dto.getInventoryId());

        // 更新战宠装备槽位
        if ("armor".equals(dto.getSlotType())) {
            pet.setEquipArmor(dto.getInventoryId());
        } else {
            pet.setEquipAccessory(dto.getInventoryId());
        }
        characterPetMapper.updateById(pet);

        // 返回更新后的战宠列表
        return getPetList(dto.getCharacterId(), dto.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PetListVO unequipPetItem(UnequipPetItemDTO dto) {
        // 校验角色存在且属于该用户
        validateCharacterOwnership(dto.getCharacterId(), dto.getUserId());

        // 校验战宠存在且属于该角色
        CharacterPet pet = validatePetOwnership(dto.getPetId(), dto.getCharacterId());

        // 校验槽位类型
        if (!"armor".equals(dto.getSlotType()) && !"accessory".equals(dto.getSlotType())) {
            throw new RuntimeException("无效的槽位类型");
        }

        // 校验该槽位有装备
        String equipId = "armor".equals(dto.getSlotType()) ? pet.getEquipArmor() : pet.getEquipAccessory();
        if (equipId == null) {
            throw new RuntimeException("该槽位没有装备");
        }

        // 将装备添加回角色背包
        returnOldEquipmentToInventory(dto.getCharacterId(), equipId);

        // 更新战宠装备槽位为null
        if ("armor".equals(dto.getSlotType())) {
            pet.setEquipArmor(null);
        } else {
            pet.setEquipAccessory(null);
        }
        characterPetMapper.updateById(pet);

        // 返回更新后的战宠列表
        return getPetList(dto.getCharacterId(), dto.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PetListVO equipPetSkill(EquipPetSkillDTO dto) {
        // 校验角色存在且属于该用户
        validateCharacterOwnership(dto.getCharacterId(), dto.getUserId());

        // 校验战宠存在且属于该角色
        CharacterPet pet = validatePetOwnership(dto.getPetId(), dto.getCharacterId());

        // 校验槽位索引
        if (dto.getSlotIndex() < 0 || dto.getSlotIndex() > 2) {
            throw new RuntimeException("槽位索引范围为0-2");
        }

        // 查询技能配置
        PetSkill skill = petSkillMapper.selectBySkillId(dto.getSkillId());
        if (skill == null) {
            throw new RuntimeException("技能不存在");
        }

        // 校验战宠已学会此技能
        if (!isPetLearnedSkill(pet, skill)) {
            throw new RuntimeException("战宠未学会此技能");
        }

        // 校验该技能未装备在其他槽位
        if (isSkillEquippedInOtherSlot(pet, dto.getSkillId(), dto.getSlotIndex())) {
            throw new RuntimeException("该技能已装备在其他槽位");
        }

        // 更新技能槽位
        switch (dto.getSlotIndex()) {
            case 0:
                pet.setSkillSlot1(dto.getSkillId());
                break;
            case 1:
                pet.setSkillSlot2(dto.getSkillId());
                break;
            case 2:
                pet.setSkillSlot3(dto.getSkillId());
                break;
        }
        characterPetMapper.updateById(pet);

        // 返回更新后的战宠列表
        return getPetList(dto.getCharacterId(), dto.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PetListVO unequipPetSkill(UnequipPetSkillDTO dto) {
        // 校验角色存在且属于该用户
        validateCharacterOwnership(dto.getCharacterId(), dto.getUserId());

        // 校验战宠存在且属于该角色
        CharacterPet pet = validatePetOwnership(dto.getPetId(), dto.getCharacterId());

        // 校验槽位索引
        if (dto.getSlotIndex() < 0 || dto.getSlotIndex() > 2) {
            throw new RuntimeException("槽位索引范围为0-2");
        }

        // 校验该槽位有技能
        Integer currentSkillId = getSkillFromSlot(pet, dto.getSlotIndex());
        if (currentSkillId == null) {
            throw new RuntimeException("该槽位没有技能");
        }

        // 卸下技能
        switch (dto.getSlotIndex()) {
            case 0:
                pet.setSkillSlot1(null);
                break;
            case 1:
                pet.setSkillSlot2(null);
                break;
            case 2:
                pet.setSkillSlot3(null);
                break;
        }
        characterPetMapper.updateById(pet);

        // 返回更新后的战宠列表
        return getPetList(dto.getCharacterId(), dto.getUserId());
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 校验角色存在且属于该用户
     */
    private void validateCharacterOwnership(String characterId, String userId) {
        WgCharacter character = characterMapper.selectById(characterId);
        if (character == null) {
            throw new RuntimeException("角色不存在");
        }
        if (!userId.equals(character.getUserId())) {
            throw new RuntimeException("角色不属于该用户");
        }
    }

    /**
     * 校验战宠存在且属于该角色
     */
    private CharacterPet validatePetOwnership(String petId, String characterId) {
        CharacterPet pet = characterPetMapper.selectByPetId(petId);
        if (pet == null) {
            throw new RuntimeException("战宠不存在");
        }
        if (!characterId.equals(pet.getCharacterId())) {
            throw new RuntimeException("战宠不属于该角色");
        }
        return pet;
    }

    /**
     * 转换战宠实体为VO
     */
    private PetInfoVO convertToPetInfoVO(CharacterPet pet) {
        PetInfoVO vo = new PetInfoVO();
        vo.setId(pet.getId());
        vo.setPetTypeId(pet.getPetTypeId());
        vo.setNickname(pet.getNickname());
        vo.setLevel(pet.getLevel());
        vo.setExp(pet.getExp());
        vo.setMaxExp(pet.getMaxExp());
        vo.setRarity(pet.getRarity());
        vo.setIsActive(pet.getIsActive() == 1);

        // 查询战宠类型配置
        PetType petType = petTypeMapper.selectByPetTypeId(pet.getPetTypeId());
        if (petType != null) {
            vo.setName(petType.getName());
            vo.setElement(petType.getElement());
            vo.setEvolveTo(petType.getEvolveTo());
            vo.setEvolveLevel(petType.getEvolveLevel());
            vo.setDescription(petType.getDescription());
        }

        // 计算属性
        PetStatsVO stats = calculatePetStats(pet, petType);
        vo.setStats(stats);

        // 计算主人加成
        PetOwnerBonusVO bonus = calculateOwnerBonus(stats, pet.getRarity());
        vo.setBonusToOwner(bonus);

        // 获取已装备的技能
        List<PetSkillVO> equippedSkills = getEquippedSkills(pet);
        vo.setSkills(equippedSkills);

        // 获取已学会的技能
        List<PetSkillVO> learnedSkills = getLearnedSkills(pet, petType);
        vo.setLearnedSkills(learnedSkills);

        // 获取装备信息
        PetEquipmentVO equipment = getPetEquipment(pet);
        vo.setEquipment(equipment);

        return vo;
    }

    /**
     * 计算战宠属性
     */
    private PetStatsVO calculatePetStats(CharacterPet pet, PetType petType) {
        PetStatsVO stats = new PetStatsVO();

        if (petType != null) {
            // 品质倍率
            double rarityMultiplier = getRarityMultiplier(pet.getRarity());

            // 基础属性 + 等级成长 × 品质倍率
            int baseHp = (petType.getBaseHp() + pet.getLevel() * petType.getHpGrowth());
            int baseAttack = (petType.getBaseAttack() + pet.getLevel() * petType.getAttackGrowth());
            int baseDefense = (petType.getBaseDefense() + pet.getLevel() * petType.getDefenseGrowth());
            int baseSpeed = (petType.getBaseSpeed() + pet.getLevel() * petType.getSpeedGrowth());

            stats.setMaxHp((int) Math.floor(baseHp * rarityMultiplier));
            stats.setAttack((int) Math.floor(baseAttack * rarityMultiplier));
            stats.setDefense((int) Math.floor(baseDefense * rarityMultiplier));
            stats.setSpeed((int) Math.floor(baseSpeed * rarityMultiplier));
        }

        // TODO: 加上装备属性加成
        // 需要从 wg_character_items 表查询装备详情并累加属性

        stats.setHp(stats.getMaxHp());

        return stats;
    }

    /**
     * 计算主人加成
     */
    private PetOwnerBonusVO calculateOwnerBonus(PetStatsVO petStats, Integer rarity) {
        PetOwnerBonusVO bonus = new PetOwnerBonusVO();

        double multiplier = getRarityMultiplier(rarity);

        bonus.setHp((int) Math.floor(petStats.getHp() * 0.1 * multiplier));
        bonus.setAttack((int) Math.floor(petStats.getAttack() * 0.1 * multiplier));
        bonus.setDefense((int) Math.floor(petStats.getDefense() * 0.1 * multiplier));

        return bonus;
    }

    /**
     * 获取品质倍率
     */
    private double getRarityMultiplier(Integer rarity) {
        if (rarity == null) return 1.0;
        switch (rarity) {
            case 1: return 1.0;   // N
            case 2: return 1.2;   // R
            case 3: return 1.5;   // SR
            case 4: return 2.0;   // SSR
            default: return 1.0;
        }
    }

    /**
     * 获取已装备的技能
     */
    private List<PetSkillVO> getEquippedSkills(CharacterPet pet) {
        List<PetSkillVO> skills = new ArrayList<>();

        Integer[] slotSkills = {pet.getSkillSlot1(), pet.getSkillSlot2(), pet.getSkillSlot3()};
        for (Integer skillId : slotSkills) {
            if (skillId != null) {
                PetSkill skill = petSkillMapper.selectBySkillId(skillId);
                if (skill != null) {
                    skills.add(convertToPetSkillVO(skill));
                }
            }
        }

        return skills;
    }

    /**
     * 获取已学会的技能
     */
    private List<PetSkillVO> getLearnedSkills(CharacterPet pet, PetType petType) {
        if (petType == null) {
            return new ArrayList<>();
        }

        // 查询所有技能
        List<PetSkill> allSkills = petSkillMapper.selectAllPetSkills();

        // 过滤出该战宠类型可学习且等级达标的技能
        return allSkills.stream()
                .filter(skill -> isSkillAvailableForPet(skill, pet, petType))
                .map(this::convertToPetSkillVO)
                .collect(Collectors.toList());
    }

    /**
     * 判断技能是否对战宠可用
     */
    private boolean isSkillAvailableForPet(PetSkill skill, CharacterPet pet, PetType petType) {
        // 检查技能的pet_type_ids是否包含当前战宠类型
        if (skill.getPetTypeIds() != null) {
            try {
                JSONArray typeIds = JSON.parseArray(skill.getPetTypeIds());
                boolean typeMatch = typeIds.stream()
                        .anyMatch(id -> ((Number) id).intValue() == petType.getPetTypeId());
                if (!typeMatch) {
                    return false;
                }
            } catch (Exception e) {
                log.error("解析技能pet_type_ids失败", e);
                return false;
            }
        }

        // 检查等级条件
        if (skill.getLearnLevel() == 0) {
            // 进化专属技能，仅进化后可学（这里简化处理，认为只要等级>=1即可）
            return pet.getLevel() >= 1;
        } else {
            // 普通技能，需要等级达标
            return pet.getLevel() >= skill.getLearnLevel();
        }
    }

    /**
     * 判断战宠是否已学会某技能
     */
    private boolean isPetLearnedSkill(CharacterPet pet, PetSkill skill) {
        PetType petType = petTypeMapper.selectByPetTypeId(pet.getPetTypeId());
        return isSkillAvailableForPet(skill, pet, petType);
    }

    /**
     * 判断技能是否已装备在其他槽位
     */
    private boolean isSkillEquippedInOtherSlot(CharacterPet pet, Integer skillId, int excludeSlotIndex) {
        Map<Integer, Integer> slotMap = new HashMap<>();
        slotMap.put(0, pet.getSkillSlot1());
        slotMap.put(1, pet.getSkillSlot2());
        slotMap.put(2, pet.getSkillSlot3());

        for (Map.Entry<Integer, Integer> entry : slotMap.entrySet()) {
            if (!entry.getKey().equals(excludeSlotIndex) && skillId.equals(entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从槽位获取技能ID
     */
    private Integer getSkillFromSlot(CharacterPet pet, int slotIndex) {
        switch (slotIndex) {
            case 0: return pet.getSkillSlot1();
            case 1: return pet.getSkillSlot2();
            case 2: return pet.getSkillSlot3();
            default: return null;
        }
    }

    /**
     * 获取战宠装备信息
     */
    private PetEquipmentVO getPetEquipment(CharacterPet pet) {
        PetEquipmentVO equipment = new PetEquipmentVO();

        // TODO: 从 wg_character_items 表查询装备详情
        // 目前只返回空对象，实际需要查询装备信息

        return equipment;
    }

    /**
     * 转换战宠类型实体为VO
     */
    private PetTypeConfigVO convertToPetTypeConfigVO(PetType petType) {
        PetTypeConfigVO vo = new PetTypeConfigVO();
        vo.setPetTypeId(petType.getPetTypeId());
        vo.setName(petType.getName());
        vo.setElement(petType.getElement());
        vo.setRarity(petType.getRarity());
        vo.setBaseHp(petType.getBaseHp());
        vo.setBaseAttack(petType.getBaseAttack());
        vo.setBaseDefense(petType.getBaseDefense());
        vo.setBaseSpeed(petType.getBaseSpeed());
        vo.setDescription(petType.getDescription());
        vo.setEvolveTo(petType.getEvolveTo());
        vo.setEvolveLevel(petType.getEvolveLevel());

        // 获取该类型的技能列表
        List<PetSkill> allSkills = petSkillMapper.selectAllPetSkills();
        List<PetSkillVO> skills = allSkills.stream()
                .filter(skill -> {
                    if (skill.getPetTypeIds() != null) {
                        try {
                            JSONArray typeIds = JSON.parseArray(skill.getPetTypeIds());
                            return typeIds.stream()
                                    .anyMatch(id -> ((Number) id).intValue() == petType.getPetTypeId());
                        } catch (Exception e) {
                            return false;
                        }
                    }
                    return false;
                })
                .map(this::convertToPetSkillVO)
                .collect(Collectors.toList());
        vo.setSkills(skills);

        return vo;
    }

    /**
     * 转换技能实体为VO
     */
    private PetSkillVO convertToPetSkillVO(PetSkill skill) {
        PetSkillVO vo = new PetSkillVO();
        vo.setId(skill.getSkillId());
        vo.setName(skill.getName());
        vo.setType(skill.getType());
        vo.setPower(skill.getPower());
        vo.setCooldown(skill.getCooldown());
        vo.setLearnLevel(skill.getLearnLevel());
        vo.setDescription(skill.getDescription());
        return vo;
    }

    /**
     * 增加战宠经验值并处理升级
     */
    private void addPetExp(CharacterPet pet, int expToAdd) {
        pet.setExp(pet.getExp() + expToAdd);

        // 处理升级
        while (pet.getExp() >= pet.getMaxExp()) {
            pet.setExp(pet.getExp() - pet.getMaxExp());
            pet.setLevel(pet.getLevel() + 1);
            // 每级所需经验增加10%
            pet.setMaxExp((int) Math.floor(pet.getMaxExp() * 1.1));
        }

        characterPetMapper.updateById(pet);
    }

    /**
     * 将旧装备返回到背包
     */
    private void returnOldEquipmentToInventory(String characterId, String equipmentId) {
        // 查询装备实例
        CharacterInventory equipment = characterItemMapper.selectById(equipmentId);
        if (equipment != null) {
            // 重新添加到背包
            equipment.setId(null); // 生成新ID
            characterItemMapper.insert(equipment);
        }
    }

    /**
     * 获取物品名称
     */
    private String getItemName(String itemId) {
        WgItemTemplate item = itemTemplateMapper.selectById(itemId);
        return item != null ? item.getName() : "未知物品";
    }
}
