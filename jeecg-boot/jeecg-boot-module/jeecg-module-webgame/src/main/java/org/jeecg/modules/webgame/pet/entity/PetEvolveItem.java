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
 * @Description: 进化材料配置表
 * @Author: jeecg-boot
 * @Date: 2026-05-12
 */
@Data
@TableName("wg_pet_evolve_items")
@EqualsAndHashCode(callSuper = false)
public class PetEvolveItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /**配置ID*/
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    /**战宠类型ID(需要进化的战宠)*/
    private Integer petTypeId;
    
    /**所需物品ID(关联wg_item_template)*/
    private String itemId;
    
    /**所需数量*/
    private Integer quantity;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    /**更新时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
