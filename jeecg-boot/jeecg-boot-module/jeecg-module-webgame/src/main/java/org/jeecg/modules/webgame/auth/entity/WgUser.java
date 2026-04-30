package org.jeecg.modules.webgame.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 游戏用户表
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 * @Version: V1.0
 */
@Data
@TableName("wg_user")
@EqualsAndHashCode(callSuper = false)
public class WgUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID*/
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    /**用户名*/
    @Excel(name = "用户名", width = 15)
    private String username;
    
    /**密码(加密存储)*/
    private String password;
    
    /**盐值*/
    private String salt;
    
    /**昵称*/
    @Excel(name = "昵称", width = 15)
    private String nickname;
    
    /**头像*/
    private String avatar;
    
    /**角色ID*/
    private String roleId;
    
    /**等级*/
    @Excel(name = "等级", width = 15)
    private Integer level;
    
    /**经验值*/
    private Long experience;
    
    /**金币*/
    private Long gold;
    
    /**钻石*/
    private Integer diamond;
    
    /**体力值*/
    private Integer stamina;
    
    /**最后登录时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    /**删除状态(0-正常,1-已删除)*/
    private Integer delFlag;
}
