package org.jeecg.modules.webgame.item.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.io.Serializable;

/**
 * @Description: 丢弃物品DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-08
 */
@Data
public class DiscardItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**角色ID*/
    @NotNull(message = "角色ID不能为空")
    private String characterId;
    
    /**背包记录ID*/
    @NotNull(message = "背包记录ID不能为空")
    private String inventoryId;
    
    /**丢弃数量*/
    @Min(value = 1, message = "丢弃数量必须大于0")
    @NotNull(message = "丢弃数量不能为空")
    private Integer quantity;
}
