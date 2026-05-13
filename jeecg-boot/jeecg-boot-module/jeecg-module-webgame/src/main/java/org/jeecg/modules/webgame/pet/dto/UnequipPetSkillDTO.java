package org.jeecg.modules.webgame.pet.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 战宠卸下技能请求DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Data
public class UnequipPetSkillDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**角色ID*/
    @NotBlank(message = "角色ID不能为空")
    private String characterId;

    /**战宠实例ID*/
    @NotBlank(message = "战宠ID不能为空")
    private String petId;

    /**槽位索引(0/1/2)*/
    @NotNull(message = "槽位索引不能为空")
    @Min(value = 0, message = "槽位索引范围为0-2")
    @Max(value = 2, message = "槽位索引范围为0-2")
    private Integer slotIndex;

    /**用户ID（由Controller填充）*/
    private String userId;
}
