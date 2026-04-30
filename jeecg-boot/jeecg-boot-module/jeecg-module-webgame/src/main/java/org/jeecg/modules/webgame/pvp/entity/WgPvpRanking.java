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
 * @Description: PVP积分排行榜表
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Data
@TableName("wg_pvp_ranking")
@EqualsAndHashCode(callSuper = false)
public class WgPvpRanking implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID*/
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    /**用户ID*/
    private String userId;
    
    /**角色ID*/
    private String characterId;
    
    /**当前积分*/
    private Integer rating;
    
    /**历史最高积分*/
    private Integer maxRating;
    
    /**胜利场次*/
    private Integer wins;
    
    /**失败场次*/
    private Integer losses;
    
    /**平局场次*/
    private Integer draws;
    
    /**总场次*/
    private Integer totalMatches;
    
    /**当前排名*/
    private Integer currentRank;
    
    /**赛季*/
    private String season;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
