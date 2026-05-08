package org.jeecg.modules.webgame.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @Description: 缓存管理控制器（开发环境使用）
 * @Author: jeecg-boot
 * @Date: 2026-05-08
 */
@Slf4j
@Tag(name = "缓存管理-开发工具")
@RestController
@RequestMapping("/webgame/admin/cache")
public class CacheAdminController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 清理 Shiro 缓存
     * 用于解决 Redis 序列化异常问题
     */
    @Operation(summary = "清理Shiro缓存", description = "解决Redis序列化异常，仅开发环境使用")
    @PostMapping("/clear-shiro")
    public Result<String> clearShiroCache() {
        try {
            // 查找所有 shiro 相关的 key
            Set<String> keys = redisTemplate.keys("shiro:*");
            
            if (keys != null && !keys.isEmpty()) {
                Long deletedCount = redisTemplate.delete(keys);
                log.info("✅ 清理 Shiro 缓存成功，删除 {} 个key", deletedCount);
                return Result.OK("清理成功，删除 " + deletedCount + " 个缓存项");
            } else {
                log.info("ℹ️ 没有找到 Shiro 缓存");
                return Result.OK("没有找到 Shiro 缓存");
            }
        } catch (Exception e) {
            log.error("❌ 清理 Shiro 缓存失败", e);
            return Result.error("清理失败：" + e.getMessage());
        }
    }

    /**
     * 查看所有 Shiro 缓存 Key
     */
    @Operation(summary = "查看Shiro缓存Key", description = "仅开发环境使用")
    @GetMapping("/list-shiro-keys")
    public Result<Set<String>> listShiroKeys() {
        try {
            Set<String> keys = redisTemplate.keys("shiro:*");
            return Result.OK(keys);
        } catch (Exception e) {
            log.error("查询 Shiro 缓存Key失败", e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }
}
