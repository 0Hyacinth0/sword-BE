package org.jeecg.modules.webgame.pet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 战宠装备穿戴请求DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Data
public class EquipPetItemDTO implements Serializable {
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

    /**槽位类型(armor/accessory)*/
    @NotBlank(message = "槽位类型不能为空")
    private String slotType;

    /**用户ID（由Controller填充）*/
    private String userId;
}
