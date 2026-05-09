package org.jeecg.modules.webgame.equipment.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description: 卸下装备DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-09
 */
@Data
public class UnequipItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**角色ID*/
    @NotBlank(message = "角色ID不能为空")
    private String characterId;
    
    /**槽位类型(weapon/helmet/chest/legs/accessory1/accessory2)*/
    @NotBlank(message = "槽位类型不能为空")
    private String slotType;
    
    /**用户ID（从Token获取）*/
    @NotBlank(message = "用户ID不能为空")
    private String userId;
}
