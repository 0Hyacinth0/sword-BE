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
 * @Description: 玩家竞技场数据表
 * @Author: jeecg-boot
 * @Date: 2026-05-19
 */
@Data
@TableName("wg_arena_player")
@EqualsAndHashCode(callSuper = false)
public class WgArenaPlayer implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID*/
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    /**角色ID*/
    private String characterId;
    
    /**赛季ID*/
    private String seasonId;
    
    /**段位(bronze/silver/gold/platinum/diamond/master)*/
    private String tier;
    
    /**小级(I/II/III)*/
    private String subTier;
    
    /**当前积分*/
    private Integer score;
    
    /**胜利场次*/
    private Integer wins;
    
    /**失败场次*/
    private Integer losses;
    
    /**平局场次*/
    private Integer draws;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
