package org.jeecg.modules.webgame.character.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Description: 检查角色名称DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-19
 */
@Data
public class CheckCharacterNameDTO {

    /** 角色名称 */
    @NotBlank(message = "角色名称不能为空")
    private String characterName;
}
