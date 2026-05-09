# Shiro认证问题解决方案

## 问题分析

### 错误现象
```
ERROR ShiroRealm:112 - 校验 check token 失败——用户不存在!
org.apache.shiro.authc.AuthenticationException: 用户不存在!
    at ShiroRealm.checkUserTokenIsEffect(ShiroRealm.java:136)
```

### 根本原因

1. **前端发送正确**：Token 通过 `X-Access-Token` 请求头正常传递
2. **JWT解析正常**：能从中取出 username: "123456"
3. **Shiro校验失败**：ShiroRealm 在 `checkUserTokenIsEffect` 方法中查找用户时，查询的是 `sys_user` 表
4. **用户表不匹配**：用户 "123456" 在 webgame 的 `wg_user` 表中存在，但在 jeecg-boot 的 `sys_user` 表中不存在

### 为什么会调用ShiroRealm？

虽然 `ShiroConfig.java` 第112行已经配置了：
```java
filterChainDefinitionMap.put("/webgame/character/**", "anon");  // WebGame 角色模块
```

但这个配置**需要重启应用才能生效**。如果应用没有重启，Shiro 仍然会使用旧的配置，对所有 `/webgame/**` 路径进行 JWT 认证，从而触发 ShiroRealm 的用户校验。

---

## 解决方案

### 方案一：重启应用（推荐）✅

**步骤：**
1. 停止当前运行的 Spring Boot 应用
2. 重新启动应用
3. Shiro 配置会自动加载，`/webgame/character/**` 路径将被设置为匿名访问

**验证方法：**
```bash
# 查看启动日志，确认 Shiro 配置已加载
# 应该能看到类似这样的日志：
# ===============(1)创建缓存管理器RedisCacheManager
# ===============(2)创建RedisManager,连接Redis..
```

**优点：**
- 简单直接
- 不需要修改代码
- ShiroConfig 已经配置好了免登录规则

**缺点：**
- 需要重启应用，可能影响其他正在使用的功能

---

### 方案二：在 sys_user 表中同步创建用户

如果希望保留 Shiro 认证（即不使用 anon 放行），可以在 `sys_user` 表中创建对应的用户。

**SQL脚本：**
```sql
-- 在 sys_user 表中创建 webgame 用户
INSERT INTO `sys_user` (
    `id`, 
    `username`, 
    `realname`, 
    `password`, 
    `salt`, 
    `status`, 
    `del_flag`, 
    `create_time`, 
    `update_time`
) VALUES (
    '123456',  -- 与 wg_user 表中的用户ID一致
    '123456',  -- 用户名
    'WebGame用户',  -- 真实姓名
    '$2a$10$xxx',  -- 密码（需要使用 BCrypt 加密）
    'xxx',     -- 盐值
    1,         -- 状态：正常
    0,         -- 删除标志：未删除
    NOW(),     -- 创建时间
    NOW()      -- 更新时间
);
```

**注意：**
- 这种方式会让 webgame 用户也能登录 jeecg-boot 后台管理系统
- 需要确保两个系统的用户ID保持一致
- 密码加密方式需要与 jeecg-boot 一致

**优点：**
- 保留完整的 Shiro 认证机制
- 可以统一管理用户权限

**缺点：**
- 需要在两个表中维护用户数据
- 可能导致安全问题（webgame 用户可以访问后台）

---

### 方案三：自定义 ShiroRealm 支持多用户表

修改 `ShiroRealm.java`，让它能够同时查询 `sys_user` 和 `wg_user` 表。

**修改位置：** `ShiroRealm.checkUserTokenIsEffect` 方法

**伪代码示例：**
```java
private void checkUserTokenIsEffect(String username) throws AuthenticationException {
    // 1. 先查询 sys_user 表
    SysUser sysUser = sysUserService.getUserByName(username);
    
    if (sysUser != null) {
        // sys_user 表中存在，使用原有逻辑
        // ... 原有校验逻辑
        return;
    }
    
    // 2. sys_user 表中不存在，尝试查询 wg_user 表
    WgUser wgUser = wgUserService.getUserByUsername(username);
    
    if (wgUser != null) {
        // wg_user 表中存在，认为是合法的 webgame 用户
        log.info("WebGame用户认证成功: {}", username);
        return;
    }
    
    // 3. 两个表都不存在，抛出异常
    throw new AuthenticationException("用户不存在!");
}
```

**优点：**
- 支持多用户表
- 保持统一的认证入口

**缺点：**
- 需要修改核心认证代码
- 增加系统复杂度
- 升级 jeecg-boot 时可能需要重新适配

---

### 方案四：业务层自行校验 Token（备选）

如果 Shiro 配置始终无法生效，可以在 Controller 中完全绕过 Shiro，自行实现 Token 校验。

