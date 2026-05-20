package org.jeecg.modules.webgame.map.vo;

import lombok.Data;

/**
 * @Description: 进入地图结果VO
 * @Author: jeecg-boot
 * @Date: 2026-05-19
 */
@Data
public class EnterMapResultVO {

    /** 是否成功 */
    private Boolean success;

    /** 提示信息 */
    private String message;

    /** 区域名称 */
    private String areaName;

    /** 区域等级要求 */
    private Integer levelRequirement;

    /** 角色当前等级 */
    private Integer characterLevel;
}
