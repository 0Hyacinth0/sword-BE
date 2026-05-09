# WebGame 模块开发指南

## 📋 概述

WebGame 模块采用**统一的认证架构**，所有接口自动支持 Token 验证和权限控制。

## 🔐 认证架构

### 1. Shiro 配置（系统级）

所有 `/webgame/**` 路径已配置为 `anon`（匿名访问），绕过 Shiro 系统级认证。

**配置文件**: `ShiroConfig.java`
```java
// WebGame 模块所有业务接口统一免 Shiro 认证（在业务层自行校验 Token）
filterChainDefinitionMap.put("/webgame/**", "anon");
```

### 2. 业务层认证（应用级）

在 Controller 中通过 `BaseWebGameController` 基类获取用户 ID，在 Service 层进行权限验证。

## 🚀 快速开始

### 创建新的 Controller

```java
@Slf4j
@Tag(name = "我的新模块")
@RestController
@RequestMapping("/webgame/mymodule")
public class MyModuleController extends BaseWebGameController {
    
    @Autowired
    private IMyModuleService myModuleService;
    
    @Operation(summary = "获取数据")
    @GetMapping("/data/{id}")
    public Result<MyDataVO> getData(HttpServletRequest request, @PathVariable String id) {
        try {
            // ✅ 一行代码获取当前用户ID
            String userId = getCurrentUserId(request);
            
            MyDataVO data = myModuleService.getData(id, userId);
            return Result.OK("获取成功", data);
        } catch (Exception e) {
            log.error("获取数据失败", e);
            return Result.error(e.getMessage());
        }
    }
}
```

### 创建 Service 接口

```java
public interface IMyModuleService {
    /**
     * 获取数据
     * @param id 数据ID
     * @param userId 用户ID（用于权限验证）
     * @return 数据VO
     */
    MyDataVO getData(String id, String userId);
}
```

### 创建 Service 实现

```java
@Service
public class MyModuleServiceImpl implements IMyModuleService {
    
    @Autowired
    private MyDataMapper myDataMapper;
    
    @Override
    public MyDataVO getData(String id, String userId) {
        // 1. 查询数据
        MyData data = myDataMapper.selectById(id);
        if (data == null) {
            throw new JeecgBootException("数据不存在");
        }
        
        // 2. ✅ 权限验证：确保只能访问自己的数据
        if (!data.getUserId().equals(userId)) {
            throw new JeecgBootException("无权访问该数据");
        }
        
        // 3. 返回数据
        return convertToVO(data);
    }
}
```

## 📦 核心组件

### 1. BaseWebGameController

**位置**: `org.jeecg.modules.webgame.common.controller.BaseWebGameController`

**功能**: 提供统一的 Token 解析方法

**使用方法**:
```java
public class MyController extends BaseWebGameController {
    // 继承后可直接使用
    String userId = getCurrentUserId(request);
}
```

### 2. WebGameTokenUtil

**位置**: `org.jeecg.modules.webgame.common.util.WebGameTokenUtil`

**功能**: Token 解析工具类

**主要方法**:
- `getUserIdFromRequest(HttpServletRequest)` - 从请求中获取用户ID
- `getUserIdFromToken(String)` - 从 Token 字符串中获取用户ID

## 🎯 最佳实践

### ✅ 推荐做法

1. **所有 Controller 继承 BaseWebGameController**
   ```java
   public class MyController extends BaseWebGameController
   ```

2. **Service 层接收 userId 参数进行权限验证**
   ```java
   public void doSomething(String id, String userId)
   ```

3. **验证数据归属权**
   ```java
   if (!data.getUserId().equals(userId)) {
       throw new JeecgBootException("无权操作");
   }
   ```

4. **DTO 中添加 userId 字段**
   ```java
   @Data
   public class MyDTO {
       private String userId; // 从 Token 中获取
       // ... 其他字段
   }
   ```

### ❌ 避免的做法

1. ~~在每个 Controller 中重复编写 Token 解析逻辑~~
2. ~~跳过权限验证直接操作数据~~
3. ~~将 userId 作为前端传参（应该从 Token 中获取）~~

## 📝 完整示例

### DTO
```java
@Data
public class UpdateItemDTO {
    @NotNull(message = "物品ID不能为空")
    private String itemId;
    
    private String name;
    
    /**用户ID（从 Token 中获取，用于权限验证）*/
    private String userId;
}
```

### Controller
```java
@PostMapping("/update")
public Result<Void> updateItem(HttpServletRequest request, 
                                @Validated @RequestBody UpdateItemDTO dto) {
    try {
        String userId = getCurrentUserId(request);
        dto.setUserId(userId);
        
        myService.updateItem(dto);
        return Result.OK("更新成功");
    } catch (Exception e) {
        log.error("更新失败", e);
        return Result.error(e.getMessage());
    }
}
```

### Service
```java
@Transactional(rollbackFor = Exception.class)
public void updateItem(UpdateItemDTO dto) {
    // 1. 查询数据
    Item item = itemMapper.selectById(dto.getItemId());
    if (item == null) {
        throw new JeecgBootException("物品不存在");
    }
    
    // 2. 权限验证
    if (!item.getUserId().equals(dto.getUserId())) {
        throw new JeecgBootException("无权操作该物品");
    }
    
    // 3. 更新数据
    item.setName(dto.getName());
    itemMapper.updateById(item);
}
```

## 🔍 常见问题

### Q: 为什么不在 Shiro 层做认证？

A: WebGame 使用自己的用户体系和 Token，与 JeecgBoot 后台管理系统分离。在业务层认证更灵活，可以：
- 自定义 Token 格式
- 灵活的权限控制
- 更好的性能（减少不必要的认证开销）

### Q: 如何测试接口？

A: 在请求 Header 中添加：
```
X-Access-Token: your_jwt_token_here
```

### Q: Token 过期怎么办？

A: Token 解析失败会抛出异常，前端捕获后引导用户重新登录。

## 📚 相关文档

- [背包系统-后端对接文档.md](../../背包系统-后端对接文档.md)
- [角色详情-后端对接文档.md](../../角色详情-后端对接文档.md)
- [消耗品与材料系统-后端对接文档.md](../../消耗品与材料系统-后端对接文档.md)
