# ============================================
# WebGame 网关路由配置脚本 (PowerShell)
# 自动在 Nacos 中添加 webgame 路由
# ============================================

Write-Host "======================================" -ForegroundColor Cyan
Write-Host "WebGame 网关路由配置工具" -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan
Write-Host ""

# 检查 MySQL 是否在 PATH 中
$mysqlPath = Get-Command mysql -ErrorAction SilentlyContinue
if (-not $mysqlPath) {
    Write-Host "❌ 未找到 MySQL 命令，请确保 MySQL 已安装并添加到 PATH" -ForegroundColor Red
    Write-Host "或者手动通过 Nacos 控制台配置（推荐）" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Nacos 控制台地址: http://localhost:8848/nacos" -ForegroundColor Green
    Write-Host "默认账号密码: nacos / nacos" -ForegroundColor Green
    exit 1
}

Write-Host "✅ 检测到 MySQL" -ForegroundColor Green
Write-Host ""

# 提示用户输入密码
Write-Host "请输入 MySQL root 密码:" -ForegroundColor Yellow
$securePassword = Read-Host -AsSecureString
$password = [Runtime.InteropServices.Marshal]::PtrToStringAuto([Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePassword))

# 执行 SQL 脚本
Write-Host ""
Write-Host "正在执行网关路由配置..." -ForegroundColor Yellow

$sqlFile = "jeecg-boot\db\webgame_gateway_route.sql"
if (-not (Test-Path $sqlFile)) {
    Write-Host "❌ 找不到 SQL 文件: $sqlFile" -ForegroundColor Red
    exit 1
}

try {
    $result = & mysql -u root -p"$password" nacos < $sqlFile 2>&1
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "✅ 网关路由配置成功！" -ForegroundColor Green
        Write-Host ""
        Write-Host "下一步操作：" -ForegroundColor Cyan
        Write-Host "1. 重启网关服务（如果使用 SQL 方式）" -ForegroundColor White
        Write-Host "   cd jeecg-server-cloud\jeecg-cloud-gateway" -ForegroundColor Gray
        Write-Host "   mvn spring-boot:run" -ForegroundColor Gray
        Write-Host ""
        Write-Host "2. 验证配置是否生效：" -ForegroundColor White
        Write-Host "   curl http://localhost:9999/webgame/auth/login" -ForegroundColor Gray
        Write-Host ""
    } else {
        throw "MySQL 执行失败"
    }
} catch {
    Write-Host ""
    Write-Host "❌ 配置失败，请检查错误信息" -ForegroundColor Red
    Write-Host ""
    Write-Host "或者手动通过 Nacos 控制台配置（推荐）：" -ForegroundColor Yellow
    Write-Host "1. 访问 http://localhost:8848/nacos" -ForegroundColor White
    Write-Host "2. 登录（默认账号: nacos / nacos）" -ForegroundColor White
    Write-Host "3. 配置管理 -> 配置列表" -ForegroundColor White
    Write-Host "4. 找到 jeecg-gateway-router.json (DEFAULT_GROUP)" -ForegroundColor White
    Write-Host "5. 点击编辑，添加 webgame 路由配置" -ForegroundColor White
    Write-Host ""
    Write-Host "详细操作步骤请查看: WebGame网关配置指南.md" -ForegroundColor Green
    Write-Host ""
}
