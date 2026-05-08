package org.jeecg.modules.webgame.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.webgame.character.entity.WgCharacter;
import org.jeecg.modules.webgame.character.mapper.WgCharacterMapper;
import org.jeecg.modules.webgame.character.service.IWgCharacterService;
import org.jeecg.modules.webgame.item.dto.DiscardItemDTO;
import org.jeecg.modules.webgame.item.dto.UseItemDTO;
import org.jeecg.modules.webgame.item.entity.CharacterInventory;
import org.jeecg.modules.webgame.item.entity.WgItemTemplate;
import org.jeecg.modules.webgame.item.mapper.CharacterInventoryMapper;
import org.jeecg.modules.webgame.item.mapper.WgItemTemplateMapper;
import org.jeecg.modules.webgame.item.service.IInventoryService;
import org.jeecg.modules.webgame.item.vo.InventoryItemVO;
import org.jeecg.modules.webgame.item.vo.UseItemResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 物品背包Service实现
 * @Author: jeecg-boot
 * @Date: 2026-05-08
 */
@Slf4j
@Service
public class InventoryServiceImpl implements IInventoryService {

    @Autowired
    private CharacterInventoryMapper inventoryMapper;

    @Autowired
    private WgItemTemplateMapper itemTemplateMapper;

    @Autowired
    private WgCharacterMapper characterMapper;

    @Autowired
    private IWgCharacterService characterService;

