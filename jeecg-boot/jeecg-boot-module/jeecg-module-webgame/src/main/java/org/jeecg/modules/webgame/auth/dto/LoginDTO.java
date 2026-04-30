package org.jeecg.modules.webgame.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.io.Serializable;

/**
 * @Description: 登录请求DTO
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Data
public class LoginDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**用户名*/
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名需要3-20个字符")
    private String username;
    
    /**密码*/
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码需要6-20个字符")
    private String password;
}
