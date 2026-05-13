package org.jeecg.modules.webgame.pet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 进化战宠请求DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Data
public class EvolvePetDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**角色ID*/
    @NotBlank(message = "角色ID不能为空")
    private String characterId;

    /**战宠实例ID*/
    @NotBlank(message = "战宠ID不能为空")
    private String petId;

    /**用户ID（由Controller填充）*/
    private String userId;
}
