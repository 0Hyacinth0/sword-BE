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
 * @Description: PVP对战记录表
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Data
@TableName("wg_pvp_match")
@EqualsAndHashCode(callSuper = false)
public class WgPvpMatch implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键ID*/
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    
    /**玩家1用户ID*/
    private String player1UserId;
    
    /**玩家1角色ID*/
    private String player1CharacterId;
    
    /**玩家2用户ID*/
    private String player2UserId;
    
    /**玩家2角色ID*/
    private String player2CharacterId;
    
    /**胜利者用户ID*/
    private String winnerUserId;
    
    /**对战结果(1-玩家1胜利,2-玩家2胜利,3-平局)*/
    private Integer result;
    
    /**战斗回合数*/
    private Integer rounds;
    
    /**战斗详情JSON*/
    private String battleDetail;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**结束时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
