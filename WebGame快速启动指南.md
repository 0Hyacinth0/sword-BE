# WebGame 模块快速启动指南

## 前置条件

确保已安装以下软件：

- JDK 17+
- Maven 3.6+
- MySQL 5.7+ 或 PostgreSQL
- Redis 6.0+
- IDE (推荐 IntelliJ IDEA)

## 步骤一：初始化数据库

### 1. 创建数据库

```sql
CREATE DATABASE webgame DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

### 2. 执行建表脚本

```bash
mysql -u root -p webgame < jeecg-boot/db/webgame.sql
```

或者在 MySQL 客户端中执行：

```sql
USE webgame;
SOURCE G:/Work/网页小游戏/网页小游戏后端/jeecg-boot/db/webgame.sql;
```

### 3. 验证数据表

```sql
SHOW TABLES LIKE 'wg_%';
```

应该看到 11 张表：
- wg_user
- wg_character
- wg_item_template
- wg_player_item
- wg_monster
- wg_battle_log
- wg_dungeon
- wg_dungeon_room
- wg_pvp_match
- wg_pvp_ranking
- wg_friend

## 步骤二：配置 Redis

### Windows 用户

1. 下载 Redis for Windows: https://github.com/microsoftarchive/redis/releases
2. 解压并运行 `redis-server.exe`
3. 默认端口 6379，无需密码

### Linux/Mac 用户

```bash
# Ubuntu/Debian
sudo apt-get install redis-server
sudo systemctl start redis

# CentOS
sudo yum install redis
sudo systemctl start redis

# Mac
brew install redis
brew services start redis
```

### 验证 Redis

```bash
redis-cli ping
# 应返回 PONG
```

## 步骤三：配置数据库连接

编辑 `jeecg-boot/jeecg-module-system/jeecg-system-start/src/main/resources/application-dev.yml`

找到数据源配置部分，修改为：

```yaml
spring:
  datasource:
    dynamic:
      datasource:
        master:
          url: jdbc:mysql://localhost:3306/webgame?useUnicode=true&characterEncoding=utf8&autoReconnect=true&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&allowPublicKeyRetrieval=true&useSSL=false
          username: root
          password: 你的MySQL密码
          driver-class-name: com.mysql.cj.jdbc.Driver
```

同时确保 Redis 配置正确：

```yaml
spring:
  redis:
    database: 0
    host: localhost
    port: 6379
    password: 
    timeout: 6000
```

## 步骤四：编译项目

### 方法一：使用命令行

```bash
cd G:\Work\网页小游戏\网页小游戏后端\jeecg-boot

# 清理并编译
mvn clean install -DskipTests

# 或者只编译 webgame 模块
cd jeecg-boot-module/jeecg-module-webgame
mvn clean install
```

### 方法二：使用 IDE

1. 用 IntelliJ IDEA 打开 `jeecg-boot/pom.xml`
2. 等待 Maven 依赖下载完成
3. 点击 Build -> Rebuild Project

## 步骤五：启动项目

### 方法一：使用命令行

```bash
cd jeecg-module-system/jeecg-system-start
mvn spring-boot:run
```

### 方法二：使用 IDE

1. 找到启动类：`org.jeecg.JeecgSystemApplication`
2. 右键 -> Run 'JeecgSystemApplication'

### 启动成功标志

看到以下日志表示启动成功：

```
----------------------------------------------------------
    Jeecg Boot 启动成功！
    接口文档: http://localhost:8080/jeecg-boot/doc.html
----------------------------------------------------------
```

## 步骤六：测试接口

### ⚠️ 重要：Swagger 配置

**如果访问 Swagger 文档看不到 WebGame 接口**，请确认：

1. **启动类已修改**
   - 已添加 `@Import({WebGameConfiguration.class})`
   - 位置：`JeecgSystemApplication.java`

2. **重新编译项目**
   ```bash
   mvn clean install -DskipTests
   ```

3. **重启服务**
   ```bash
   cd jeecg-module-system/jeecg-system-start
   mvn spring-boot:run
   ```

详细说明请查看：[Swagger配置说明.md](./Swagger配置说明.md)

---

### 1. 访问 Swagger 文档

浏览器打开：http://localhost:8080/jeecg-boot/doc.html

在左侧菜单中找到 "webgame" 或 "游戏认证接口"

### 2. 测试注册接口

使用 curl：

```bash
curl -X POST http://localhost:8080/jeecg-boot/webgame/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456"
  }'
