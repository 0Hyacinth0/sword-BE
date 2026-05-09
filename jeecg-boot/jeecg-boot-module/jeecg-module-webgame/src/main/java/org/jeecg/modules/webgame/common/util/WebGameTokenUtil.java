package org.jeecg.modules.webgame.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.jeecg.common.system.util.JwtUtil;

/**
 * @Description: WebGame Token 工具类
 * @Author: jeecg-boot
 * @Date: 2026-05-09
 */
public class WebGameTokenUtil {

    /**
     * 从请求中获取用户ID
     * @param request HTTP请求
     * @return 用户ID
     */
    public static String getUserIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("X-Access-Token");
        return getUserIdFromToken(token);
    }

    /**
     * 从 Token 中解析用户ID
     * @param token JWT Token
     * @return 用户ID
     */
    public static String getUserIdFromToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("未登录，请先登录");
        }
        
        try {
            // 从 Token 中解析用户名
            String username = JwtUtil.getUsername(token);
            
            // TODO: 实际项目中应该调用系统用户服务，通过用户名查询用户ID
            // 这里简化处理，直接使用用户名作为 userId
            // 真实场景：LoginUser loginUser = commonApi.getUserByName(username);
            //         return loginUser.getId();
            
            return username;  // 简化处理
        } catch (Exception e) {
            throw new RuntimeException("Token 无效，请重新登录");
        }
    }
}
