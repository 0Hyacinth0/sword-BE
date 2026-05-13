package org.jeecg.modules.webgame.pet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 重命名战宠请求DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Data
public class RenamePetDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**角色ID*/
    @NotBlank(message = "角色ID不能为空")
    private String characterId;

    /**战宠实例ID*/
    @NotBlank(message = "战宠ID不能为空")
    private String petId;

    /**新昵称*/
    @NotBlank(message = "昵称不能为空")
    @Size(min = 1, max = 12, message = "昵称长度为1-12个字符")
    private String nickname;

    /**用户ID（由Controller填充）*/
    private String userId;
}
