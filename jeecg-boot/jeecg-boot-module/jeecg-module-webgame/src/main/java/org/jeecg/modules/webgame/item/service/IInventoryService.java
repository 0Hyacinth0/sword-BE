package org.jeecg.modules.webgame.item.service;

import org.jeecg.modules.webgame.item.dto.DiscardItemDTO;
import org.jeecg.modules.webgame.item.dto.UseItemDTO;
import org.jeecg.modules.webgame.item.vo.InventoryItemVO;
import org.jeecg.modules.webgame.item.vo.UseItemResultVO;

import java.util.List;

/**
 * @Description: 物品背包Service
 * @Author: jeecg-boot
 * @Date: 2026-05-08
 */
public interface IInventoryService {

    /**
     * 获取角色背包列表
     * @param characterId 角色ID
     * @return 背包物品列表
     */
    List<InventoryItemVO> getInventoryList(String characterId);

    /**
     * 使用消耗品
     * @param dto 使用物品参数
     * @return 使用结果
     */
    UseItemResultVO useItem(UseItemDTO dto);

    /**
     * 丢弃物品
     * @param dto 丢弃物品参数
     */
    void discardItem(DiscardItemDTO dto);
}
