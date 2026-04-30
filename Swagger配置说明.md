# Swagger 接口文档配置指南

## ❓ 问题描述

启动项目后，访问 Swagger/Knife4j 文档（http://localhost:8080/jeecg-boot/doc.html），**看不到 WebGame 模块的接口**。

---

## 🔍 问题原因

WebGame 模块的 Controller 已经正确添加了 Swagger 注解（`@Tag` 和 `@Operation`），但 **Spring Boot 启动类没有扫描到 WebGame 模块的包**。

JeecgBoot 默认只扫描以下包：
- `org.jeecg.modules.system.*`
- `org.jeecg.modules.demo.*`
- `org.jeecg.modules.airag.*`

**WebGame 模块的包路径是 `org.jeecg.modules.webgame.*`，不在默认扫描范围内！**

---

## ✅ 解决方案

### 方案一：在启动类中导入配置（已修复）✅

我已经修改了启动类 `JeecgSystemApplication.java`，添加了 `@Import` 注解：

```java
@Import({WebGameConfiguration.class})  // 导入 WebGame 模块配置
public class JeecgSystemApplication extends SpringBootServletInitializer {
    // ...
}
```

`WebGameConfiguration` 配置类中已经包含了组件扫描：

```java
@Configuration
@ComponentScan(basePackages = {"org.jeecg.modules.webgame"})
public class WebGameConfiguration {
}
```

**这样 Spring 就会扫描 webgame 包下的所有 Controller、Service 等组件。**

---

### 方案二：修改启动类的 @ComponentScan（备选）

如果方案一不生效，可以直接修改启动类的扫描范围：

```java
@SpringBootApplication(
    exclude = MongoAutoConfiguration.class,
    scanBasePackages = {
        "org.jeecg",  // 扫描所有 org.jeecg 包
        "org.jeecg.modules.webgame"  // 显式扫描 webgame
    }
)
```

---

## 🚀 验证步骤

### 1. 重新编译项目

```bash
cd jeecg-boot
mvn clean install -DskipTests
```

### 2. 重启服务

```bash
cd jeecg-module-system/jeecg-system-start
mvn spring-boot:run
```

或者在 IDE 中重新启动 `JeecgSystemApplication`。

### 3. 访问 Swagger 文档

浏览器打开：http://localhost:8080/jeecg-boot/doc.html

### 4. 检查接口列表

在左侧菜单中应该能看到：

- ✅ **游戏认证接口** (webgame/auth)
  - POST /webgame/auth/login
  - POST /webgame/auth/register
  - POST /webgame/auth/logout

后续实现其他模块后，还会看到：
- 🔄 角色管理接口 (webgame/character)
- 🔄 物品管理接口 (webgame/item)
- 🔄 战斗系统接口 (webgame/battle)
- 🔄 副本系统接口 (webgame/dungeon)
- 🔄 PVP竞技接口 (webgame/pvp)
- 🔄 社交系统接口 (webgame/social)

---

## 📝 Swagger 注解说明

WebGame 模块的 Controller 已经使用了标准的 OpenAPI 3.0 注解：

### @Tag - 接口分组

```java
@Tag(name = "游戏认证接口")
@RestController
@RequestMapping("/webgame/auth")
public class WgAuthController {
    // ...
}
```

这会在 Swagger UI 中创建一个名为 "游戏认证接口" 的分组。

### @Operation - 接口描述

```java
@Operation(summary = "用户登录")
@PostMapping("/login")
public Result<LoginVO> login(@Validated @RequestBody LoginDTO loginDTO) {
    // ...
}
```

这会为接口添加简短的描述信息。

### 完整的注解示例

```java
@Slf4j
@Tag(name = "游戏认证接口")
@RestController
@RequestMapping("/webgame/auth")
public class WgAuthController {

    @Autowired
    private IWgUserService wgUserService;

    /**
     * 用户登录
     * @param loginDTO 登录参数
     * @return 登录信息(包含token)
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Validated @RequestBody LoginDTO loginDTO) {
        try {
            LoginVO loginVO = wgUserService.login(loginDTO);
            return Result.OK("登录成功", loginVO);
        } catch (Exception e) {
            log.error("登录失败", e);
            return Result.error(e.getMessage());
        }
    }
}
```

---

## 🔧 Knife4j 配置说明

JeecgBoot 使用的是 **Knife4j**（Swagger 的增强版），配置文件在：

### application.yml

```yaml
knife4j:
  production: false  # 开发环境设置为 false，生产环境设置为 true
  enable: true       # 启用 Knife4j
```

### Swagger3Config.java

位置：`jeecg-boot-base-core/src/main/java/org/jeecg/config/Swagger3Config.java`

关键配置：

```java
@Configuration
@ConditionalOnProperty(prefix = "knife4j", name = "production", havingValue = "false", matchIfMissing = true)
public class Swagger3Config implements WebMvcConfigurer {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("JeecgBoot API文档")
                .version("3.9.1")
                .description("JeecgBoot 前后端分离架构文档"));
    }
}
```

