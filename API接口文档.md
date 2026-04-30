# 剑之传说 - 前后端接口文档

> 版本：v1.0.0  
> 更新日期：2026-04-29  
> 状态：登录/注册模块

---

## 通用约定

### Base URL

```
待定，建议格式：https://api.example.com/v1
```

### 请求格式

- Content-Type: `application/json`
- 字符编码: `UTF-8`

### 认证方式

- 采用 **JWT (JSON Web Token)** 认证
- 登录成功后，后端返回 `token`
- 后续需要鉴权的接口，前端会在请求头中携带：

```
Authorization: Bearer <token>
```

### 统一响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `code` | `number` | 状态码，200 成功，其他为失败 |
| `message` | `string` | 提示信息，前端会直接展示给用户 |
| `data` | `object \| null` | 业务数据，失败时为 `null` |

### 常用错误码

| code | 含义 |
|------|------|
| `200` | 成功 |
| `400` | 请求参数错误 |
| `401` | 未认证 / Token 过期 |
| `409` | 资源冲突（如用户名已存在） |
| `500` | 服务器内部错误 |

---

## 接口列表

| 序号 | 方法 | 路径 | 说明 | 是否需要鉴权 |
|------|------|------|------|:---:|
| 1 | POST | `/auth/login` | 用户登录 | ❌ |
| 2 | POST | `/auth/register` | 用户注册 | ❌ |
| 3 | POST | `/auth/logout` | 用户登出 | ✅ |

---

## 1. 用户登录

### `POST /auth/login`

#### 请求参数（Request Body）

```json
{
  "username": "testuser",
  "password": "123456"
}
```

| 字段 | 类型 | 必填 | 校验规则 | 说明 |
|------|------|:---:|------|------|
| `username` | `string` | ✅ | 3-20 个字符 | 用户名 |
| `password` | `string` | ✅ | 6-20 个字符 | 密码（明文传输，建议后端做 hash） |

#### 成功响应

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "username": "testuser",
    "token": "eyJhbGciOiJIUzI1NiIs..."
  }
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `data.id` | `string` | 用户唯一标识（UUID） |
| `data.username` | `string` | 用户名 |
| `data.token` | `string` | JWT Token，前端会存入 localStorage |

#### 失败响应示例

**用户名不存在：**
```json
{
  "code": 401,
  "message": "用户名不存在",
  "data": null
}
```

**密码错误：**
```json
{
  "code": 401,
  "message": "密码错误",
  "data": null
}
```

---

## 2. 用户注册

### `POST /auth/register`

#### 请求参数（Request Body）

```json
{
  "username": "newplayer",
  "password": "abc123"
}
```

| 字段 | 类型 | 必填 | 校验规则 | 说明 |
|------|------|:---:|------|------|
| `username` | `string` | ✅ | 3-20 个字符 | 用户名，需唯一 |
| `password` | `string` | ✅ | 6-20 个字符 | 密码（明文传输，后端做 hash 存储） |

> **备注**：前端已做「确认密码」的一致性校验，不会传 `confirmPassword` 字段给后端。

#### 成功响应

```json
{
  "code": 200,
  "message": "注册成功，请登录",
  "data": null
}
```

> **说明**：注册成功后前端不会自动登录，而是切换到登录 Tab 让用户手动登录。所以注册接口无需返回 token。

#### 失败响应示例

**用户名已存在：**
```json
{
  "code": 409,
  "message": "该用户名已被注册",
  "data": null
}
```

**参数校验失败：**
```json
{
  "code": 400,
  "message": "用户名需要 3-20 个字符",
  "data": null
}
```

---

## 3. 用户登出

### `POST /auth/logout`

> 需要在请求头携带 `Authorization: Bearer <token>`

#### 请求参数

无（Token 从 Header 中获取）

#### 成功响应

```json
{
  "code": 200,
  "message": "已退出登录",
  "data": null
}
```

> **说明**：前端会清除本地 localStorage 中的用户信息。后端可选择将该 Token 加入黑名单，或直接依赖 Token 过期机制。

---

## 前端数据结构参考

### 前端存储的用户对象

```typescript
interface UserInfo {
  id: string       // 用户 UUID
  username: string // 用户名
  token: string    // JWT Token
}
```

前端会将此对象以 `JSON.stringify` 存入 `localStorage`，key 为 `auth_user`。

### 前端校验规则汇总

| 字段 | 前端校验 | 建议后端也校验 |
|------|----------|:---:|
| 用户名 | 非空，3-20 字符 | ✅ |
| 密码 | 非空，6-20 字符 | ✅ |
| 确认密码 | 与密码一致（仅前端校验，不传后端） | — |

---

## 补充说明

1. **密码安全**：当前前端明文传输密码，建议上线前改为 HTTPS，或前端做一层加密后传输
2. **Token 有效期**：建议后端设置合理的 JWT 过期时间（如 24h），前端收到 401 时自动跳转登录页
3. **CORS**：后端需配置跨域允许，开发环境前端地址为 `http://localhost:5173`
