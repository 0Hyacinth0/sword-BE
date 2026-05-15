package org.jeecg.modules.webgame.social.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.webgame.social.service.IMultiBattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description: 多人副本战斗 Controller
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Slf4j
@RestController
@RequestMapping("/webgame/multi-battle")
public class MultiBattleController {
    
    @Autowired
    private IMultiBattleService multiBattleService;
    
    /**
     * 开始多人副本战斗
     */
    @PostMapping("/start")
    public Result<Map<String, Object>> startMultiBattle(
            @RequestParam String roomId,
            @RequestParam String dungeonId,
            HttpServletRequest request) {
        
        log.info("开始多人副本战斗, roomId: {}, dungeonId: {}", roomId, dungeonId);
        
        Map<String, Object> battleData = multiBattleService.initMultiBattle(roomId, dungeonId);
        return Result.OK("战斗开始", battleData);
    }
    
    /**
     * 楼层结算
     */
    @PostMapping("/floor-complete")
    public Result<Map<String, Object>> floorComplete(
            @RequestParam String battleId,
            @RequestParam Integer floor,
            @RequestParam(required = false, defaultValue = "false") boolean isBossFloor,
            @RequestBody List<String> memberIds,
            HttpServletRequest request) {
        
        log.info("楼层结算, battleId: {}, floor: {}, isBoss: {}", battleId, floor, isBossFloor);
        
        Map<String, Object> result = multiBattleService.floorComplete(battleId, floor, isBossFloor, memberIds);
        return Result.OK("楼层结算成功", result);
    }
    
    /**
     * 副本通关结算
     */
    @PostMapping("/complete")
    public Result<Map<String, Object>> dungeonComplete(
            @RequestParam String battleId,
            @RequestBody List<String> memberIds,
            HttpServletRequest request) {
        
        log.info("副本通关结算, battleId: {}", battleId);
        
        Map<String, Object> result = multiBattleService.dungeonComplete(battleId, memberIds);
        return Result.OK("副本通关", result);
    }
}