---

## ⚠️ 常见问题

### Q1: 重启后还是看不到接口

**检查项**:

1. **确认代码已重新编译**
   ```bash
   mvn clean install -DskipTests
   ```

2. **确认启动类已修改**
   检查 `JeecgSystemApplication.java` 是否包含：
   ```java
   @Import({WebGameConfiguration.class})
   ```

3. **查看启动日志**
   启动时应该有类似输出：
   ```
   Mapped "{[/webgame/auth/login],methods=[POST]}" onto ...
   Mapped "{[/webgame/auth/register],methods=[POST]}" onto ...
   ```

4. **检查包扫描**
   在启动类中添加临时日志：
   ```java
   @PostConstruct
   public void checkScan() {
       log.info("扫描到的 Controller: {}", 
           applicationContext.getBeansWithAnnotation(RestController.class).keySet());
   }
   ```

### Q2: 接口显示但没有详细描述

**原因**: 缺少 `@Operation` 注解或注解格式错误

**解决**: 确保每个接口方法都有 `@Operation` 注解：

```java
@Operation(summary = "用户登录", description = "用户通过用户名和密码登录系统")
@PostMapping("/login")
public Result<LoginVO> login(...) {
    // ...
}
```

### Q3: 请求参数没有显示

**原因**: DTO 类缺少字段注释或校验注解

**解决**: 

1. 在 DTO 字段上添加注释：
   ```java
   @Data
   public class LoginDTO {
       /**用户名*/
       @NotBlank(message = "用户名不能为空")
       private String username;
       
       /**密码*/
       @NotBlank(message = "密码不能为空")
       private String password;
   }
   ```

2. 使用 `@Parameter` 注解（可选）：
   ```java
   @Operation(summary = "用户登录")
   @PostMapping("/login")
   public Result<LoginVO> login(
       @Parameter(description = "登录参数", required = true) 
       @Validated @RequestBody LoginDTO loginDTO
   ) {
       // ...
   }
   ```

### Q4: 响应数据结构不清晰

**解决**: 在 VO 类中添加字段注释：

```java
@Data
@Schema(description = "登录响应VO")
public class LoginVO {
    @Schema(description = "用户ID")
    private String id;
    
    @Schema(description = "用户名")
    private String username;
    
    @Schema(description = "JWT Token")
    private String token;
}
```

---

## 🎯 最佳实践

### 1. 统一的注解风格

所有 Controller 都应该包含：

```java
@Slf4j
@Tag(name = "模块名称")
@RestController
@RequestMapping("/模块路径")
public class XxxController {
    
    @Operation(summary = "接口功能简述")
    @PostMapping("/具体路径")
    public Result<XxxVO> method(@Validated @RequestBody XxxDTO dto) {
        // ...
    }
}
```

### 2. 详细的接口描述

```java
@Operation(
    summary = "创建角色",
    description = "根据用户选择的职业创建新角色，初始化角色属性",
    tags = {"角色管理"}
)
```

### 3. 参数校验注解

```java
@Data
public class CreateCharacterDTO {
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 1, max = 20, message = "角色名称长度1-20个字符")
    private String characterName;
    
    @NotNull(message = "职业类型不能为空")
    @Min(value = 1, message = "职业类型无效")
    @Max(value = 3, message = "职业类型无效")
    private Integer profession;
}
```

### 4. 响应示例

```java
@Operation(summary = "用户登录")
@PostMapping("/login")
@ApiResponse(responseCode = "200", description = "登录成功", 
    content = @Content(schema = @Schema(implementation = LoginVO.class)))
public Result<LoginVO> login(...) {
    // ...
}
```

---

## 📊 Swagger vs Knife4j

| 特性 | Swagger | Knife4j |
|------|---------|---------|
| UI 美观度 | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| 离线文档 | ❌ | ✅ |
| 接口搜索 | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| 参数调试 | ✅ | ✅ |
| 中文支持 | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| 个性化定制 | ⭐⭐⭐ | ⭐⭐⭐⭐ |

**JeecgBoot 推荐使用 Knife4j**，功能更强大，界面更友好。

---

## 🔗 相关文档

- [Knife4j 官方文档](https://doc.xiaominfo.com/)
- [OpenAPI 3.0 规范](https://swagger.io/specification/)
- [JeecgBoot 文档](https://doc.jeecg.com/)

---

## ✅ 检查清单

配置完成后，请确认：

- [x] 启动类已添加 `@Import({WebGameConfiguration.class})`
- [x] WebGameConfiguration 包含 `@ComponentScan`
- [x] Controller 已添加 `@Tag` 注解
- [x] 接口方法已添加 `@Operation` 注解
- [x] DTO/VO 类字段有注释
- [x] 项目已重新编译
- [x] 服务已重启
- [x] 访问 http://localhost:8080/jeecg-boot/doc.html 能看到接口

---

**配置完成后，Swagger 文档就能正常显示 WebGame 模块的接口了！** 🎉
