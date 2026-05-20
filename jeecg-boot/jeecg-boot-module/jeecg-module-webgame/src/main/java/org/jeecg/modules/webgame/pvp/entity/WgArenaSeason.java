package org.jeecg.modules.webgame.pvp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 竞技场赛季表
 * @Author: jeecg-boot
 * @Date: 2026-05-19
 */
@Data
@TableName("wg_arena_season")
@EqualsAndHashCode(callSuper = false)
public class WgArenaSeason implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID*/
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    /**赛季唯一标识*/
    private String seasonId;
    
    /**赛季名称*/
    private String seasonName;
    
    /**赛季编号*/
    private Integer seasonNumber;
    
    /**赛季开始时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    
    /**赛季结束时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    
    /**是否为当前活跃赛季*/
    private Boolean isActive;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
