# JeecgBoot 系统登录参数填写指南

## 📋 登录接口信息

**接口地址**: `POST /sys/login`

**完整URL**: `http://localhost:8080/jeecg-boot/sys/login`

---

## 🔑 参数说明

### 1. username（用户名）
- **类型**: String
- **必填**: ✅ 是
- **说明**: 系统管理员账号
- **默认值**: `admin`
- **示例**: `"admin"`

### 2. password（密码）
- **类型**: String
- **必填**: ✅ 是
- **说明**: 用户密码（支持明文或 AES 加密）
- **默认值**: `123456`
- **示例**: `"123456"`

### 3. loginOrgCode（登录部门编码）
- **类型**: String
- **必填**: ❌ 否（可选）
- **说明**: 如果用户属于多个部门，可以指定登录的部门
- **默认值**: `""`（空字符串，系统会自动选择第一个部门）
- **示例**: `""` 或 `"A01"`

### 4. captcha（验证码）
- **类型**: String
- **必填**: ✅ 是（如果开启了验证码功能）
- **说明**: 图形验证码的内容（图片上显示的数字/字母）
- **获取方式**: 先调用验证码接口获取图片和 checkKey
- **示例**: `"1242131421"`（这是你截图中的值）

### 5. checkKey（验证码键）
- **类型**: String
- **必填**: ✅ 是（如果开启了验证码功能）
- **说明**: 验证码的唯一标识，用于后端验证
- **获取方式**: 从验证码接口返回
- **示例**: `"D9EK"`（这是你截图中的值）

---

## 🎯 完整的登录流程

### 步骤 1: 获取验证码

**接口**: `GET /sys/randomImage/{checkKey}`

**请求示例**:
```bash
# 生成一个随机的 checkKey（可以用时间戳或 UUID）
CHECK_KEY=$(date +%s)

# 获取验证码图片
curl -X GET "http://localhost:8080/jeecg-boot/sys/randomImage/${CHECK_KEY}" \
  -o captcha.png
```

**返回**: 一张验证码图片（captcha.png）

**手动操作**: 打开图片，看到验证码内容（比如：`AB12`）

---

### 步骤 2: 登录系统

**接口**: `POST /sys/login`

**请求示例**:

```bash
curl -X POST http://localhost:8080/jeecg-boot/sys/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "123456",
    "loginOrgCode": "",
    "captcha": "AB12",
    "checkKey": "1234567890"
  }'
```

**成功响应**:
```json
{
  "success": true,
  "message": "登录成功",
  "code": 200,
  "result": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "id": "1",
      "username": "admin",
      "realname": "管理员",
      ...
    },
    "sysAllDictItems": [...]
  }
}
```

---

## 💡 参数填写示例

### 示例 1: 标准登录（带验证码）

```json
{
  "username": "admin",
  "password": "123456",
  "loginOrgCode": "",
  "captcha": "1242131421",
  "checkKey": "D9EK"
}
```

**说明**:
- 使用默认管理员账号
- 密码是默认的 `123456`
- 不指定部门（空字符串）
- 验证码从图片中读取
- checkKey 是获取验证码时生成的

---

### 示例 2: 指定部门登录

```json
{
  "username": "admin",
  "password": "123456",
  "loginOrgCode": "A01A02",
  "captcha": "AB12",
  "checkKey": "1678901234"
}
```

**说明**:
- `loginOrgCode`: 指定登录到编码为 `A01A02` 的部门
- 适用于用户有多个部门权限的情况

---

### 示例 3: 自定义用户登录

```json
{
  "username": "testuser",
  "password": "test123",
  "loginOrgCode": "",
  "captcha": "XY78",
  "checkKey": "1678901235"
}
```

**说明**:
- 使用自定义创建的用户账号
- 其他参数同上

---

## 🔍 如何获取验证码

### 方法 1: 通过浏览器

1. 打开浏览器访问: http://localhost:8080/jeecg-boot/sys/randomImage/123456
2. 会显示一张验证码图片
3. 手动记录图片上的文字
4. 使用相同的 checkKey（这里是 `123456`）

### 方法 2: 通过 Postman

1. 新建 GET 请求: `http://localhost:8080/jeecg-boot/sys/randomImage/test123`
2. 发送请求，在 Response 的 "Send and Download" 中保存图片
3. 查看图片，记录验证码内容
4. 在登录请求中使用相同的 checkKey（`test123`）

### 方法 3: 通过代码生成

```bash
# 使用当前时间戳作为 checkKey
CHECK_KEY=$(date +%s)
echo "CheckKey: $CHECK_KEY"

# 获取验证码图片
curl -X GET "http://localhost:8080/jeecg-boot/sys/randomImage/${CHECK_KEY}" \
  -o captcha.png

# 打开图片查看验证码（手动）
# Windows: start captcha.png
# Mac: open captcha.png
# Linux: xdg-open captcha.png

# 然后在登录请求中使用
```

---

## ⚠️ 常见问题

### Q1: 验证码一直提示错误

**原因**: 
- checkKey 不一致（获取验证码和登录时用的不是同一个）
- 验证码已过期（默认 10 分钟）
- 验证码大小写错误（系统会自动转小写比较）

