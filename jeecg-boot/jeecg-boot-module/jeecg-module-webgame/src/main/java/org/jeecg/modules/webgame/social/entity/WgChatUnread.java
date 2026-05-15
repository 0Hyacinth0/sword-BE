package org.jeecg.modules.webgame.social.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description: 未读消息计数表
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
@TableName("wg_chat_unread")
@EqualsAndHashCode(callSuper = false)
public class WgChatUnread implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID(UUID)*/
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    /**会话ID*/
    @TableField("conversation_id")
    private String conversationId;
    
    /**角色ID*/
    @TableField("character_id")
    private String characterId;
    
    /**未读消息数量*/
    private Integer unreadCount;
}
