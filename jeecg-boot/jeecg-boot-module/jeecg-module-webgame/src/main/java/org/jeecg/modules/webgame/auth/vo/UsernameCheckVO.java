package org.jeecg.modules.webgame.auth.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * @Description: 用户名检测结果VO
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Data
public class UsernameCheckVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**用户名是否可用*/
    private Boolean available;
}
