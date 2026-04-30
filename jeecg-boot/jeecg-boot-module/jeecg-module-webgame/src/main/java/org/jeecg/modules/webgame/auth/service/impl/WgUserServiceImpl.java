package org.jeecg.modules.webgame.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.webgame.auth.dto.LoginDTO;
import org.jeecg.modules.webgame.auth.dto.RegisterDTO;
import org.jeecg.modules.webgame.auth.entity.WgUser;
import org.jeecg.modules.webgame.auth.mapper.WgUserMapper;
import org.jeecg.modules.webgame.auth.service.IWgUserService;
import org.jeecg.modules.webgame.auth.vo.LoginVO;
import org.jeecg.modules.webgame.auth.vo.UsernameCheckVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * @Description: 游戏用户Service实现
 * @Author: jeecg-boot
 * @Date: 2026-04-30
 */
@Slf4j
@Service
public class WgUserServiceImpl extends ServiceImpl<WgUserMapper, WgUser> implements IWgUserService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 查询用户
        WgUser user = this.getUserByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new JeecgBootException("用户名不存在");
        }

        // 验证密码
        String encryptPassword = PasswordUtil.encrypt(loginDTO.getUsername(), loginDTO.getPassword(), user.getSalt());
        if (!encryptPassword.equals(user.getPassword())) {
            throw new JeecgBootException("密码错误");
        }

        // 检查删除状态
        if (user.getDelFlag() != null && user.getDelFlag() == 1) {
            throw new JeecgBootException("账号已被删除");
        }

        // 生成Token（使用用户名和密码）
        String token = JwtUtil.sign(user.getUsername(), user.getPassword(), CommonConstant.CLIENT_TYPE_PC);
        
        // 将token存入Redis，设置过期时间(24小时)
        redisUtil.set("webgame:token:" + token, user.getId(), 86400);

        // 更新最后登录时间
        user.setLastLoginTime(new Date());
        this.updateById(user);

        // 构建返回对象
        LoginVO loginVO = new LoginVO();
        loginVO.setId(user.getId());
        loginVO.setUsername(user.getUsername());
        loginVO.setToken(token);

        log.info("用户登录成功: {}", user.getUsername());
        return loginVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        WgUser existUser = this.getUserByUsername(registerDTO.getUsername());
        if (existUser != null) {
            throw new JeecgBootException("该用户名已被注册");
        }

        // 创建新用户
        WgUser user = new WgUser();
        user.setUsername(registerDTO.getUsername());
        
        // 生成盐值并加密密码
        String salt = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String encryptPassword = PasswordUtil.encrypt(registerDTO.getUsername(), registerDTO.getPassword(), salt);
        user.setPassword(encryptPassword);
        user.setSalt(salt);
        
        // 初始化用户属性
        user.setNickname(registerDTO.getUsername());
        user.setLevel(1);
        user.setExperience(0L);
        user.setGold(1000L);
        user.setDiamond(100);
        user.setStamina(100);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setDelFlag(0);

        this.save(user);
        log.info("用户注册成功: {}", user.getUsername());
    }

    @Override
    public void logout(String token) {
        if (token != null && !token.isEmpty()) {
            // 从Redis中删除token
            redisUtil.del("webgame:token:" + token);
            log.info("用户登出成功");
        }
    }

    @Override
    public WgUser getUserByUsername(String username) {
        LambdaQueryWrapper<WgUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WgUser::getUsername, username);
        queryWrapper.eq(WgUser::getDelFlag, 0);
        return this.getOne(queryWrapper);
    }

    @Override
    public UsernameCheckVO checkUsername(String username) {
        UsernameCheckVO result = new UsernameCheckVO();
        
        // 查询用户名是否已存在
        WgUser existUser = this.getUserByUsername(username);
        
        if (existUser != null) {
            // 用户名已存在
            result.setAvailable(false);
            log.info("用户名已存在: {}", username);
        } else {
            // 用户名可用
            result.setAvailable(true);
            log.info("用户名可用: {}", username);
        }
        
        return result;
    }
}
