package org.jeecg.modules.webgame.map.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Description: 进入地图请求DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-19
 */
@Data
public class EnterMapDTO {

    /** 角色ID */
    @NotBlank(message = "角色ID不能为空")
    private String characterId;

    /** 区域ID */
    @NotBlank(message = "区域ID不能为空")
    private String areaId;
}
