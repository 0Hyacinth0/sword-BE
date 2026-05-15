package org.jeecg.modules.webgame.social.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * @Description: 发送好友请求 DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
public class FriendRequestDTO {
    
    /**接收者角色ID*/
    @NotBlank(message = "目标角色ID不能为空")
    private String toCharacterId;
}
