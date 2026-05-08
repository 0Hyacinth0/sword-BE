package org.jeecg.modules.webgame.character.dto;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description: 增加经验DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-08
 */
@Data
public class AddExperienceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**角色ID*/
    @NotNull(message = "角色ID不能为空")
    private String characterId;
    
    /**要增加的经验值*/
    @Min(value = 0, message = "经验值不能为负数")
    @NotNull(message = "经验值不能为空")
    private Integer expToAdd;
}
