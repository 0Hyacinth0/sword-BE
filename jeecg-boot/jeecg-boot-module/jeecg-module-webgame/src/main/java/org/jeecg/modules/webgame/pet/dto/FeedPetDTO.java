package org.jeecg.modules.webgame.pet.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 喂食战宠请求DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Data
public class FeedPetDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**角色ID*/
    @NotBlank(message = "角色ID不能为空")
    private String characterId;

    /**战宠实例ID*/
    @NotBlank(message = "战宠ID不能为空")
    private String petId;

    /**背包物品ID*/
    @NotBlank(message = "背包物品ID不能为空")
    private String inventoryId;

    /**使用数量*/
    @NotNull(message = "使用数量不能为空")
    @Min(value = 1, message = "使用数量至少为1")
    private Integer quantity;

    /**用户ID（由Controller填充）*/
    private String userId;
}
