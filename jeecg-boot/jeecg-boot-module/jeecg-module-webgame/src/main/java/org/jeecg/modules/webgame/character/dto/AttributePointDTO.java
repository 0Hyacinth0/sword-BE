package org.jeecg.modules.webgame.character.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;
import java.io.Serializable;

/**
 * @Description: 属性加点DTO
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Data
public class AttributePointDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**力量加点*/
    @Min(value = 0, message = "力量加点不能小于0")
    private Integer strengthPoint = 0;
    
    /**智力加点*/
    @Min(value = 0, message = "智力加点不能小于0")
    private Integer intelligencePoint = 0;
    
    /**敏捷加点*/
    @Min(value = 0, message = "敏捷加点不能小于0")
    private Integer agilityPoint = 0;
}
