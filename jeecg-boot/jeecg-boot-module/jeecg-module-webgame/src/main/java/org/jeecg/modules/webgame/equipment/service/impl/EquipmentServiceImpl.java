package org.jeecg.modules.webgame.equipment.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.webgame.auth.entity.WgUser;
import org.jeecg.modules.webgame.auth.mapper.WgUserMapper;
import org.jeecg.modules.webgame.character.entity.WgCharacter;
import org.jeecg.modules.webgame.character.mapper.WgCharacterMapper;
import org.jeecg.modules.webgame.character.vo.EquipmentVO;
import org.jeecg.modules.webgame.equipment.dto.EquipItemDTO;
import org.jeecg.modules.webgame.equipment.dto.EnhanceEquipmentDTO;
import org.jeecg.modules.webgame.equipment.dto.UnequipItemDTO;
import org.jeecg.modules.webgame.equipment.entity.CharacterEquipment;
import org.jeecg.modules.webgame.equipment.mapper.CharacterEquipmentMapper;
import org.jeecg.modules.webgame.equipment.service.IEquipmentService;
import org.jeecg.modules.webgame.equipment.vo.EnhanceResultVO;
import org.jeecg.modules.webgame.item.entity.CharacterInventory;
import org.jeecg.modules.webgame.item.entity.WgItemTemplate;
import org.jeecg.modules.webgame.item.mapper.CharacterInventoryMapper;
import org.jeecg.modules.webgame.item.mapper.WgItemTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Description: 装备系统Service实现
 * @Author: jeecg-boot
 * @Date: 2026-05-09
 */
@Slf4j
@Service
public class EquipmentServiceImpl implements IEquipmentService {

    @Autowired
    private CharacterEquipmentMapper equipmentMapper;

    @Autowired
    private WgItemTemplateMapper itemTemplateMapper;

    @Autowired
    private WgCharacterMapper characterMapper;

    @Autowired
    private CharacterInventoryMapper inventoryMapper;

    @Autowired
    private WgUserMapper userMapper;

    /**
     * 6个装备槽位
     */
    private static final List<String> SLOT_TYPES = Arrays.asList(
            "weapon", "helmet", "chest", "legs", "accessory1", "accessory2"
    );

