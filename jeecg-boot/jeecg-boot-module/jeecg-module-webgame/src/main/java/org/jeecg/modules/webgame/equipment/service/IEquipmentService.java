package org.jeecg.modules.webgame.equipment.service;

import org.jeecg.modules.webgame.character.vo.EquipmentVO;
import org.jeecg.modules.webgame.equipment.dto.EquipItemDTO;
import org.jeecg.modules.webgame.equipment.dto.EnhanceEquipmentDTO;
import org.jeecg.modules.webgame.equipment.dto.UnequipItemDTO;
import org.jeecg.modules.webgame.equipment.vo.EnhanceResultVO;

import java.util.Map;

/**
 * @Description: 装备系统Service接口
 * @Author: jeecg-boot
 * @Date: 2026-05-09
 */
public interface IEquipmentService {

    /**
     * 获取角色装备列表
     * @param characterId 角色ID
     * @param userId 用户ID
     * @return 装备列表(6个槽位)
     */
    Map<String, EquipmentVO> getEquipmentList(String characterId, String userId);

    /**
     * 穿戴装备
     * @param dto 穿戴请求
     */
    void equipItem(EquipItemDTO dto);

    /**
     * 卸下装备
     * @param dto 卸下请求
     */
    void unequipItem(UnequipItemDTO dto);

    /**
     * 强化装备
     * @param dto 强化请求
     * @return 强化结果
     */
    EnhanceResultVO enhanceEquipment(EnhanceEquipmentDTO dto);
}