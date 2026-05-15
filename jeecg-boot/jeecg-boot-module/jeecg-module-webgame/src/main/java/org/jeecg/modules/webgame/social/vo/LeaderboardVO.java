package org.jeecg.modules.webgame.social.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description: 排行榜响应 VO
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
public class LeaderboardVO {
    
    /**排行榜条目列表*/
    private List<LeaderboardEntryVO> entries;
    
    /**我的排名*/
    private Integer myRank;
    
    /**我的排序值*/
    private Object myValue;
}
