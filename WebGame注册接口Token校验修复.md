# WebGame 注册接口 Token 校验问题修复

## ❌ 问题描述

**现象**: 前后端联调测试 WebGame 注册接口时，返回 401 错误，提示需要 Token

**错误信息**:
```json
{
  "code": 401,
  "message": "Token 不能为空!"
}
```

**请求信息**:
- URL: `POST /webgame/auth/register`
- Body: `{"username": "testuser", "password": "123456"}`
- Header: 没有携带 Token（因为是注册，还没登录）

---

## 🔍 问题原因

### Shiro 拦截机制

JeecgBoot 使用 **Shiro** 作为安全框架，所有请求都会经过 `JwtFilter` 拦截器：

```java
// ShiroConfig.java 第 202 行
filterChainDefinitionMap.put("/**", "jwt");  // 所有未配置的路径都需要 Token
```

### 当前配置的问题

**系统接口已放行**:
```java
filterChainDefinitionMap.put("/sys/login", "anon");         // ✅ 系统登录
filterChainDefinitionMap.put("/sys/user/register", "anon"); // ✅ 系统注册
```

**WebGame 接口未放行**:
```java
// ❌ 缺少以下配置
// filterChainDefinitionMap.put("/webgame/auth/login", "anon");
// filterChainDefinitionMap.put("/webgame/auth/register", "anon");
// filterChainDefinitionMap.put("/webgame/auth/logout", "anon");
```

### 拦截流程

```
前端请求: POST /webgame/auth/register
    ↓
Shiro 拦截器 (JwtFilter)
    ↓
检查是否匹配 "anon" 规则?
    ↓
❌ 不匹配 (没有配置 /webgame/auth/register)
    ↓
匹配 "/**" → "jwt" 规则
    ↓
要求携带 Token
    ↓
前端没有 Token（因为是注册接口）
    ↓
返回 401 错误: "Token 不能为空!"
```

---

## ✅ 修复方案

### 修改文件: `ShiroConfig.java`

**位置**: `jeecg-boot-base-core/src/main/java/org/jeecg/config/shiro/ShiroConfig.java`

**添加配置**:

```java
// 第 103 行后添加

// WebGame 模块认证接口免登录
filterChainDefinitionMap.put("/webgame/auth/login", "anon");    // WebGame 登录
filterChainDefinitionMap.put("/webgame/auth/register", "anon"); // WebGame 注册
filterChainDefinitionMap.put("/webgame/auth/logout", "anon");   // WebGame 登出
```

### 完整的 Shiro 配置规则（部分）

```java
// 系统认证接口
filterChainDefinitionMap.put("/sys/login", "anon");              // 系统登录
filterChainDefinitionMap.put("/sys/user/register", "anon");      // 系统注册
filterChainDefinitionMap.put("/sys/logout", "anon");             // 系统登出

// WebGame 认证接口（新增）
filterChainDefinitionMap.put("/webgame/auth/login", "anon");     // WebGame 登录
filterChainDefinitionMap.put("/webgame/auth/register", "anon");  // WebGame 注册
filterChainDefinitionMap.put("/webgame/auth/logout", "anon");    // WebGame 登出

// 其他需要 Token 的接口
filterChainDefinitionMap.put("/**", "jwt");  // 默认规则
```

---

##  "anon" 和 "jwt" 的区别

| 规则 | 说明 | 适用场景 |
|------|------|---------|
| **anon** | Anonymous（匿名） | 不需要登录，不需要 Token |
| **jwt** | JWT Token 认证 | 需要登录，必须携带 Token |

### 示例

```java
// 不需要 Token（匿名访问）
filterChainDefinitionMap.put("/webgame/auth/register", "anon");

// 需要 Token（认证访问）
filterChainDefinitionMap.put("/webgame/character/create", "jwt");
filterChainDefinitionMap.put("/webgame/battle/pve", "jwt");
```

---

## 📋 哪些接口需要 "anon"？

### ✅ 应该设置为 "anon" 的接口

