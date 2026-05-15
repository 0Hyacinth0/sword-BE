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
 * @Description: 聊天消息表
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
@TableName("wg_chat_messages")
@EqualsAndHashCode(callSuper = false)
public class WgChatMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID(UUID)*/
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    /**发送者角色ID*/
    @TableField("sender_id")
    private String senderId;
    
    /**频道类型(world/private)*/
    private String channel;
    
    /**私聊目标ID（仅私聊时有值）*/
    @TableField("target_id")
    private String targetId;
    
    /**消息内容*/
    private String content;
    
    /**发送时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
}
