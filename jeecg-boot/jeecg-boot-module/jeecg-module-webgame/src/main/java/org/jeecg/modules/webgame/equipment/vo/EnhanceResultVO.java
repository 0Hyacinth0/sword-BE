package org.jeecg.modules.webgame.equipment.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * @Description: 装备强化结果VO
 * @Author: jeecg-boot
 * @Date: 2026-05-09
 */
@Data
public class EnhanceResultVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**是否成功*/
    private Boolean success;
    
    /**新的强化等级*/
    private Integer newLevel;
    
    /**提示信息*/
    private String message;
    
    /**装备名称*/
    private String equipmentName;
}
