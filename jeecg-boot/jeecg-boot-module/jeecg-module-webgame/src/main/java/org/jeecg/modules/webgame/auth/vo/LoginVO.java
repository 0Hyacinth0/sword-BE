package org.jeecg.modules.webgame.auth.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * @Description: 登录响应VO
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Data
public class LoginVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**用户ID*/
    private String id;
    
    /**用户名*/
    private String username;
    
    /**JWT Token*/
    private String token;
}
