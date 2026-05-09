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
    public List<InventoryItemVO> getInventoryList(String characterId, String userId) {
        // 1. 校验角色是否存在
        WgCharacter character = characterMapper.selectById(characterId);
        if (character == null || character.getDelFlag() == 1) {
            throw new JeecgBootException("角色不存在");
        }

        // 2. 权限验证：只能查看自己的角色背包
        if (!character.getUserId().equals(userId)) {
            throw new JeecgBootException("无权查看该角色的背包");
        }

        // 3. 查询背包列表（JOIN物品模板表）
        List<InventoryItemVO> inventoryList = inventoryMapper.selectInventoryWithItems(characterId);
        
        log.info("获取背包列表成功: characterId={}, userId={}, itemCount={}", characterId, userId, inventoryList.size());
        
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
        
        // 3. 权限验证：查询角色信息，验证 userId
        WgCharacter character = characterMapper.selectById(dto.getCharacterId());
        if (character == null || character.getDelFlag() == 1) {
            throw new JeecgBootException("角色不存在");
        }
        if (!character.getUserId().equals(dto.getUserId())) {
            throw new JeecgBootException("无权操作该角色的背包");
        }

        // 4. 校验数量
        if (dto.getQuantity() > inventory.getQuantity()) {
            throw new JeecgBootException("数量不足");
        }

        // 5. 查询物品模板
        WgItemTemplate itemTemplate = itemTemplateMapper.selectById(inventory.getItemId());
        if (itemTemplate == null) {
            throw new JeecgBootException("物品模板不存在");
        }

        // 6. 校验物品类型
        // 注意：根据最新表结构，type 字段可能代表不同类型，此处仅做基础校验
        if (itemTemplate.getType() == null) {
            throw new JeecgBootException("物品类型配置错误");
        }

        // TODO: 根据实际业务需求补充消耗品使用逻辑
        // 目前表结构已变更为装备/材料模板，消耗品逻辑需根据新字段调整
        // throw new JeecgBootException("物品使用功能待适配新表结构");

        // 7. 根据效果类型执行相应逻辑（暂时保留原有逻辑）
        List<String> effects = new ArrayList<>();
        String message = "";

        // 暂时默认处理，后续可根据 type 字段实现不同逻辑
        effects.add("使用物品成功");
        message = String.format("成功使用 %s×%d", itemTemplate.getName(), dto.getQuantity());

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

        log.info("使用物品成功: characterId={}, userId={}, itemId={}, quantity={}", 
                dto.getCharacterId(), dto.getUserId(), inventory.getItemId(), dto.getQuantity());

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
        
        // 3. 权限验证：查询角色信息，验证 userId
        WgCharacter character = characterMapper.selectById(dto.getCharacterId());
        if (character == null || character.getDelFlag() == 1) {
            throw new JeecgBootException("角色不存在");
        }
        if (!character.getUserId().equals(dto.getUserId())) {
            throw new JeecgBootException("无权操作该角色的背包");
        }

        // 4. 校验数量
        if (dto.getQuantity() > inventory.getQuantity()) {
            throw new JeecgBootException("数量不足");
        }

        // 5. 扣减数量
        int newQuantity = inventory.getQuantity() - dto.getQuantity();
        if (newQuantity <= 0) {
            // 删除背包记录
            inventoryMapper.deleteById(dto.getInventoryId());
        } else {
            // 更新数量
            inventoryMapper.updateQuantity(dto.getInventoryId(), newQuantity);
        }

        log.info("丢弃物品成功: characterId={}, userId={}, itemId={}, quantity={}", 
                dto.getCharacterId(), dto.getUserId(), inventory.getItemId(), dto.getQuantity());
    }
}
