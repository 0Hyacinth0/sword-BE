package org.jeecg.modules.webgame.pet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 战宠类型配置表
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Data
@TableName("wg_pet_types")
@EqualsAndHashCode(callSuper = false)
public class PetType implements Serializable {
    private static final long serialVersionUID = 1L;

    /**战宠类型ID*/
    @TableId(type = IdType.AUTO)
    private Integer petTypeId;
    
    /**战宠名称*/
    private String name;
    
    /**元素类型(1-火,2-水,3-风,4-地,5-光,6-暗)*/
    private Integer element;
    
    /**稀有度(1-N,2-R,3-SR,4-SSR)*/
    private Integer rarity;
    
    /**基础生命值*/
    private Integer baseHp;
    
    /**基础攻击力*/
    private Integer baseAttack;
    
    /**基础防御力*/
    private Integer baseDefense;
    
    /**基础速度*/
    private Integer baseSpeed;
    
    /**生命成长值(每级增加)*/
    private Integer hpGrowth;
    
    /**攻击成长值(每级增加)*/
    private Integer attackGrowth;
    
    /**防御成长值(每级增加)*/
    private Integer defenseGrowth;
    
    /**速度成长值(每级增加)*/
    private Integer speedGrowth;
    
    /**战宠描述*/
    private String description;
    
    /**进化目标类型ID,null为最终形态*/
    private Integer evolveTo;
    
    /**进化所需等级*/
    private Integer evolveLevel;
    
    /**图标URL*/
    private String iconUrl;
    
    /**剪影URL(未收集时显示)*/
    private String silhouetteUrl;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
