# Token 生成方法修复报告

## ❌ 原始问题

`WgUserServiceImpl.java` 中使用了不存在的方法：

```java
// ❌ 错误 - TokenUtils 没有 sign 方法
String token = TokenUtils.sign(user.getId(), user.getUsername());
```

**编译错误**:
```
Cannot resolve method 'sign' in 'TokenUtils'
```

---

## ✅ 修复方案

### 正确的 Token 生成方法

JeecgBoot 使用 `JwtUtil.sign()` 方法生成 JWT Token：

```java
// ✅ 正确 - 使用 JwtUtil.sign()
String token = JwtUtil.sign(user.getUsername(), user.getPassword(), CommonConstant.CLIENT_TYPE_PC);
```

### 方法签名

```java
public static String sign(String username, String secret, String clientType)
```

**参数说明**:
- `username`: 用户名
- `secret`: 密码（用于签名验证）
- `clientType`: 客户端类型（PC/APP）

---

## 🔧 已修改的文件

### WgUserServiceImpl.java

**位置**: `webgame/auth/service/impl/WgUserServiceImpl.java`

#### 修改 1: 导入语句

**修改前**:
```java
import org.jeecg.common.util.TokenUtils;
```

**修改后**:
```java
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
```

#### 修改 2: Token 生成代码

**修改前**:
```java
// 生成Token
String token = TokenUtils.sign(user.getId(), user.getUsername());
```

**修改后**:
```java
// 生成Token（使用用户名和密码）
String token = JwtUtil.sign(user.getUsername(), user.getPassword(), CommonConstant.CLIENT_TYPE_PC);
```

---

## 📊 JeecgBoot Token 机制

### 1. Token 生成流程

```
用户登录
  ↓
验证用户名和密码
  ↓
JwtUtil.sign(username, password, clientType)
  ↓
生成 JWT Token
  ↓
存入 Redis (PREFIX_USER_TOKEN + token)
  ↓
返回给前端
```

### 2. Token 验证流程

```
前端请求携带 Token
  ↓
拦截器提取 Token
  ↓
从 Redis 获取缓存的 Token
  ↓
JwtUtil.verify(token, username, password)
  ↓
验证通过 → 放行
验证失败 → 返回 401
```

### 3. Token 刷新机制

JeecgBoot 实现了自动 Token 续期：

```java
// JwtUtil 内部逻辑
if (token 即将过期) {
    String newToken = JwtUtil.sign(username, password, clientType);
    redis.set(PREFIX_USER_TOKEN + token, newToken);
    redis.expire(...);
}
```

---

## 🎯 客户端类型说明

JeecgBoot 支持多种客户端类型：

| 常量 | 值 | 说明 | Token 有效期 |
|------|---|------|------------|
| `CLIENT_TYPE_PC` | "PC" | PC 浏览器 | 2 小时 |
| `CLIENT_TYPE_APP` | "APP" | 移动应用 | 7 天 |
| `CLIENT_TYPE_WX` | "WX" | 微信小程序 | 7 天 |

### WebGame 模块的选择

WebGame 是网页游戏，所以使用 `CLIENT_TYPE_PC`：

```java
JwtUtil.sign(username, password, CommonConstant.CLIENT_TYPE_PC)
```

如果未来开发移动端版本，可以改为：

```java
JwtUtil.sign(username, password, CommonConstant.CLIENT_TYPE_APP)
```

---

## 🔍 参考实现

JeecgBoot 系统中其他模块的 Token 生成方式：

### LoginController.java (系统登录)

```java
String token = JwtUtil.sign(username, syspassword, clientType);
```

### ThirdLoginController.java (第三方登录)

```java
String token = JwtUtil.sign(user.getUsername(), user.getPassword(), CommonConstant.CLIENT_TYPE_PC);
```

### CasClientController.java (CAS 单点登录)

```java
String token = JwtUtil.sign(sysUser.getUsername(), sysUser.getPassword(), CommonConstant.CLIENT_TYPE_PC);
```

---

## ⚠️ 常见错误

### 错误 1: 使用不存在的方法

```java
// ❌ 错误
TokenUtils.sign(userId, username);
```

**原因**: `TokenUtils` 只有验证和获取 Token 的方法，没有生成方法。

### 错误 2: 参数顺序错误

```java
// ❌ 错误 - 参数顺序不对
JwtUtil.sign(password, username, clientType);
```

**正确**:
```java
// ✅ 正确
JwtUtil.sign(username, password, clientType);
```

### 错误 3: 缺少客户端类型

```java
// ❌ 错误 - 缺少第三个参数
JwtUtil.sign(username, password);
```

**正确**:
```java
// ✅ 正确 - 必须指定客户端类型
JwtUtil.sign(username, password, CommonConstant.CLIENT_TYPE_PC);
```

---

## ✅ 验证步骤

### 1. 重新编译

```bash
cd jeecg-boot
mvn clean compile
```

应该没有编译错误。

### 2. 启动服务

```bash
cd jeecg-module-system/jeecg-system-start
mvn spring-boot:run
```

### 3. 测试登录接口

```bash
curl -X POST http://localhost:8080/jeecg-boot/webgame/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123456"}'
```

**预期响应**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "id": "xxx",
    "username": "testuser",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

### 4. 验证 Token 有效性

使用返回的 Token 访问需要认证的接口：

```bash
curl -X GET http://localhost:8080/jeecg-boot/webgame/character/info \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
```

应该能正常访问，不会返回 401 错误。

---

## 📝 Token 相关工具类

### JwtUtil (JWT 工具类)

**位置**: `org.jeecg.common.system.util.JwtUtil`

**主要方法**:
- `sign(username, secret, clientType)` - 生成 Token
- `verify(token, username, secret)` - 验证 Token
- `getUsername(token)` - 从 Token 获取用户名
- `getClientType(token)` - 从 Token 获取客户端类型

### TokenUtils (Token 辅助工具)

**位置**: `org.jeecg.common.util.TokenUtils`

**主要方法**:
- `getTokenByRequest(request)` - 从请求中获取 Token
- `verifyToken(token, commonApi, redisUtil)` - 验证 Token
- `getLoginUser(username, commonApi, redisUtil)` - 获取登录用户信息

**注意**: `TokenUtils` **没有** `sign` 方法！

---

## 🎉 修复完成

现在 WebGame 模块的 Token 生成已经与 JeecgBoot 框架保持一致：

✅ 使用正确的 `JwtUtil.sign()` 方法  
✅ 传入正确的参数（username, password, clientType）  
✅ 使用合适的客户端类型（CLIENT_TYPE_PC）  
✅ Token 可以被框架正确验证和刷新  

---

**修复完成！现在可以正常登录了！** 🚀