1. **登录接口**
   - `/sys/login`
   - `/webgame/auth/login`

2. **注册接口**
   - `/sys/user/register`
   - `/webgame/auth/register`

3. **登出接口**
   - `/sys/logout`
   - `/webgame/auth/logout`

4. **验证码接口**
   - `/sys/randomImage/**`
   - `/sys/checkCaptcha`

5. **公开接口**
   - 文档、静态资源
   - 不需要用户身份的查询接口

###  应该设置为 "jwt" 的接口

1. **用户操作接口**
   - `/webgame/character/**`
   - `/webgame/item/**`
   - `/webgame/battle/**`

2. **数据修改接口**
   - POST/PUT/DELETE 请求
   - 涉及用户数据的操作

3. **敏感信息查询**
   - `/webgame/user/info`
   - `/webgame/character/bag`

---

## 🔄 修复后的拦截流程

```
前端请求: POST /webgame/auth/register
    ↓
Shiro 拦截器 (JwtFilter)
    ↓
检查是否匹配 "anon" 规则?
    ↓
✅ 匹配！/webgame/auth/register → "anon"
    ↓
放行请求，不需要 Token
    ↓
Controller 处理注册逻辑
    ↓
返回 200 成功
```

---

## 🧪 测试验证

### 1. 重启服务

修改 Shiro 配置后，需要重启服务才能生效：

```bash
cd jeecg-module-system/jeecg-system-start
mvn spring-boot:run
```

### 2. 测试注册接口

```bash
curl -X POST http://localhost:8080/jeecg-boot/webgame/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456"
  }'
```

**预期响应**:
```json
{
  "code": 200,
  "message": "注册成功，请登录",
  "data": null
}
```

### 3. 测试登录接口

```bash
curl -X POST http://localhost:8080/jeecg-boot/webgame/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456"
  }'
```

**预期响应**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "id": "xxx-xxx-xxx",
    "username": "testuser",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

### 4. 测试需要 Token 的接口

使用上一步获取的 Token 访问需要认证的接口：

```bash
curl -X GET http://localhost:8080/jeecg-boot/webgame/character/info \
  -H "X-Access-Token: eyJhbGciOiJIUzI1NiIs..."
```

**预期**: 应该返回角色信息，而不是 401 错误。

---

## 📚 Shiro 配置完整说明

### Filter Chain 规则说明

```java
/**
 * Filter Chain定义说明
 *
 * 1、一个URL可以配置多个Filter，使用逗号分隔
 * 2、当设置多个过滤器时，全部验证通过，才视为通过
 * 3、部分过滤器可指定参数，如perms，roles
 */
```

### 常用过滤器

| 过滤器 | 说明 | 示例 |
|--------|------|------|
| **anon** | 匿名访问，不需要认证 | `"/sys/login", "anon"` |
| **jwt** | JWT Token 认证 | `"/webgame/**", "jwt"` |
| **authc** | 需要认证（Session） | `"/admin/**", "authc"` |
| **perms** | 需要权限 | `"/admin/delete", "perms[admin:delete]"` |
| **roles** | 需要角色 | `"/admin/**", "roles[admin]"` |

### 配置顺序很重要

```java
// ✅ 正确：从上到下匹配，先具体后通用
filterChainDefinitionMap.put("/sys/login", "anon");      // 具体路径先匹配
filterChainDefinitionMap.put("/sys/**", "jwt");          // 通用路径后匹配
filterChainDefinitionMap.put("/**", "jwt");              // 最后匹配所有

// ❌ 错误："/**" 放在最前面，会覆盖所有规则
filterChainDefinitionMap.put("/**", "jwt");              // 先匹配所有
filterChainDefinitionMap.put("/sys/login", "anon");      // 永远不会执行
```

---

## 🎮 WebGame 模块完整配置建议

### 当前配置

