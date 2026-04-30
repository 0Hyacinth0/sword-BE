package org.jeecg.modules.webgame.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.webgame.auth.dto.LoginDTO;
import org.jeecg.modules.webgame.auth.dto.RegisterDTO;
import org.jeecg.modules.webgame.auth.entity.WgUser;
import org.jeecg.modules.webgame.auth.vo.LoginVO;
import org.jeecg.modules.webgame.auth.vo.UsernameCheckVO;

/**
 * @Description: 游戏用户Service
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
public interface IWgUserService extends IService<WgUser> {

    /**
     * 用户登录
     * @param loginDTO 登录参数
     * @return 登录信息(包含token)
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 用户注册
     * @param registerDTO 注册参数
     */
    void register(RegisterDTO registerDTO);

    /**
     * 用户登出
     * @param token JWT token
     */
    void logout(String token);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    WgUser getUserByUsername(String username);

    /**
     * 检测用户名是否可用
     * @param username 用户名
     * @return 检测结果
     */
    UsernameCheckVO checkUsername(String username);
}
