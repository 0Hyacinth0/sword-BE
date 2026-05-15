package org.jeecg.modules.webgame.social.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * @Description: 处理好友请求 DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
public class HandleFriendRequestDTO {
    
    /**请求ID*/
    @NotBlank(message = "请求ID不能为空")
    private String requestId;
}