**解决方法**:
```bash
# 确保两次使用相同的 checkKey
CHECK_KEY="mykey123"

# 1. 获取验证码
curl "http://localhost:8080/jeecg-boot/sys/randomImage/${CHECK_KEY}" -o captcha.png

# 2. 查看图片，假设看到验证码是 AB12

# 3. 登录时使用相同的 checkKey
curl -X POST http://localhost:8080/jeecg-boot/sys/login \
  -H "Content-Type: application/json" \
  -d "{
    \"username\": \"admin\",
    \"password\": \"123456\",
    \"loginOrgCode\": \"\",
    \"captcha\": \"AB12\",
    \"checkKey\": \"${CHECK_KEY}\"
  }"
```

---

### Q2: 如何关闭验证码功能？

**方法 1: 通过配置文件**

编辑 `application.yml`:
```yaml
jeecg:
  firewall:
    enableLoginCaptcha: false  # 关闭登录验证码
```

**方法 2: 通过数据库**

```sql
UPDATE sys_config 
SET config_value = '0' 
WHERE config_key = 'sys.login.captcha';
```

关闭后，登录时可以不传 `captcha` 和 `checkKey`：
```json
{
  "username": "admin",
  "password": "123456",
  "loginOrgCode": ""
}
```

---

### Q3: loginOrgCode 是什么？

**说明**: 
- 如果一个用户属于多个部门，登录时需要选择进入哪个部门
- 如果只有一个部门，可以留空，系统会自动选择
- 部门编码可以在系统管理 → 部门管理中查看

**示例**:
```json
{
  "username": "admin",
  "password": "123456",
  "loginOrgCode": "A01",  // 登录到 A01 部门
  "captcha": "AB12",
  "checkKey": "123456"
}
```

---

### Q4: 密码需要加密吗？

**说明**: 
- 默认支持**明文传输**
- 系统后端会自动进行 AES 解密（如果检测到是加密的）
- 建议生产环境使用 HTTPS + 前端加密

**明文示例**:
```json
{
  "password": "123456"
}
```

**加密示例**（需要前端实现 AES 加密）:
```json
{
  "password": "U2FsdGVkX1+..."  // AES 加密后的密文
}
```

---

## 📝 Postman 测试步骤

### 1. 获取验证码

- **Method**: GET
- **URL**: `http://localhost:8080/jeecg-boot/sys/randomImage/test123`
- **点击 "Send and Download"**
- 保存验证码图片，查看内容（比如：`AB12`）

### 2. 登录系统

- **Method**: POST
- **URL**: `http://localhost:8080/jeecg-boot/sys/login`
- **Headers**: 
  - `Content-Type`: `application/json`
- **Body** (raw JSON):
```json
{
  "username": "admin",
  "password": "123456",
  "loginOrgCode": "",
  "captcha": "AB12",
  "checkKey": "test123"
}
```

### 3. 保存 Token

登录成功后，从响应中复制 `result.token` 的值，后续接口需要在 Header 中携带：

```
X-Access-Token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 🎯 快速测试脚本

```bash
#!/bin/bash

# 配置
BASE_URL="http://localhost:8080/jeecg-boot"
USERNAME="admin"
PASSWORD="123456"
CHECK_KEY=$(date +%s)

echo "=== JeecgBoot 系统登录测试 ==="
echo ""

# 1. 获取验证码
echo "1. 获取验证码图片..."
curl -s -X GET "${BASE_URL}/sys/randomImage/${CHECK_KEY}" -o captcha.png
echo "   验证码图片已保存到 captcha.png"
echo "   请打开图片查看验证码内容"
echo ""

# 2. 提示输入验证码
read -p "2. 请输入验证码内容: " CAPTCHA
echo ""

# 3. 登录
echo "3. 正在登录..."
RESPONSE=$(curl -s -X POST "${BASE_URL}/sys/login" \
  -H "Content-Type: application/json" \
  -d "{
    \"username\": \"${USERNAME}\",
    \"password\": \"${PASSWORD}\",
    \"loginOrgCode\": \"\",
    \"captcha\": \"${CAPTCHA}\",
    \"checkKey\": \"${CHECK_KEY}\"
  }")

# 4. 显示结果
echo ""
echo "=== 登录结果 ==="
echo "${RESPONSE}" | jq .

# 5. 提取 Token
TOKEN=$(echo "${RESPONSE}" | jq -r '.result.token')
if [ "$TOKEN" != "null" ]; then
  echo ""
  echo "✅ 登录成功！"
  echo "Token: ${TOKEN}"
else
  echo ""
  echo "❌ 登录失败！"
fi
```

---

## 📚 相关接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/sys/login` | POST | 用户登录 |
| `/sys/randomImage/{checkKey}` | GET | 获取验证码图片 |
| `/sys/logout` | POST | 用户登出 |
| `/sys/user/getUserInfo` | GET | 获取当前用户信息 |

---

## ✅ 检查清单

登录前确认：

- [ ] 服务已启动（http://localhost:8080）
- [ ] 数据库已初始化
- [ ] 默认管理员账号存在（admin/123456）
- [ ] 获取了验证码图片和 checkKey
- [ ] captcha 和 checkKey 匹配
- [ ] Content-Type 设置为 application/json

---

**按照以上步骤操作，就能成功获取系统 Token 了！** 🎉

如有问题，请提供具体的错误信息。