    @Override
    public List<InventoryItemVO> getInventoryList(String characterId) {
        // 1. 校验角色是否存在
        WgCharacter character = characterMapper.selectById(characterId);
        if (character == null || character.getDelFlag() == 1) {
            throw new JeecgBootException("角色不存在");
        }

        // 2. 查询背包列表（JOIN物品模板表）
        List<InventoryItemVO> inventoryList = inventoryMapper.selectInventoryWithItems(characterId);
        
        log.info("获取背包列表成功: characterId={}, itemCount={}", characterId, inventoryList.size());
        
        return inventoryList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UseItemResultVO useItem(UseItemDTO dto) {
        // 1. 查询背包记录
        CharacterInventory inventory = inventoryMapper.selectById(dto.getInventoryId());
        if (inventory == null) {
            throw new JeecgBootException("物品不存在");
        }

        // 2. 校验是否属于该角色
        if (!inventory.getCharacterId().equals(dto.getCharacterId())) {
            throw new JeecgBootException("无权操作该角色背包");
        }

        // 3. 校验数量
        if (dto.getQuantity() > inventory.getQuantity()) {
            throw new JeecgBootException("数量不足");
        }

        // 4. 查询物品模板
        WgItemTemplate itemTemplate = itemTemplateMapper.selectById(inventory.getItemId());
        if (itemTemplate == null) {
            throw new JeecgBootException("物品模板不存在");
        }

        // 5. 校验物品类型（只有消耗品可以使用）
        if (!"consumable".equals(itemTemplate.getCategory())) {
            throw new JeecgBootException("该物品无法使用");
        }

        // 6. 查询角色信息
        WgCharacter character = characterMapper.selectById(dto.getCharacterId());
        if (character == null || character.getDelFlag() == 1) {
            throw new JeecgBootException("角色不存在");
        }

        // 7. 根据效果类型执行相应逻辑
        List<String> effects = new ArrayList<>();
        String message = "";

        switch (itemTemplate.getEffectType()) {
            case "heal_hp":
                // 恢复生命值
                if (character.getHp() >= character.getMaxHp()) {
                    throw new JeecgBootException("HP 已满，无需恢复");
                }
                
                int healHpAmount = Math.min(
                    itemTemplate.getEffectValue() * dto.getQuantity(),
                    character.getMaxHp() - character.getHp()
                );
                character.setHp(character.getHp() + healHpAmount);
                effects.add("恢复 " + healHpAmount + " HP");
                message = String.format("成功使用 %s×%d，恢复 %d 点生命值", 
                    itemTemplate.getItemName(), dto.getQuantity(), healHpAmount);
                break;

            case "heal_mp":
                // 恢复魔法值
                if (character.getMp() >= character.getMaxMp()) {
                    throw new JeecgBootException("MP 已满，无需恢复");
                }
                
                int healMpAmount = Math.min(
                    itemTemplate.getEffectValue() * dto.getQuantity(),
                    character.getMaxMp() - character.getMp()
                );
                character.setMp(character.getMp() + healMpAmount);
                effects.add("恢复 " + healMpAmount + " MP");
                message = String.format("成功使用 %s×%d，恢复 %d 点魔法值", 
                    itemTemplate.getItemName(), dto.getQuantity(), healMpAmount);
                break;

            case "add_exp":
                // 增加经验值（调用现有的增加经验接口）
                int expToAdd = itemTemplate.getEffectValue() * dto.getQuantity();
                
                // 这里需要调用 characterService.addExperience
                // 但由于返回类型不同，我们直接在这里处理
                org.jeecg.modules.webgame.character.dto.AddExperienceDTO expDto = 
                    new org.jeecg.modules.webgame.character.dto.AddExperienceDTO();
                expDto.setCharacterId(dto.getCharacterId());
                expDto.setExpToAdd(expToAdd);
                
                org.jeecg.modules.webgame.character.vo.AddExperienceResultVO expResult = 
                    characterService.addExperience(expDto);
                
                effects.add("获得 " + expToAdd + " 经验值");
                
                if (expResult.getLevelUp() != null && expResult.getLevelUp().getLevelsGained() > 0) {
                    message = String.format("成功使用 %s×%d，获得 %d 经验值，等级提升至 %d！", 
                        itemTemplate.getItemName(), dto.getQuantity(), expToAdd, 
                        expResult.getLevelUp().getNewLevel());
                } else {
                    message = String.format("成功使用 %s×%d，获得 %d 经验值", 
                        itemTemplate.getItemName(), dto.getQuantity(), expToAdd);
                }
                break;

            case "revive":
                // 复活角色
                if (character.getHp() > 0) {
                    throw new JeecgBootException("角色未阵亡，无法使用");
                }
                
                // 恢复 value% 最大生命值
                int reviveHp = (int) Math.floor(character.getMaxHp() * itemTemplate.getEffectValue() / 100.0);
                character.setHp(reviveHp);
                effects.add("复活并恢复 " + itemTemplate.getEffectValue() + "% HP");
                message = String.format("成功使用 %s×%d，复活并恢复 %d 点生命值", 
                    itemTemplate.getItemName(), dto.getQuantity(), reviveHp);
                break;

            default:
                throw new JeecgBootException("未知的物品效果类型");
        }

        // 8. 更新角色数据
        character.setUpdateTime(new Date());
        characterMapper.updateById(character);

        // 9. 扣减物品数量
        int newQuantity = inventory.getQuantity() - dto.getQuantity();
        if (newQuantity <= 0) {
            // 删除背包记录
            inventoryMapper.deleteById(dto.getInventoryId());
        } else {
            // 更新数量
            inventoryMapper.updateQuantity(dto.getInventoryId(), newQuantity);
        }

        log.info("使用物品成功: characterId={}, itemId={}, quantity={}, effect={}", 
                dto.getCharacterId(), inventory.getItemId(), dto.getQuantity(), itemTemplate.getEffectType());

        // 10. 返回结果
        return new UseItemResultVO(effects, message);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void discardItem(DiscardItemDTO dto) {
        // 1. 查询背包记录
        CharacterInventory inventory = inventoryMapper.selectById(dto.getInventoryId());
        if (inventory == null) {
            throw new JeecgBootException("物品不存在");
        }

        // 2. 校验是否属于该角色
        if (!inventory.getCharacterId().equals(dto.getCharacterId())) {
            throw new JeecgBootException("无权操作该角色背包");
        }

        // 3. 校验数量
        if (dto.getQuantity() > inventory.getQuantity()) {
            throw new JeecgBootException("数量不足");
        }

        // 4. 扣减数量
        int newQuantity = inventory.getQuantity() - dto.getQuantity();
        if (newQuantity <= 0) {
            // 删除背包记录
            inventoryMapper.deleteById(dto.getInventoryId());
        } else {
            // 更新数量
            inventoryMapper.updateQuantity(dto.getInventoryId(), newQuantity);
        }

        log.info("丢弃物品成功: characterId={}, itemId={}, quantity={}", 
                dto.getCharacterId(), inventory.getItemId(), dto.getQuantity());
    }
}
