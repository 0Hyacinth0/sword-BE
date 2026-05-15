package org.jeecg.modules.webgame.social.service;

import org.jeecg.modules.webgame.social.vo.LeaderboardVO;

/**
 * @Description: 排行榜系统 Service
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
public interface ILeaderboardService {
    
    /**
     * 获取排行榜
     * @param category 分类（level/power/arena）
     * @param scope 范围（all/friends）
     * @param characterId 当前角色ID（用于计算我的排名和好友排行）
     * @return 排行榜数据
     */
    LeaderboardVO getLeaderboard(String category, String scope, String characterId);
}
