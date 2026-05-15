package org.jeecg.modules.webgame.social.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 私聊会话表
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
@TableName("wg_chat_conversations")
@EqualsAndHashCode(callSuper = false)
public class WgChatConversation implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID(UUID)*/
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    /**较小角色ID*/
    @TableField("user1_id")
    private String user1Id;
    
    /**较大角色ID*/
    @TableField("user2_id")
    private String user2Id;
    
    /**最后一条消息内容*/
    private String lastMessage;
    
    /**最后消息时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
}
