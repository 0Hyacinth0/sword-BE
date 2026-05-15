package org.jeecg.modules.webgame.social.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @Description: 发送消息 DTO
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
public class SendMessageDTO {
    
    /**发送者角色ID（可选，不传则自动从Token获取）*/
    private String senderId;
    
    /**频道类型(world/private)*/
    @NotBlank(message = "频道类型不能为空")
    private String channel;
    
    /**消息内容*/
    @NotBlank(message = "消息内容不能为空")
    @Size(max = 500, message = "消息内容不能超过500字符")
    private String content;
    
    /**私聊目标ID（仅私聊时需要）*/
    private String targetId;
}
