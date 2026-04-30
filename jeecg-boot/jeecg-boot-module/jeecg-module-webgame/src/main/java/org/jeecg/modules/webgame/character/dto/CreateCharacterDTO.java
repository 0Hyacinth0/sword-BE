package org.jeecg.modules.webgame.character.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.io.Serializable;

/**
 * @Description: 创建角色DTO
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Data
public class CreateCharacterDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**角色名称*/
    @NotBlank(message = "角色名称不能为空")
    private String characterName;
    
    /**职业类型(1-战士,2-法师,3-猎人)*/
    @NotNull(message = "职业类型不能为空")
    private Integer profession;
}
