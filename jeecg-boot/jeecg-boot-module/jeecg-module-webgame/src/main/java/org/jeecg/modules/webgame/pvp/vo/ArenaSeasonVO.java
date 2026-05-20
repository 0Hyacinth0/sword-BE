package org.jeecg.modules.webgame.pvp.vo;

import lombok.Data;

/**
 * @Description: 赛季信息VO
 * @Author: jeecg-boot
 * @Date: 2026-05-19
 */
@Data
public class ArenaSeasonVO {

    /** 赛季唯一标识 */
    private String seasonId;

    /** 赛季名称 */
    private String seasonName;

    /** 赛季编号 */
    private Integer seasonNumber;

    /** 赛季开始时间 */
    private String startDate;

    /** 赛季结束时间 */
    private String endDate;

    /** 是否为当前活跃赛季 */
    private Boolean isActive;
}
