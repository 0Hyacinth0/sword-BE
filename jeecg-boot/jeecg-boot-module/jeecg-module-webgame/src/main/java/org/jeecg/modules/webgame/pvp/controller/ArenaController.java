package org.jeecg.modules.webgame.pvp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.webgame.pvp.dto.PvpMatchRequestDTO;
import org.jeecg.modules.webgame.pvp.dto.PvpSettlementRequestDTO;
import org.jeecg.modules.webgame.pvp.service.IArenaService;
import org.jeecg.modules.webgame.pvp.vo.ArenaPlayerVO;
import org.jeecg.modules.webgame.pvp.vo.ArenaSeasonVO;
import org.jeecg.modules.webgame.pvp.vo.PvpMatchOpponentVO;
import org.jeecg.modules.webgame.pvp.vo.PvpSettlementResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 竞技场控制器
 * @Author: jeecg-boot
 * @Date: 2026-05-19
 */
@Slf4j
@Tag(name = "竞技场系统接口")
@RestController
@RequestMapping("/webgame/arena")
public class ArenaController {

    @Autowired
    private IArenaService arenaService;

    /**
     * 获取当前赛季信息
     */
    @Operation(summary = "获取当前赛季")
    @GetMapping("/season")
    public Result<ArenaSeasonVO> getCurrentSeason() {
        try {
            log.info("请求获取当前赛季信息");
            ArenaSeasonVO season = arenaService.getCurrentSeason();
            if (season == null) {
                return Result.error("未找到活跃赛季");
            }
            return Result.OK(season);
        } catch (Exception e) {
            log.error("获取赛季信息失败", e);
            return Result.error("获取赛季信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取玩家竞技数据
     * 注意：实际项目中应从token中获取characterId，这里简化处理
     */
    @Operation(summary = "获取玩家竞技数据")
    @GetMapping("/me")
    public Result<ArenaPlayerVO> getPlayerArenaData(String characterId) {
        try {
            if (characterId == null || characterId.isEmpty()) {
                return Result.error("角色ID不能为空");
            }
            
            log.info("请求获取玩家竞技数据: characterId={}", characterId);
            ArenaPlayerVO playerData = arenaService.getPlayerArenaData(characterId);
            if (playerData == null) {
                return Result.error("获取玩家竞技数据失败");
            }
            return Result.OK(playerData);
        } catch (Exception e) {
            log.error("获取玩家竞技数据失败", e);
            return Result.error("获取玩家竞技数据失败: " + e.getMessage());
        }
    }

    /**
     * PVP匹配对手
     */
    @Operation(summary = "PVP匹配")
    @PostMapping("/match")
    public Result<PvpMatchOpponentVO> matchOpponent(@RequestBody @Validated PvpMatchRequestDTO request, 
                                                    @RequestParam String characterId) {
        try {
            if (characterId == null || characterId.isEmpty()) {
                return Result.error("角色ID不能为空");
            }
            
            log.info("请求PVP匹配: characterId={}, score={}", characterId, request.getScore());
            PvpMatchOpponentVO opponent = arenaService.matchOpponent(characterId, request);
            if (opponent == null) {
                return Result.error("当前没有合适的对手，请稍后再试（测试环境需要其他玩家在线）");
            }
            return Result.OK("匹配成功", opponent);
        } catch (Exception e) {
            log.error("PVP匹配失败", e);
            return Result.error("PVP匹配失败: " + e.getMessage());
        }
    }

    /**
     * 结算PVP战斗结果
     */
    @Operation(summary = "PVP战斗结算")
    @PostMapping("/settle")
    public Result<PvpSettlementResultVO> settleBattle(@RequestBody @Validated PvpSettlementRequestDTO request) {
        try {
            if (request.getPlayerCharacterId() == null || request.getPlayerCharacterId().isEmpty()) {
                return Result.error("玩家角色ID不能为空");
            }
            if (request.getOpponentCharacterId() == null || request.getOpponentCharacterId().isEmpty()) {
                return Result.error("对手角色ID不能为空（请先成功匹配对手）");
            }
            if (request.getWon() == null) {
                return Result.error("请指定战斗结果（胜利或失败）");
            }
            
            log.info("请求PVP结算: player={}, opponent={}, won={}", 
                    request.getPlayerCharacterId(), request.getOpponentCharacterId(), request.getWon());
            PvpSettlementResultVO result = arenaService.settleBattle(
                    request.getPlayerCharacterId(), 
                    request.getOpponentCharacterId(), 
                    request.getWon()
            );
            if (result == null) {
                return Result.error("结算失败");
            }
            return Result.OK("结算成功", result);
        } catch (Exception e) {
            log.error("PVP结算失败", e);
            return Result.error("PVP结算失败: " + e.getMessage());
        }
    }
}
