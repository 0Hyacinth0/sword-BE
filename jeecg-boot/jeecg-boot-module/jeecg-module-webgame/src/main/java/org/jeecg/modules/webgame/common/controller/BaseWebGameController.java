package org.jeecg.modules.webgame.common.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.webgame.common.util.WebGameTokenUtil;

/**
 * @Description: WebGame Controller 基类
 * @Author: jeecg-boot
 * @Date: 2026-05-09
 */
@Slf4j
public abstract class BaseWebGameController {

    /**
     * 从请求中获取当前用户ID
     * @param request HTTP请求
     * @return 用户ID
     */
    protected String getCurrentUserId(HttpServletRequest request) {
        return WebGameTokenUtil.getUserIdFromRequest(request);
    }
}
