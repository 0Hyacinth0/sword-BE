# Redis 缓存清理指南

## 问题现象

```
org.springframework.data.redis.serializer.SerializationException: 
Could not read JSON: Unexpected character ('' (code 172))
```

## 原因分析

Redis 中存储的 Shiro 权限缓存数据损坏或格式不正确，导致 Jackson 反序列化失败。

## 解决方法

### 方法一：使用 Redis CLI 清理（推荐）

#### 1. 连接到 Redis

```bash
# 本地 Redis
redis-cli

# 远程 Redis（需要密码）
redis-cli -h 127.0.0.1 -p 6379 -a your_password
```

#### 2. 查看 Shiro 相关的 Key

```bash
# 查看所有 shiro 相关的 key
KEYS *shiro*

# 或者查看所有 key（谨慎使用）
KEYS *
```

#### 3. 删除 Shiro 缓存

```bash
# 删除所有 shiro 相关的 key
DEL shiro:cache:authorization:*
DEL shiro:cache:authentication:*

# 或者删除所有以 shiro 开头的 key
KEYS shiro:* | xargs redis-cli DEL

# 如果想彻底清理，可以删除所有 key（⚠️ 危险操作，生产环境慎用）
FLUSHDB  # 清空当前数据库
# 或
FLUSHALL # 清空所有数据库
```

#### 4. 验证清理结果

```bash
# 确认 shiro 相关的 key 已被删除
KEYS *shiro*

# 应该返回空列表
(empty list or set)
```

### 方法二：使用 Redis Desktop Manager / Another Redis Desktop Manager

1. 打开 Redis 可视化工具
2. 连接到 Redis 服务器
3. 搜索 key：`*shiro*`
4. 选中所有匹配的 key
5. 右键 → Delete（删除）

### 方法三：在应用中添加清理接口（开发环境）

创建一个临时的 Controller 来清理缓存：

```java
package org.jeecg.modules.webgame.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @Description: 缓存管理控制器（仅开发环境使用）
 */
@Slf4j
@Tag(name = "缓存管理")
@RestController
@RequestMapping("/webgame/admin/cache")
public class CacheAdminController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 清理 Shiro 缓存
     */
    @Operation(summary = "清理Shiro缓存")
    @PostMapping("/clear-shiro")
    public Result<Void> clearShiroCache() {
        try {
            // 查找所有 shiro 相关的 key
            Set<String> keys = redisTemplate.keys("shiro:*");
            
            if (keys != null && !keys.isEmpty()) {
                Long deletedCount = redisTemplate.delete(keys);
                log.info("清理 Shiro 缓存成功，删除 {} 个key", deletedCount);
                return Result.OK("清理成功，删除 " + deletedCount + " 个缓存项");
            } else {
                log.info("没有找到 Shiro 缓存");
                return Result.OK("没有找到 Shiro 缓存");
            }
        } catch (Exception e) {
            log.error("清理 Shiro 缓存失败", e);
            return Result.error("清理失败：" + e.getMessage());
        }
    }

    /**
     * 清理所有缓存（⚠️ 危险操作）
     */
    @Operation(summary = "清理所有缓存")
    @PostMapping("/clear-all")
    public Result<Void> clearAllCache() {
        try {
            redisTemplate.getConnectionFactory().getConnection().flushDb();
            log.warn("已清空所有Redis缓存");
            return Result.OK("已清空所有缓存");
        } catch (Exception e) {
            log.error("清空缓存失败", e);
            return Result.error("清空失败：" + e.getMessage());
        }
    }
}
```

**⚠️ 注意：** 这个接口只应在开发环境使用，生产环境需要添加权限控制！

### 方法四：重启 Redis 服务

如果以上方法都不方便，可以直接重启 Redis：

```bash
# Linux/Mac
sudo systemctl restart redis
# 或
sudo service redis-server restart

# Windows（如果使用 Windows 版 Redis）
net stop Redis
net start Redis

# Docker
docker restart redis-container-name
```

**注意：** 重启 Redis 会清空所有未持久化的数据！

## 预防措施

### 1. 检查 Redis 序列化配置

确保 `application.yml` 中的 Redis 配置正确：

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: your_password
      database: 0
      lettuce:
        pool:
          max-active: 8
          max-wait: -1ms
          max-idle: 8
          min-idle: 0
      # 关键：确保使用正确的序列化器
      serializer:
        type: json  # 或 string
