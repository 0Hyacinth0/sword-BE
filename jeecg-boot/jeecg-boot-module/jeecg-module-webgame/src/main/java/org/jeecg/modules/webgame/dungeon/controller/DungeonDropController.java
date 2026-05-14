package org.jeecg.modules.webgame.dungeon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.webgame.battle.vo.BattleRewardItemVO;
import org.jeecg.modules.webgame.dungeon.dto.DungeonDropTableDTO;
import org.jeecg.modules.webgame.dungeon.dto.EliteDungeonConfigDTO;
import org.jeecg.modules.webgame.dungeon.service.IDungeonDropService;
import org.jeecg.modules.webgame.dungeon.service.IEliteDungeonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 副本掉落控制器
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Slf4j
@Tag(name = "副本掉落接口")
@RestController
@RequestMapping("/webgame/dungeon")
public class DungeonDropController {

    @Autowired
    private IDungeonDropService dungeonDropService;

    @Autowired
    private IEliteDungeonService eliteDungeonService;

    /**
     * 获取副本掉落表配置
     * @param dungeonId 副本ID
     * @return 掉落表配置
     */
    @Operation(summary = "获取副本掉落表")
    @GetMapping("/drop-table/{dungeonId}")
    public Result<DungeonDropTableDTO> getDropTable(@PathVariable("dungeonId") String dungeonId) {
        try {
            DungeonDropTableDTO dropTable = dungeonDropService.getDropTable(dungeonId);
            return Result.OK("获取成功", dropTable);
        } catch (Exception e) {
            log.error("获取副本掉落表失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 解析掉落（模拟掷骰）
     * @param dungeonId 副本ID
     * @param isBossFloor 是否为Boss层
     * @return 掉落物品列表
     */
    @Operation(summary = "解析副本掉落")
    @PostMapping("/resolve-drops")
    public Result<List<BattleRewardItemVO>> resolveDrops(
            @RequestParam("dungeonId") String dungeonId,
            @RequestParam("isBossFloor") boolean isBossFloor) {
        try {
            List<BattleRewardItemVO> drops = dungeonDropService.resolveDrops(dungeonId, isBossFloor);
            return Result.OK("掉落解析成功", drops);
        } catch (Exception e) {
            log.error("解析副本掉落失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取精英副本配置
     * @param dungeonId 副本ID
     * @return 精英副本配置
     */
    @Operation(summary = "获取精英副本配置")
    @GetMapping("/elite-config/{dungeonId}")
    public Result<EliteDungeonConfigDTO> getEliteConfig(@PathVariable("dungeonId") String dungeonId) {
        try {
            EliteDungeonConfigDTO config = eliteDungeonService.getEliteConfig(dungeonId);
            return Result.OK("获取成功", config);
        } catch (Exception e) {
            log.error("获取精英副本配置失败", e);
            return Result.error(e.getMessage());
        }
    }
}
