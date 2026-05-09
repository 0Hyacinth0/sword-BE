package org.jeecg.modules.webgame.equipment.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.webgame.character.vo.EquipmentVO;
import org.jeecg.modules.webgame.common.controller.BaseWebGameController;
import org.jeecg.modules.webgame.equipment.dto.EquipItemDTO;
import org.jeecg.modules.webgame.equipment.dto.EnhanceEquipmentDTO;
import org.jeecg.modules.webgame.equipment.dto.UnequipItemDTO;
import org.jeecg.modules.webgame.equipment.service.IEquipmentService;
import org.jeecg.modules.webgame.equipment.vo.EnhanceResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description: 装备系统Controller
 * @Author: jeecg-boot
 * @Date: 2026-05-09
 */
@Slf4j
@RestController
@RequestMapping("/webgame/equipment")
public class EquipmentController extends BaseWebGameController {

    @Autowired
    private IEquipmentService equipmentService;

    /**
     * 获取角色装备列表
     * @param request HTTP请求
     * @param characterId 角色ID
     * @return 装备列表
     */
    @GetMapping("/list/{characterId}")
    public Result<Map<String, EquipmentVO>> getEquipmentList(HttpServletRequest request, @PathVariable String characterId) {
        try {
            String userId = getCurrentUserId(request);
            Map<String, EquipmentVO> equipmentList = equipmentService.getEquipmentList(characterId, userId);
            return Result.OK("获取成功", equipmentList);
        } catch (Exception e) {
            log.error("获取装备列表失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 穿戴装备
     * @param request HTTP请求
     * @param dto 穿戴请求
     * @return 操作结果
     */
    @PostMapping("/equip")
    public Result<Void> equipItem(HttpServletRequest request, @RequestBody @Validated EquipItemDTO dto) {
        try {
            String userId = getCurrentUserId(request);
            dto.setUserId(userId);
            equipmentService.equipItem(dto);
            return Result.OK("穿戴成功");
        } catch (Exception e) {
            log.error("穿戴装备失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 卸下装备
     * @param request HTTP请求
     * @param dto 卸下请求
     * @return 操作结果
     */
    @PostMapping("/unequip")
    public Result<Void> unequipItem(HttpServletRequest request, @RequestBody @Validated UnequipItemDTO dto) {
        try {
            String userId = getCurrentUserId(request);
            dto.setUserId(userId);
            equipmentService.unequipItem(dto);
            return Result.OK("卸下成功");
        } catch (Exception e) {
            log.error("卸下装备失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 强化装备
     * @param request HTTP请求
     * @param dto 强化请求
     * @return 强化结果
     */
    @PostMapping("/enhance")
    public Result<EnhanceResultVO> enhanceEquipment(HttpServletRequest request, @RequestBody @Validated EnhanceEquipmentDTO dto) {
        try {
            String userId = getCurrentUserId(request);
            dto.setUserId(userId);
            EnhanceResultVO result = equipmentService.enhanceEquipment(dto);
            return Result.OK(result.getMessage(), result);
        } catch (Exception e) {
            log.error("强化装备失败", e);
            return Result.error(e.getMessage());
        }
    }
}