```java
// 认证接口（免登录）
filterChainDefinitionMap.put("/webgame/auth/login", "anon");
filterChainDefinitionMap.put("/webgame/auth/register", "anon");
filterChainDefinitionMap.put("/webgame/auth/logout", "anon");

// 其他接口（需要登录）
filterChainDefinitionMap.put("/webgame/**", "jwt");  // 未来添加
```

### 未来可能需要添加的配置

```java
// WebSocket 接口（如果需要）
filterChainDefinitionMap.put("/webgame/ws/**", "anon");

// 公开查询接口（如排行榜）
filterChainDefinitionMap.put("/webgame/ranking/list", "anon");

// 游戏下载资源
filterChainDefinitionMap.put("/webgame/download/**", "anon");
```

---

## ⚠️ 注意事项

### 1. 修改后必须重启

Shiro 配置在服务启动时加载，修改后需要重启服务：

```bash
# 停止服务（Ctrl+C）
# 重新启动
mvn spring-boot:run
```

### 2. 配置顺序不能错

```java
// ❌ 错误顺序
filterChainDefinitionMap.put("/webgame/**", "jwt");     // 会拦截所有 /webgame/**
filterChainDefinitionMap.put("/webgame/auth/login", "anon");  // 这行不会生效

// ✅ 正确顺序
filterChainDefinitionMap.put("/webgame/auth/login", "anon");  // 先匹配具体路径
filterChainDefinitionMap.put("/webgame/**", "jwt");     // 后匹配通用路径
```

### 3. 使用通配符

```java
// 匹配单个路径
filterChainDefinitionMap.put("/webgame/auth/login", "anon");

// 匹配所有子路径
filterChainDefinitionMap.put("/webgame/auth/**", "anon");

// 两种写法都可以，看需求选择
```

---

## 📊 完整的 Shiro 配置清单

### 系统接口（已配置）

```java
filterChainDefinitionMap.put("/sys/login", "anon");
filterChainDefinitionMap.put("/sys/logout", "anon");
filterChainDefinitionMap.put("/sys/user/register", "anon");
filterChainDefinitionMap.put("/sys/randomImage/**", "anon");
```

### WebGame 接口（新增）

```java
filterChainDefinitionMap.put("/webgame/auth/login", "anon");
filterChainDefinitionMap.put("/webgame/auth/register", "anon");
filterChainDefinitionMap.put("/webgame/auth/logout", "anon");
```

### Swagger 文档（已配置）

```java
filterChainDefinitionMap.put("/doc.html", "anon");
filterChainDefinitionMap.put("/swagger**/**", "anon");
filterChainDefinitionMap.put("/v3/**", "anon");
```

### 静态资源（已配置）

```java
filterChainDefinitionMap.put("/**/*.js", "anon");
filterChainDefinitionMap.put("/**/*.css", "anon");
filterChainDefinitionMap.put("/**/*.html", "anon");
```

### 默认规则（最后）

```java
filterChainDefinitionMap.put("/**", "jwt");  // 所有其他接口需要 Token
```

---

## ✅ 修复完成

**修改内容**:
- 在 `ShiroConfig.java` 中添加了 WebGame 认证接口的免登录配置
- 登录、注册、登出接口现在不需要 Token 即可访问

**重启服务后即可生效！** 🚀

---

## 🔍 调试技巧

### 1. 查看 Shiro 配置日志

启动时查看日志，确认配置是否生效：

```
INFO - ShiroFilterFactoryBean - Filter chain definition:
  /webgame/auth/login = anon
  /webgame/auth/register = anon
  /webgame/auth/logout = anon
  /** = jwt
```

### 2. 开启 Shiro 调试日志

在 `application.yml` 中添加：

```yaml
logging:
  level:
    org.apache.shiro: DEBUG
    org.jeecg.config.shiro: DEBUG
```

### 3. 使用 Postman 测试

```
POST http://localhost:8080/jeecg-boot/webgame/auth/register

Headers:
  Content-Type: application/json

Body (raw JSON):
{
  "username": "testuser",
  "password": "123456"
}

Expected: 200 OK
```

---

**问题已修复！现在注册和登录接口都不需要 Token 了！** 🎉
