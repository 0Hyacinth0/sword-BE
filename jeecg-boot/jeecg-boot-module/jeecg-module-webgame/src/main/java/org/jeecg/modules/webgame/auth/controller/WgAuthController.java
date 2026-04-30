package org.jeecg.modules.webgame.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.webgame.auth.dto.LoginDTO;
import org.jeecg.modules.webgame.auth.dto.RegisterDTO;
import org.jeecg.modules.webgame.auth.service.IWgUserService;
import org.jeecg.modules.webgame.auth.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 游戏认证控制器
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Slf4j
@Tag(name = "游戏认证接口")
@RestController
@RequestMapping("/webgame/auth")
public class WgAuthController {

    @Autowired
    private IWgUserService wgUserService;

    /**
     * 用户登录
     * @param loginDTO 登录参数
     * @return 登录信息(包含token)
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Validated @RequestBody LoginDTO loginDTO) {
        try {
            LoginVO loginVO = wgUserService.login(loginDTO);
            return Result.OK("登录成功", loginVO);
        } catch (Exception e) {
            log.error("登录失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户注册
     * @param registerDTO 注册参数
     * @return 注册结果
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<Void> register(@Validated @RequestBody RegisterDTO registerDTO) {
        try {
            wgUserService.register(registerDTO);
            return Result.OK("注册成功，请登录");
        } catch (Exception e) {
            log.error("注册失败", e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户登出
     * @param request HTTP请求
     * @return 登出结果
     */
    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            wgUserService.logout(token);
            return Result.OK("已退出登录");
        } catch (Exception e) {
            log.error("登出失败", e);
            return Result.error(e.getMessage());
        }
    }
}
