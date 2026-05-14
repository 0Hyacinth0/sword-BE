package org.jeecg.modules.webgame.map.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 地图区域配置表
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Data
@TableName("wg_map_areas")
@EqualsAndHashCode(callSuper = false)
public class WgMapArea implements Serializable {
    private static final long serialVersionUID = 1L;

    /**区域ID*/
    @TableId
    private String id;
    
    /**区域名称*/
    private String name;
    
    /**emoji图标*/
    private String icon;
    
    /**等级范围最小值*/
    private Integer levelMin;
    
    /**等级范围最大值*/
    private Integer levelMax;
    
    /**区域描述*/
    private String description;
    
    /**解锁所需等级*/
    private Integer unlockLevel;
    
    /**排序顺序*/
    private Integer sortOrder;
    
    /**创建时间*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
}
