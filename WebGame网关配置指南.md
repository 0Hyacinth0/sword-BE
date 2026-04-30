# WebGame 模块网关配置指南

## ⚠️ 重要提示

**WebGame 模块的网关路由尚未配置！** 需要手动在 Nacos 配置中心添加路由规则。

---

## 📋 配置方案（二选一）

### 方案一：通过 Nacos 控制台配置（推荐）✅

#### 步骤 1：登录 Nacos 控制台

访问：`http://localhost:8848/nacos`

默认账号密码：
- 用户名：`nacos`
- 密码：`nacos`

#### 步骤 2：找到网关路由配置

1. 点击左侧菜单 **配置管理** → **配置列表**
2. 找到 `Data ID`: `jeecg-gateway-router.json`
3. `Group`: `DEFAULT_GROUP`
4. 点击右侧的 **编辑** 按钮

#### 步骤 3：添加 WebGame 路由

在现有的 JSON 数组中添加以下两个路由配置：

```json
{
  "id": "webgame",
  "order": 2,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/webgame/**"
    }
  }],
  "filters": [],
  "uri": "lb://jeecg-system"
},
{
  "id": "webgame-websocket",
  "order": 5,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/webgame/ws/**"
    }
  }],
  "filters": [],
  "uri": "lb:ws://jeecg-system"
}
```

#### 完整的配置示例

```json
[
  {
    "id": "jeecg-system",
    "order": 0,
    "predicates": [{
      "name": "Path",
      "args": {
        "_genkey_0": "/sys/**",
        "_genkey_1": "/jmreport/**",
        "_genkey_3": "/online/**",
        "_genkey_4": "/generic/**",
        "_genkey_5": "/oauth2/**",
        "_genkey_6": "/drag/**",
        "_genkey_7": "/actuator/**",
        "_genkey_8": "/airag/**",
        "_genkey_9": "/jimubi/**",
        "_genkey_10": "/openapi/**"
      }
    }],
    "filters": [],
    "uri": "lb://jeecg-system"
  },
  {
    "id": "jeecg-demo",
    "order": 1,
    "predicates": [{
      "name": "Path",
      "args": {
        "_genkey_0": "/mock/**",
        "_genkey_1": "/test/**",
        "_genkey_2": "/bigscreen/template1/**",
        "_genkey_3": "/bigscreen/template2/**"
      }
    }],
    "filters": [],
    "uri": "lb://jeecg-demo"
  },
  {
    "id": "webgame",
    "order": 2,
    "predicates": [{
      "name": "Path",
      "args": {
        "_genkey_0": "/webgame/**"
      }
    }],
    "filters": [],
    "uri": "lb://jeecg-system"
  },
  {
    "id": "jeecg-system-websocket",
    "order": 3,
    "predicates": [{
      "name": "Path",
      "args": {
        "_genkey_0": "/websocket/**",
        "_genkey_1": "/newsWebsocket/**"
      }
    }],
    "filters": [],
    "uri": "lb:ws://jeecg-system"
  },
  {
    "id": "jeecg-demo-websocket",
    "order": 4,
    "predicates": [{
      "name": "Path",
      "args": {
        "_genkey_0": "/vxeSocket/**"
      }
    }],
    "filters": [],
    "uri": "lb:ws://jeecg-demo"
  },
  {
    "id": "webgame-websocket",
    "order": 5,
    "predicates": [{
      "name": "Path",
      "args": {
        "_genkey_0": "/webgame/ws/**"
      }
    }],
    "filters": [],
    "uri": "lb:ws://jeecg-system"
  }
]
```

#### 步骤 4：发布配置

1. 点击底部的 **发布** 按钮
2. 确认配置内容无误
3. 等待几秒钟让网关自动刷新配置

---

### 方案二：通过 SQL 脚本配置

如果无法访问 Nacos 控制台，可以直接执行 SQL 脚本。

#### 步骤 1：执行 SQL 脚本

```bash
mysql -u root -p nacos < jeecg-boot/db/webgame_gateway_route.sql
```

或者在 MySQL 客户端中：

```sql
USE nacos;
SOURCE G:/Work/网页小游戏/网页小游戏后端/jeecg-boot/db/webgame_gateway_route.sql;
```

#### 步骤 2：重启网关服务

SQL 方式需要重启网关才能生效：

```bash
cd jeecg-server-cloud/jeecg-cloud-gateway
mvn spring-boot:run
```

---

## ✅ 验证配置

### 方法一：通过网关访问接口

配置完成后，应该可以通过网关访问 WebGame 接口：

```bash
# 直接访问（不经过网关）
curl http://localhost:8080/jeecg-boot/webgame/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456"}'

# 通过网关访问（推荐）
curl http://localhost:9999/webgame/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456"}'
```

