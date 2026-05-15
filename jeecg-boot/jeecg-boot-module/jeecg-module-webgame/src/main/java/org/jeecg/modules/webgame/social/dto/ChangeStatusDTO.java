package org.jeecg.modules.webgame.social.dto;

import lombok.Data;

/**
 * @Description: 更改队伍状态 DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
public class ChangeStatusDTO {
    
    /**新的状态*/
    private String status;
}
