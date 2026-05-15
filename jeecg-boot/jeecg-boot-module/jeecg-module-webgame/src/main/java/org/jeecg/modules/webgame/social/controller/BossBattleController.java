package org.jeecg.modules.webgame.social.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.webgame.social.service.IBossBattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description: 团队副本Boss战 Controller
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Slf4j
@RestController
@RequestMapping("/webgame/boss-battle")
public class BossBattleController {
    
    @Autowired
    private IBossBattleService bossBattleService;
    
    /**
     * 初始化Boss战斗
     */
    @PostMapping("/init")
    public Result<Map<String, Object>> initBossBattle(
            @RequestParam String battleId,
            @RequestParam String bossId,
            HttpServletRequest request) {
        
        log.info("初始化Boss战斗, battleId: {}, bossId: {}", battleId, bossId);
        
        Map<String, Object> data = bossBattleService.initBossBattle(battleId, bossId);
        return Result.OK("Boss战斗开始", data);
    }
    
    /**
     * 切换Boss阶段
     */
    @PostMapping("/switch-phase")
    public Result<Map<String, Object>> switchPhase(
            @RequestParam String battleId,
            @RequestParam Integer newPhase,
            HttpServletRequest request) {
        
        log.info("切换Boss阶段, battleId: {}, phase: {}", battleId, newPhase);
        
        Map<String, Object> data = bossBattleService.switchPhase(battleId, newPhase);
        return Result.OK("阶段切换成功", data);
    }
    
    /**
     * 触发狂暴
     */
    @PostMapping("/enrage")
    public Result<Map<String, Object>> triggerEnrage(
            @RequestParam String battleId,
            HttpServletRequest request) {
        
        log.info("触发狂暴, battleId: {}", battleId);
        
        Map<String, Object> data = bossBattleService.triggerEnrage(battleId);
        return Result.OK("狂暴已触发", data);
    }
    
    /**
     * 复活队友
     */
    @PostMapping("/revive")
    public Result<Map<String, Object>> reviveTeammate(
            @RequestParam String battleId,
            @RequestParam String revivedId,
            HttpServletRequest request) {
        
        String reviverId = (String) request.getAttribute("characterId");
        log.info("复活队友, battleId: {}, reviverId: {}, revivedId: {}", battleId, reviverId, revivedId);
        
        Map<String, Object> data = bossBattleService.reviveTeammate(battleId, reviverId, revivedId);
        return Result.OK("复活成功", data);
    }
    
    /**
     * 使用全屏AOE技能
     */
    @PostMapping("/aoe-skill")
    public Result<Map<String, Object>> useAoeSkill(
            @RequestParam String battleId,
            @RequestParam String skillId,
            HttpServletRequest request) {
        
        log.info("使用AOE技能, battleId: {}, skillId: {}", battleId, skillId);
        
        Map<String, Object> data = bossBattleService.useAoeSkill(battleId, skillId);
        return Result.OK("技能释放成功", data);
    }
}
