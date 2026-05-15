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
 * @Description: 好友请求表
 * @Author: jeecg-boot
 * @Date: 2026-05-15
 */
@Data
@TableName("wg_friend_requests")
@EqualsAndHashCode(callSuper = false)
public class WgFriendRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID(UUID)*/
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    /**发送者角色ID*/
    @TableField("from_character_id")
    private String fromCharacterId;
    
    /**接收者角色ID*/
    @TableField("to_character_id")
    private String toCharacterId;
    
    /**请求状态(pending/accepted/rejected)*/
    private String status;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
}
