package org.jeecg.modules.webgame.item.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

/**
 * @Description: 使用物品响应VO
 * @Author: jeecg-boot
 * @Date: 2026-05-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UseItemResultVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**效果描述列表*/
    private List<String> effects;
    
    /**汇总提示消息*/
    private String message;
}
