package org.jeecg.modules.webgame.social.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.webgame.social.service.ILeaderboardService;
import org.jeecg.modules.webgame.social.vo.LeaderboardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @Description: 排行榜系统 Controller
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Slf4j
@RestController
@RequestMapping("/webgame/leaderboard")
public class LeaderboardController {
    
    @Autowired
    private ILeaderboardService leaderboardService;
    
    /**
     * 获取排行榜
     * @param category 分类（level/power/arena）
     * @param scope 范围（all/friends）
     */
    @GetMapping("/{category}")
    public Result<LeaderboardVO> getLeaderboard(
            @PathVariable String category,
            @RequestParam(required = false, defaultValue = "all") String scope,
            HttpServletRequest request) {
        
        String characterId = (String) request.getAttribute("characterId");
        log.info("获取排行榜, category: {}, scope: {}, characterId: {}", category, scope, characterId);
        
        LeaderboardVO leaderboard = leaderboardService.getLeaderboard(category, scope, characterId);
        return Result.OK("获取成功", leaderboard);
    }
}