```

预期响应：

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

预期响应：

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

### 4. 使用 Postman 测试

#### 注册

- Method: POST
- URL: http://localhost:8080/jeecg-boot/webgame/auth/register
- Headers: Content-Type: application/json
- Body (raw JSON):
```json
{
  "username": "newplayer",
  "password": "abc123"
}
```

#### 登录

- Method: POST
- URL: http://localhost:8080/jeecg-boot/webgame/auth/login
- Headers: Content-Type: application/json
- Body (raw JSON):
```json
{
  "username": "newplayer",
  "password": "abc123"
}
```

复制响应中的 `token` 值，后续接口需要使用。

#### 创建角色（需要认证）

- Method: POST
- URL: http://localhost:8080/jeecg-boot/webgame/character/create
- Headers: 
  - Content-Type: application/json
  - Authorization: Bearer <你的token>
- Body (raw JSON):
```json
{
  "characterName": "剑士小明",
  "profession": 1
}
```

## 常见问题

### Q1: 启动时报错 "Connection refused"

**原因**: Redis 未启动

**解决**: 
```bash
# Windows
redis-server.exe

# Linux
sudo systemctl start redis

# Mac
brew services start redis
```

### Q2: 数据库连接失败

**检查项**:
1. MySQL 是否启动
2. 用户名密码是否正确
3. 数据库 `webgame` 是否已创建
4. 防火墙是否阻止 3306 端口

**测试连接**:
```bash
mysql -u root -p -h localhost
```

### Q3: 编译错误 "找不到符号"

**原因**: Maven 依赖未下载完成

**解决**:
```bash
# 清理并重新下载依赖
mvn clean install -U

# 或者删除本地仓库后重新下载
rm -rf ~/.m2/repository/org/jeecgframework
mvn clean install
```

### Q4: 端口 8080 被占用

**解决方法一**: 修改端口

编辑 `application.yml`:
```yaml
server:
  port: 8081
```

**解决方法二**: 关闭占用端口的进程

Windows:
```powershell
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

Linux/Mac:
```bash
lsof -i :8080
kill -9 <PID>
```

### Q5: Token 验证失败

**可能原因**:
1. Token 已过期（默认 24 小时）
2. Token 格式错误
3. Redis 中的 Token 已被删除

**解决**: 重新登录获取新 Token

### Q6: 中文乱码

**检查项**:
1. 数据库字符集是否为 utf8mb4
2. 连接 URL 是否包含 `characterEncoding=utf8`
3. IDE 文件编码是否为 UTF-8

**修复**:
```sql
ALTER DATABASE webgame CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE wg_user CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

## 开发建议

### 1. 开启热部署

在 `pom.xml` 中添加：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <optional>true</optional>
</dependency>
```

IDEA 设置：
- File -> Settings -> Build, Execution, Deployment -> Compiler
- 勾选 "Build project automatically"
- Ctrl + Shift + A -> 搜索 "Registry" -> 勾选 "compiler.automake.allow.when.app.running"

### 2. 日志级别调整

开发时可以在 `application-dev.yml` 中设置：

```yaml
logging:
  level:
    org.jeecg.modules.webgame: DEBUG
```

### 3. 使用 MyBatis Plus 代码生成器

可以快速生成 CRUD 代码：

```java
// 参考 jeecg-boot 官方文档
// https://doc.jeecg.com/2043888
```

## 下一步

接口测试通过后，可以：

1. **查看完整 API 文档**: `WebGame接口文档.md`
2. **了解模块架构**: `jeecg-boot-module/jeecg-module-webgame/README.md`
3. **查看开发总结**: `WebGame模块开发总结.md`
4. **开始实现业务逻辑**: 按照优先级 P0 -> P1 -> P2

## 技术支持

- JeecgBoot 官方文档: https://doc.jeecg.com
- Spring Boot 文档: https://spring.io/projects/spring-boot
- MyBatis Plus 文档: https://baomidou.com

## 附录：完整的测试流程

```bash
# 1. 注册用户
curl -X POST http://localhost:8080/jeecg-boot/webgame/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"player1","password":"pass123"}'

# 2. 登录获取 token
TOKEN=$(curl -s -X POST http://localhost:8080/jeecg-boot/webgame/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"player1","password":"pass123"}' | jq -r '.data.token')

echo "Token: $TOKEN"

# 3. 创建角色
curl -X POST http://localhost:8080/jeecg-boot/webgame/character/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"characterName":"战士一号","profession":1}'

# 4. 查询角色信息
curl -X GET http://localhost:8080/jeecg-boot/webgame/character/info \
  -H "Authorization: Bearer $TOKEN"
```

> 注意：以上命令使用了 `jq` 工具解析 JSON，Windows 用户可以先安装或使用 Postman。

祝开发顺利！🎮
