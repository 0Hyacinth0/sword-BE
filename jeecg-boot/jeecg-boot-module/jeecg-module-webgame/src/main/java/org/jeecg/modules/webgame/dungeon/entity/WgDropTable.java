package org.jeecg.modules.webgame.dungeon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 副本掉落表
 * @Author: jeecg-boot
 * @Date: 2026-05-14
 */
@Data
@TableName("wg_drop_tables")
@EqualsAndHashCode(callSuper = false)
public class WgDropTable implements Serializable {
    private static final long serialVersionUID = 1L;

    /**副本ID*/
    @TableId
    private String dungeonId;
    
    /**掉落率倍率（普通=1.0, 精英=1.5）*/
    private BigDecimal dropRateMultiplier;
}
