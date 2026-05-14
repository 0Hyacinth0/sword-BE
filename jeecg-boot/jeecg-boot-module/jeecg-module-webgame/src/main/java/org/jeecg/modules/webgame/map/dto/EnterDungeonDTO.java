package org.jeecg.modules.webgame.map.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description: 进入副本请求DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Data
public class EnterDungeonDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**角色ID*/
    @NotBlank(message = "角色ID不能为空")
    private String characterId;
    
    /**副本ID*/
    @NotBlank(message = "副本ID不能为空")
    private String dungeonId;
}