两个请求应该返回相同的结果。

### 方法二：查看网关日志

启动网关后，查看日志中是否有类似输出：

```
Route defined: webgame -> lb://jeecg-system
Route defined: webgame-websocket -> lb:ws://jeecg-system
```

### 方法三：Nacos 控制台查看

1. 登录 Nacos 控制台
2. 进入 **配置列表**
3. 找到 `jeecg-gateway-router.json`
4. 点击 **详情** 查看是否包含 webgame 路由

---

## 🔧 路由配置说明

### 路由字段解释

| 字段 | 说明 | 示例 |
|------|------|------|
| `id` | 路由唯一标识 | `webgame` |
| `order` | 路由优先级（数字越小优先级越高） | `2` |
| `predicates` | 断言条件（匹配规则） | Path 路径匹配 |
| `filters` | 过滤器（可选） | 空数组表示不使用 |
| `uri` | 转发目标 | `lb://jeecg-system` |

### URI 协议说明

- `lb://jeecg-system` - 负载均衡转发到 jeecg-system 服务
- `lb:ws://jeecg-system` - WebSocket 负载均衡转发

### 路径匹配规则

- `/webgame/**` - 匹配所有以 `/webgame/` 开头的路径
- 例如：
  - `/webgame/auth/login` ✅
  - `/webgame/character/info` ✅
  - `/webgame/ws/chat` ✅ (WebSocket)

---

## 🚨 常见问题

### Q1: 配置后访问返回 404

**可能原因**:
1. 网关未刷新配置
2. jeecg-system 服务未启动
3. 路径拼写错误

**解决方法**:
```bash
# 1. 重启网关
cd jeecg-cloud-gateway
mvn spring-boot:run

# 2. 检查 jeecg-system 是否运行
curl http://localhost:8080/jeecg-boot/actuator/health

# 3. 测试直连是否正常
curl http://localhost:8080/jeecg-boot/webgame/auth/login
```

### Q2: WebSocket 连接失败

**检查项**:
1. WebSocket 路由是否正确配置
2. 前端连接地址是否为 `ws://localhost:9999/webgame/ws/...`
3. 后端 WebSocket 端点是否正确

### Q3: 跨域问题

网关已经配置了全局 CORS，理论上不需要额外配置。

如果仍有问题，检查网关的 `application.yml`:

```yaml
spring:
  cloud:
    gateway:
      globalcors:
        cors-configurations:
          '[/**]':
            allow-credentials: true
            allowed-origin-patterns: "*"
            allowed-methods: "*"
            allowed-headers: "*"
```

### Q4: 如何调整路由优先级？

修改 `order` 字段的值：
- 数字越小，优先级越高
- 建议保持现有顺序，将 webgame 放在中间位置

---

## 📝 微服务架构下的配置

如果你使用的是**微服务模式**（而非单体模式），需要：

### 1. 创建独立的 webgame 微服务

参考 `jeecg-demo-cloud-start` 创建 `webgame-cloud-start` 模块。

### 2. 修改路由配置

将 `uri` 改为指向新的微服务：

```json
{
  "id": "webgame",
  "order": 2,
  "predicates": [{
    "name": "Path",
    "args": {
      "_genkey_0": "/webgame/**"
    }
  }],
  "filters": [],
  "uri": "lb://webgame-service"
}
```

### 3. 注册到 Nacos

确保 webgame 微服务正确注册到 Nacos 服务发现中心。

---

## 🎯 当前架构说明

**目前采用的是单体架构**：
- WebGame 模块作为 `jeecg-system` 的一部分
- 通过网关路由到 `lb://jeecg-system`
- 所有 webgame 请求都会转发到 jeecg-system 服务

**优点**:
- 部署简单，只需启动一个服务
- 无需额外的服务间通信
- 适合初期开发和小型项目

**缺点**:
- 无法独立扩展 webgame 模块
- 与系统管理模块耦合

**未来优化方向**:
- 当用户量增长时，可以将 webgame 拆分为独立微服务
- 实现独立的水平扩展

---

## 📞 技术支持

如遇到问题，请检查：

1. **Nacos 配置是否正确**
   - Data ID: `jeecg-gateway-router.json`
   - Group: `DEFAULT_GROUP`
   - 内容包含 webgame 路由

2. **服务是否正常启动**
   ```bash
   # 检查 jeecg-system
   curl http://localhost:8080/jeecg-boot/actuator/health
   
   # 检查网关
   curl http://localhost:9999/actuator/gateway/routes
   ```

3. **数据库是否初始化**
   ```bash
   mysql -u root -p -e "USE webgame; SHOW TABLES;"
   ```

---

**配置完成后，记得重启网关服务！** 🚀
