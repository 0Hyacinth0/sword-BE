package org.jeecg.modules.webgame.map.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.webgame.map.dto.EnterMapDTO;
import org.jeecg.modules.webgame.map.dto.MapAreaDTO;
import org.jeecg.modules.webgame.map.dto.WildBattleSettleDTO;
import org.jeecg.modules.webgame.map.service.IWgMapAreaService;
import org.jeecg.modules.webgame.map.service.IWildBattleService;
import org.jeecg.modules.webgame.map.vo.EnterMapResultVO;
import org.jeecg.modules.webgame.map.vo.WildBattleSettleResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 世界地图控制器
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Slf4j
@Tag(name = "世界地图接口")
@RestController
@RequestMapping("/webgame/map")
public class WgMapController {

    @Autowired
    private IWgMapAreaService mapAreaService;

    @Autowired
    private IWildBattleService wildBattleService;

    /**
     * 获取所有区域列表
     * @return 区域列表（含怪物和掉落信息）
     */
    @Operation(summary = "获取所有区域列表")
    @GetMapping("/areas")
    public Result<List<MapAreaDTO>> getAllAreas() {
        try {
            List<MapAreaDTO> areas = mapAreaService.getAllAreas();
            return Result.OK("获取成功", areas);
        } catch (Exception e) {
            log.error("获取区域列表失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取单个区域详情
     * @param areaId 区域ID
     * @return 区域详情
     */
    @Operation(summary = "获取区域详情")
    @GetMapping("/area/{areaId}")
    public Result<MapAreaDTO> getAreaById(@PathVariable("areaId") String areaId) {
        try {
            MapAreaDTO area = mapAreaService.getAreaById(areaId);
            if (area == null) {
                return Result.error("区域不存在");
            }
            return Result.OK("获取成功", area);
        } catch (Exception e) {
            log.error("获取区域详情失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 野外战斗结算（验证模式）
     * @param settleDTO 结算请求
     * @return 结算结果
     */
    @Operation(summary = "野外战斗结算")
    @PostMapping("/battle/settle")
    public Result<WildBattleSettleResultVO> settleWildBattle(@Validated @RequestBody WildBattleSettleDTO settleDTO) {
        try {
            WildBattleSettleResultVO result = wildBattleService.settleWildBattle(settleDTO);
            return Result.OK("结算成功", result);
        } catch (Exception e) {
            log.error("野外战斗结算失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 进入地图（验证等级要求）
     * @param enterDTO 进入地图请求
     * @return 进入结果
     */
    @Operation(summary = "进入地图")
    @PostMapping("/enter")
    public Result<EnterMapResultVO> enterMap(@Validated @RequestBody EnterMapDTO enterDTO) {
        try {
            EnterMapResultVO result = mapAreaService.enterMap(enterDTO);
            if (result.getSuccess()) {
                return Result.OK(result.getMessage(), result);
            } else {
                return Result.error(result.getMessage());
            }
        } catch (Exception e) {
            log.error("进入地图失败", e);
            return Result.error(e.getMessage());
        }
    }
}