**修改 WgCharacterController：**
```java
@GetMapping("/info/{characterId}")
public Result<CharacterVO> getCharacterInfo(@PathVariable("characterId") String characterId) {
    try {
        // 不依赖 Shiro，直接从 Token 中解析 userId
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
            .currentRequestAttributes()).getRequest();
        String token = request.getHeader("X-Access-Token");
        
        // 使用 JwtUtil 解析 Token（不涉及 Shiro）
        String userId = JwtUtil.getUsername(token);
        
        if (userId == null) {
            return Result.error("Token无效");
        }
        
        // 验证角色是否属于该用户
        CharacterVO characterVO = wgCharacterService.getCharacterInfo(characterId);
        
        // TODO: 添加权限校验，确保 userId 有权限访问该角色
        
        return Result.OK(characterVO);
    } catch (Exception e) {
        log.error("获取角色信息失败", e);
        return Result.error(e.getMessage());
    }
}
```

**优点：**
- 完全不依赖 Shiro
- 灵活性高

**缺点：**
- 失去了 Shiro 的统一认证管理
- 每个接口都需要手动校验
- 不符合 JeecgBoot 框架的设计规范

---

## 推荐方案

### ✅ 首选：方案一（重启应用）

**理由：**
1. ShiroConfig 已经正确配置了 `/webgame/character/**` 为 `anon`
2. 这是最符合 JeecgBoot 框架设计的方案
3. 无需修改代码，只需重启即可生效
4. WebGame 模块的其他接口（auth/login、auth/register 等）也使用了相同的配置方式

**操作步骤：**
```bash
# 1. 停止应用
# 如果使用 IDE，点击停止按钮
# 如果使用命令行，按 Ctrl+C

# 2. 重新启动应用
# 如果使用 IDE，点击运行按钮
# 如果使用命令行：
mvn spring-boot:run

# 3. 验证配置是否生效
# 查看启动日志，确认没有报错
# 测试接口是否可以正常访问
```

### 🔧 备选：方案三（自定义 ShiroRealm）

如果重启后仍然有问题，或者未来需要更精细的权限控制，可以考虑方案三。

---

## 验证步骤

### 1. 重启应用后测试

```bash
# 使用 curl 测试获取角色详情接口
curl -X GET "http://localhost:8080/webgame/character/info/{characterId}" \
  -H "X-Access-Token: {your_jwt_token}"

# 预期结果：返回角色详情，不再报"用户不存在"错误
```

### 2. 检查日志

启动日志中应该看到：
```
[ShiroConfig] 创建 ShiroFilterFactoryBean
[ShiroConfig] 配置拦截器映射
...
/webgame/character/** -> anon
...
```

### 3. 测试属性加点接口

```bash
curl -X POST "http://localhost:8080/webgame/character/update-attributes" \
  -H "Content-Type: application/json" \
  -H "X-Access-Token: {your_jwt_token}" \
  -d '{
    "characterId": "uuid",
    "str": 2,
    "intelligence": 0,
    "agi": 1
  }'

# 预期结果：属性加点成功
```

---

## 常见问题

### Q1: 重启后仍然报同样的错误？

**可能原因：**
1. ShiroConfig 没有被扫描到
2. 有其他配置文件覆盖了 Shiro 配置
3. 应用启动顺序问题

**解决方法：**
1. 检查 `WebGameConfiguration` 是否正确配置了组件扫描
2. 检查是否有多个 ShiroConfig 文件
3. 清理编译缓存后重新编译：
   ```bash
   mvn clean compile
   mvn spring-boot:run
   ```

### Q2: 为什么其他接口（如 /webgame/auth/login）可以正常工作？

因为 `/webgame/auth/**` 接口在 ShiroConfig 中也配置了 `anon`，并且这些接口在之前的开发中已经重启过应用，所以配置已生效。

而 `/webgame/character/**` 是最近才添加的配置，如果之后没有重启过应用，配置就不会生效。

### Q3: 能否只重启部分模块？

JeecgBoot 是单体应用，所有模块都在同一个 JVM 进程中运行。要生效新的 Shiro 配置，必须重启整个应用。

---

## 总结

| 方案 | 难度 | 风险 | 推荐度 |
|------|------|------|--------|
| 方案一：重启应用 | ⭐ | 低 | ⭐⭐⭐⭐⭐ |
| 方案二：同步用户表 | ⭐⭐ | 中 | ⭐⭐ |
| 方案三：自定义ShiroRealm | ⭐⭐⭐ | 高 | ⭐⭐⭐ |
| 方案四：业务层校验 | ⭐⭐ | 中 | ⭐⭐ |

**强烈建议使用方案一**，这是最简单、最安全、最符合框架设计的解决方案。

---

*文档版本：2026-05-08*
*适用项目：网页小游戏后端（JeecgBoot）*