```

### 2. 检查 Shiro Redis 配置

在 `ShiroConfig.java` 中，确认 RedisManager 配置正确：

```java
@Bean
public IRedisManager redisManager() {
    RedisManager redisManager = new RedisManager();
    redisManager.setHost(lettuceConnectionFactory.getHostName() + ":" + lettuceConnectionFactory.getPort());
    redisManager.setDatabase(lettuceConnectionFactory.getDatabase());
    redisManager.setTimeout(0);
    if (!StringUtils.isEmpty(lettuceConnectionFactory.getPassword())) {
        redisManager.setPassword(lettuceConnectionFactory.getPassword());
    }
    return redisManager;
}
```

### 3. 定期清理过期缓存

可以在应用中添加定时任务，定期清理过期的 Shiro 缓存：

```java
@Component
@Slf4j
public class ShiroCacheCleaner {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 每天凌晨3点清理过期的 Shiro 缓存
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanExpiredCache() {
        try {
            Set<String> keys = redisTemplate.keys("shiro:cache:*");
            if (keys != null && !keys.isEmpty()) {
                // 这里可以添加更精细的清理逻辑
                // 比如只清理超过一定时间的缓存
                log.info("定时清理 Shiro 缓存，找到 {} 个key", keys.size());
            }
        } catch (Exception e) {
            log.error("定时清理 Shiro 缓存失败", e);
        }
    }
}
```

## 验证清理结果

清理缓存后，重新测试接口：

```bash
# 测试获取角色详情
curl -X GET "http://localhost:8080/webgame/character/info/{characterId}" \
  -H "X-Access-Token: {your_jwt_token}"

# 应该不再报 SerializationException 错误
```

## 常见问题

### Q1: 清理缓存后会影响其他功能吗？

**答：** 
- Shiro 缓存主要存储用户的权限信息和会话信息
- 清理后，用户需要重新登录或重新获取权限
- 不会影响业务数据
- 对于 webgame 模块，由于配置了 `anon`，影响很小

### Q2: 为什么会出现缓存损坏？

**可能原因：**
1. Redis 版本升级导致序列化格式不兼容
2. 应用升级时，对象结构发生变化
3. Redis 内存不足导致数据损坏
4. 网络中断导致写入不完整

### Q3: 如何避免这个问题再次发生？

**建议：**
1. 定期监控 Redis 内存使用情况
2. 设置合理的缓存过期时间
3. 应用升级时，先清理旧缓存
4. 使用 Redis 持久化（RDB/AOF）防止数据丢失
5. 在开发环境，每次重启应用时自动清理缓存

### Q4: 生产环境能直接 FLUSHDB 吗？

**答：** ⚠️ **绝对不建议！**

生产环境应该：
1. 只删除特定的 shiro 相关 key
2. 在低峰期操作
3. 提前通知相关人员
4. 做好备份
5. 逐步清理，观察系统反应

## 快速操作脚本

### Linux/Mac 一键清理脚本

```bash
#!/bin/bash
# clear_shiro_cache.sh

REDIS_HOST="127.0.0.1"
REDIS_PORT="6379"
REDIS_PASSWORD=""  # 如果有密码，填写在这里

echo "开始清理 Shiro 缓存..."

if [ -z "$REDIS_PASSWORD" ]; then
    # 无密码
    redis-cli -h $REDIS_HOST -p $REDIS_PORT KEYS "shiro:*" | xargs redis-cli -h $REDIS_HOST -p $REDIS_PORT DEL
else
    # 有密码
    redis-cli -h $REDIS_HOST -p $REDIS_PORT -a $REDIS_PASSWORD KEYS "shiro:*" | xargs redis-cli -h $REDIS_HOST -p $REDIS_PORT -a $REDIS_PASSWORD DEL
fi

echo "清理完成！"
```

使用方法：
```bash
chmod +x clear_shiro_cache.sh
./clear_shiro_cache.sh
```

### Windows PowerShell 脚本

```powershell
# clear_shiro_cache.ps1

$redisHost = "127.0.0.1"
$redisPort = "6379"

Write-Host "开始清理 Shiro 缓存..." -ForegroundColor Green

# 使用 redis-cli 清理
& redis-cli -h $redisHost -p $redisPort KEYS "shiro:*" | ForEach-Object {
    & redis-cli -h $redisHost -p $redisPort DEL $_
}

Write-Host "清理完成！" -ForegroundColor Green
```

---

*文档版本：2026-05-08*
*适用场景：JeecgBoot + Shiro + Redis 缓存问题*