    @Override
    public Map<String, EquipmentVO> getEquipmentList(String characterId, String userId) {
        // 1. 校验角色是否存在
        WgCharacter character = characterMapper.selectById(characterId);
        if (character == null || character.getDelFlag() == 1) {
            throw new JeecgBootException("角色不存在");
        }

        // 2. 权限验证
        if (!character.getUserId().equals(userId)) {
            throw new JeecgBootException("无权查看该角色的装备");
        }

        // 3. 查询角色所有装备
        List<CharacterEquipment> equipmentList = equipmentMapper.selectByCharacterId(characterId);

        // 4. 构建6槽位结构
        Map<String, EquipmentVO> result = new LinkedHashMap<>();
        for (String slotType : SLOT_TYPES) {
            result.put(slotType, null);
        }

        // 5. 填充装备信息
        for (CharacterEquipment equip : equipmentList) {
            EquipmentVO vo = buildEquipmentVO(equip);
            result.put(equip.getSlotType(), vo);
        }

        log.info("获取装备列表成功: characterId={}, userId={}, equipmentCount={}", 
                characterId, userId, equipmentList.size());

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void equipItem(EquipItemDTO dto) {
        // 1. 查询背包物品
        CharacterInventory inventory = inventoryMapper.selectById(dto.getInventoryId());
        if (inventory == null) {
            throw new JeecgBootException("背包物品不存在");
        }

        // 2. 校验是否属于该角色
        if (!inventory.getCharacterId().equals(dto.getCharacterId())) {
            throw new JeecgBootException("无权操作该角色背包");
        }

        // 3. 权限验证
        WgCharacter character = characterMapper.selectById(dto.getCharacterId());
        if (character == null || character.getDelFlag() == 1) {
            throw new JeecgBootException("角色不存在");
        }
        if (!character.getUserId().equals(dto.getUserId())) {
            throw new JeecgBootException("无权操作该角色的背包");
        }

        // 4. 校验槽位类型是否合法
        if (!SLOT_TYPES.contains(dto.getSlotType())) {
            throw new JeecgBootException("无效的槽位类型");
        }

        // 5. 查询物品模板
        WgItemTemplate itemTemplate = itemTemplateMapper.selectById(inventory.getItemId());
        if (itemTemplate == null) {
            throw new JeecgBootException("物品模板不存在");
        }

        // 6. 校验物品类型是否为装备(type=1)
        if (itemTemplate.getType() == null || itemTemplate.getType() != 1) {
            throw new JeecgBootException("该物品不是装备，无法穿戴");
        }

        // 7. 校验装备槽位是否匹配
        if (itemTemplate.getSlotType() == null || !dto.getSlotType().equals(itemTemplate.getSlotType())) {
            throw new JeecgBootException("装备槽位不匹配");
        }

        // 8. 检查角色等级是否满足装备需求
        if (itemTemplate.getLevelRequirement() != null && character.getLevel() < itemTemplate.getLevelRequirement()) {
            throw new JeecgBootException(String.format("等级不足，需要 %d 级才能穿戴该装备", itemTemplate.getLevelRequirement()));
        }

        // 9. 检查该槽位是否已有装备
        CharacterEquipment existingEquip = equipmentMapper.selectByCharacterAndSlot(
                dto.getCharacterId(), dto.getSlotType());

        if (existingEquip != null) {
            // 已有装备，先卸下旧装备到背包
            // 检查背包中是否已存在该物品
            CharacterInventory existingInventory = inventoryMapper.selectByCharacterAndItem(
                    dto.getCharacterId(), existingEquip.getItemId());
            
            if (existingInventory != null) {
                // 已存在，数量+1
                existingInventory.setQuantity(existingInventory.getQuantity() + 1);
                existingInventory.setUpdateTime(new Date());
                inventoryMapper.updateById(existingInventory);
                
                log.info("替换装备(背包已有该物品): characterId={}, slotType={}, itemId={}, 数量+1", 
                        dto.getCharacterId(), dto.getSlotType(), existingEquip.getItemId());
            } else {
                // 不存在，创建新记录
                CharacterInventory oldInventory = new CharacterInventory();
                oldInventory.setId(UUID.randomUUID().toString());
                oldInventory.setCharacterId(dto.getCharacterId());
                oldInventory.setItemId(existingEquip.getItemId());
                oldInventory.setQuantity(1);
                oldInventory.setObtainedAt(new Date());
                oldInventory.setCreateTime(new Date());
                oldInventory.setUpdateTime(new Date());
                inventoryMapper.insert(oldInventory);
                
                log.info("替换装备(背包新物品): characterId={}, slotType={}, itemId={}", 
                        dto.getCharacterId(), dto.getSlotType(), existingEquip.getItemId());
            }
            
            // 删除旧装备记录
            equipmentMapper.deleteById(existingEquip.getId());
        } else {
            // 槽位为空，需要检查背包容量（32种物品上限）
            int inventoryCount = inventoryMapper.countByCharacterId(dto.getCharacterId());
            if (inventoryCount >= 32) {
                throw new JeecgBootException("背包已满（32/32），无法穿戴新装备");
            }
        }

        // 9. 创建新装备实例
        CharacterEquipment newEquip = new CharacterEquipment();
        newEquip.setId(UUID.randomUUID().toString());
        newEquip.setCharacterId(dto.getCharacterId());
        newEquip.setItemId(inventory.getItemId());
        newEquip.setSlotType(dto.getSlotType());
        newEquip.setEnhanceLevel(0);
        newEquip.setCreateTime(new Date());
        newEquip.setUpdateTime(new Date());
        equipmentMapper.insert(newEquip);

        // 10. 从背包移除物品
        inventoryMapper.deleteById(dto.getInventoryId());

        log.info("穿戴装备成功: characterId={}, userId={}, slotType={}, itemId={}", 
                dto.getCharacterId(), dto.getUserId(), dto.getSlotType(), inventory.getItemId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unequipItem(UnequipItemDTO dto) {
        // 1. 权限验证
        WgCharacter character = characterMapper.selectById(dto.getCharacterId());
        if (character == null || character.getDelFlag() == 1) {
            throw new JeecgBootException("角色不存在");
        }
        if (!character.getUserId().equals(dto.getUserId())) {
            throw new JeecgBootException("无权操作该角色的装备");
        }

        // 2. 查询该槽位的装备
        CharacterEquipment equipment = equipmentMapper.selectByCharacterAndSlot(
                dto.getCharacterId(), dto.getSlotType());

        if (equipment == null) {
            throw new JeecgBootException("该槽位没有装备");
        }

        // 3. 检查背包容量（32种物品上限）
        int inventoryCount = inventoryMapper.countByCharacterId(dto.getCharacterId());
        if (inventoryCount >= 32) {
            throw new JeecgBootException("背包已满（32/32），无法卸下装备");
        }

        // 4. 将装备放回背包
        CharacterInventory inventory = new CharacterInventory();
        inventory.setId(UUID.randomUUID().toString());
        inventory.setCharacterId(dto.getCharacterId());
        inventory.setItemId(equipment.getItemId());
        inventory.setQuantity(1);
        inventory.setObtainedAt(new Date());
        inventory.setCreateTime(new Date());
        inventory.setUpdateTime(new Date());
        inventoryMapper.insert(inventory);

        // 4. 删除装备记录
        equipmentMapper.deleteById(equipment.getId());

        log.info("卸下装备成功: characterId={}, userId={}, slotType={}, itemId={}", 
                dto.getCharacterId(), dto.getUserId(), dto.getSlotType(), equipment.getItemId());
    }

    /**
     * 构建装备VO对象
     * @param equipment 装备实体
     * @return 装备VO
     */
    private EquipmentVO buildEquipmentVO(CharacterEquipment equipment) {
        // 查询物品模板
        WgItemTemplate template = itemTemplateMapper.selectById(equipment.getItemId());
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EnhanceResultVO enhanceEquipment(EnhanceEquipmentDTO dto) {
        // 1. 权限验证
        WgCharacter character = characterMapper.selectById(dto.getCharacterId());
        if (character == null || character.getDelFlag() == 1) {
            throw new JeecgBootException("角色不存在");
        }
        if (!character.getUserId().equals(dto.getUserId())) {
            throw new JeecgBootException("无权操作该角色的装备");
        }

        // 2. 查询该槽位的装备
        CharacterEquipment equipment = equipmentMapper.selectByCharacterAndSlot(
                dto.getCharacterId(), dto.getSlotType());

        if (equipment == null) {
            throw new JeecgBootException("该槽位没有装备");
        }

        // 3. 校验强化等级上限
        if (equipment.getEnhanceLevel() >= 10) {
            throw new JeecgBootException("装备已达到最大强化等级+10");
        }

        // 4. 查询物品模板获取稀有度
        WgItemTemplate template = itemTemplateMapper.selectById(equipment.getItemId());
        if (template == null) {
            throw new JeecgBootException("装备模板不存在");
        }

        // 5. 计算强化成功率和消耗（根据当前等级区间）
        int currentLevel = equipment.getEnhanceLevel();
        double successRate;
        String materialName;
        int materialCount;
        int goldCost;

        if (currentLevel < 3) {
            // +0 → +3: 100% 成功率
            successRate = 1.0;
            materialName = "铁矿石";
            materialCount = 3;
            goldCost = 100;
        } else if (currentLevel < 6) {
            // +3 → +6: 90% 成功率
            successRate = 0.9;
            materialName = "精钢矿石";
            materialCount = 2;
            goldCost = 300;
        } else if (currentLevel < 8) {
            // +6 → +8: 70% 成功率
            successRate = 0.7;
            materialName = "秘法水晶";
            materialCount = 1;
            goldCost = 800;
        } else {
            // +8 → +10: 50% 成功率
            successRate = 0.5;
            materialName = "秘法水晶";
            materialCount = 2;
            goldCost = 2000;
        }

        // TODO: 校验背包材料数量和金币是否充足
        // 目前暂时跳过校验，直接进行强化

        // 6. 判定强化是否成功
        boolean success = ThreadLocalRandom.current().nextDouble() < successRate;

        EnhanceResultVO result = new EnhanceResultVO();
        result.setEquipmentName(template.getName());

        if (success) {
            // 强化成功：等级+1
            equipment.setEnhanceLevel(currentLevel + 1);
            equipment.setUpdateTime(new Date());
            equipmentMapper.updateById(equipment);

            result.setSuccess(true);
            result.setNewLevel(currentLevel + 1);
            result.setMessage(String.format("强化成功！%s 强化至 +%d", template.getName(), currentLevel + 1));

            log.info("装备强化成功: characterId={}, userId={}, slotType={}, itemId={}, newLevel={}",
                    dto.getCharacterId(), dto.getUserId(), dto.getSlotType(), 
                    equipment.getItemId(), currentLevel + 1);
        } else {
            // 强化失败：等级不变，材料已消耗
            result.setSuccess(false);
            result.setNewLevel(currentLevel);
            result.setMessage(String.format("强化失败！%s 强化等级仍为 +%d", template.getName(), currentLevel));

            log.info("装备强化失败: characterId={}, userId={}, slotType={}, itemId={}, level={}",
                    dto.getCharacterId(), dto.getUserId(), dto.getSlotType(), 
                    equipment.getItemId(), currentLevel);
        }

        return result;
    }

    /**
     * 生成随机词条（装备获取时调用）
     * @param rarity 稀有度
     * @return 随机词条列表
     */
    public List<Map<String, Object>> generateRandomExtraStats(String rarity) {
        List<Map<String, Object>> extraStats = new ArrayList<>();
        if (rarity == null || "Normal".equals(rarity)) {
            return extraStats; // 普通品质无随机词条
        }

        // 根据稀有度确定词条数量
        int minCount = 0;
        int maxCount = 0;
        switch (rarity) {
            case "Rare":
                minCount = 1;
                maxCount = 2;
                break;
            case "Epic":
                minCount = 3;
                maxCount = 4;
                break;
            case "Legendary":
                minCount = 4;
                maxCount = 6;
                break;
        }

        // 随机生成词条数量
        int count = minCount + new Random().nextInt(maxCount - minCount + 1);
        
        // 可选属性列表
        String[] statKeys = {"physicalAttack", "magicAttack", "defense", "hp", "mp", 
                             "criticalRate", "dodgeRate", "strength", "intelligence", "agility"};
        
        // 随机选择属性并生成数值
        Set<Integer> usedIndexes = new HashSet<>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int index;
            do {
                index = random.nextInt(statKeys.length);
            } while (usedIndexes.contains(index));
            usedIndexes.add(index);
            
            Map<String, Object> stat = new HashMap<>();
            stat.put("key", statKeys[index]);
            
            // 根据属性类型生成数值（百分比类为小数，其他为整数）
            String key = statKeys[index];
            if ("criticalRate".equals(key) || "dodgeRate".equals(key)) {
                // 暴击率和闪避率：0.01 ~ 0.05 (1% ~ 5%)
                stat.put("value", Math.round((0.01 + random.nextDouble() * 0.04) * 100.0) / 100.0);
            } else {
                // 其他属性：1 ~ 10
                stat.put("value", 1 + random.nextInt(10));
            }
            
            extraStats.add(stat);
        }
        
        return extraStats;
    }
}
