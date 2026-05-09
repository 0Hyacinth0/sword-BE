package org.jeecg.modules.webgame.item.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.webgame.common.controller.BaseWebGameController;
import org.jeecg.modules.webgame.item.dto.DiscardItemDTO;
import org.jeecg.modules.webgame.item.dto.UseItemDTO;
import org.jeecg.modules.webgame.item.service.IInventoryService;
import org.jeecg.modules.webgame.item.vo.InventoryItemVO;
import org.jeecg.modules.webgame.item.vo.UseItemResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 背包系统控制器
 * @Author: jeecg-boot
 * @Date: 2026-05-08
 */
@Slf4j
@Tag(name = "背包系统接口")
@RestController
@RequestMapping("/webgame/inventory")
public class InventoryController extends BaseWebGameController {

    @Autowired
    private IInventoryService inventoryService;

    /**
     * 获取角色背包列表
     * @param request HTTP请求
     * @param characterId 角色ID
     * @return 背包物品列表
     */
    @Operation(summary = "获取背包列表")
    @GetMapping("/list/{characterId}")
    public Result<List<InventoryItemVO>> getInventoryList(HttpServletRequest request, @PathVariable String characterId) {
        try {
            // 从 Token 中获取用户ID
            String userId = getCurrentUserId(request);
            
            List<InventoryItemVO> inventoryList = inventoryService.getInventoryList(characterId, userId);
            return Result.OK("获取成功", inventoryList);
        } catch (Exception e) {
            log.error("获取背包列表失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 使用消耗品
     * @param request HTTP请求
     * @param dto 使用物品参数
     * @return 使用结果
     */
    @Operation(summary = "使用消耗品")
    @PostMapping("/use")
    public Result<UseItemResultVO> useItem(HttpServletRequest request, @Validated @RequestBody UseItemDTO dto) {
        try {
            // 从 Token 中获取用户ID
            String userId = getCurrentUserId(request);
            
            // 设置 userId 到 DTO 中用于权限验证
            dto.setUserId(userId);
            
            UseItemResultVO result = inventoryService.useItem(dto);
            return Result.OK(result.getMessage(), result);
        } catch (Exception e) {
            log.error("使用物品失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 丢弃物品
     * @param request HTTP请求
     * @param dto 丢弃物品参数
     * @return 操作结果
     */
    @Operation(summary = "丢弃物品")
    @PostMapping("/discard")
    public Result<Void> discardItem(HttpServletRequest request, @Validated @RequestBody DiscardItemDTO dto) {
        try {
            // 从 Token 中获取用户ID
            String userId = getCurrentUserId(request);
            
            // 设置 userId 到 DTO 中用于权限验证
            dto.setUserId(userId);
            
            inventoryService.discardItem(dto);
            return Result.OK("丢弃成功", null);
        } catch (Exception e) {
            log.error("丢弃物品失败", e);
            return Result.error(e.getMessage());
        }
    }
}
