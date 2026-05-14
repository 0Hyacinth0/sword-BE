package org.jeecg.modules.webgame.battle.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.modules.webgame.battle.dto.BattleActionDTO;
import org.jeecg.modules.webgame.battle.dto.StartBattleDTO;
import org.jeecg.modules.webgame.battle.service.IBattleService;
import org.jeecg.modules.webgame.battle.vo.BattleEndVO;
import org.jeecg.modules.webgame.battle.vo.BattleStateVO;
import org.jeecg.modules.webgame.battle.vo.SkillListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 战斗控制器
 * @Author: jeecg-boot
 * @Date: 2026-05-13
 */
@Slf4j
@Tag(name = "战斗系统接口")
@RestController
@RequestMapping("/webgame/battle")
public class BattleController {

    @Autowired
    private IBattleService battleService;

    /**
     * 发起战斗
     */
    @Operation(summary = "发起战斗")
    @PostMapping("/start")
    public Result<BattleStateVO> startBattle(@Validated @RequestBody StartBattleDTO dto) {
        try {
            log.info("发起战斗请求: characterId={}, enemyGroupId={}", dto.getCharacterId(), dto.getEnemyGroupId());
            BattleStateVO battleState = battleService.startBattle(dto);
            return Result.OK("战斗开始", battleState);
        } catch (Exception e) {
            log.error("发起战斗失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 提交玩家行动
     */
    @Operation(summary = "提交行动")
    @PostMapping("/action")
    public Result<BattleStateVO> submitAction(@Validated @RequestBody BattleActionDTO dto) {
        try {
            log.info("提交行动: battleId={}, type={}", dto.getBattleId(), dto.getType());
            BattleStateVO battleState = battleService.submitAction(dto);
            return Result.OK("行动执行成功", battleState);
        } catch (Exception e) {
            log.error("提交行动失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 结束战斗并领取奖励
     */
    @Operation(summary = "结束战斗")
    @PostMapping("/end/{battleId}")
    public Result<BattleEndVO> endBattle(@PathVariable("battleId") String battleId) {
        try {
            log.info("结束战斗: battleId={}", battleId);
            BattleEndVO result = battleService.endBattle(battleId);
            
            String message = "defeat".equals(result.getOutcome()) ? "战斗失败" : 
                           "fled".equals(result.getOutcome()) ? "逃跑成功" : "战斗胜利！";
            return Result.OK(message, result);
        } catch (Exception e) {
            log.error("结束战斗失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取角色技能列表
     */
    @Operation(summary = "获取角色技能列表")
    @GetMapping("/skills/{characterId}")
    public Result<SkillListVO> getCharacterSkills(@PathVariable("characterId") String characterId) {
        try {
            log.info("获取角色技能列表: characterId={}", characterId);
            SkillListVO skillList = battleService.getCharacterSkills(characterId);
            return Result.OK(skillList);
        } catch (Exception e) {
            log.error("获取技能列表失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询战斗状态（用于断线重连）
     */
    @Operation(summary = "查询战斗状态")
    @GetMapping("/state/{battleId}")
    public Result<BattleStateVO> getBattleState(@PathVariable("battleId") String battleId) {
        try {
            log.info("查询战斗状态: battleId={}", battleId);
            BattleStateVO battleState = battleService.getBattleState(battleId);
            return Result.OK(battleState);
        } catch (Exception e) {
            log.error("查询战斗状态失败", e);
            return Result.error(e.getMessage());
        }
    }
}
