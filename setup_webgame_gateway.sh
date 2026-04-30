#!/bin/bash
# ============================================
# WebGame 网关路由配置脚本
# 自动在 Nacos 中添加 webgame 路由
# ============================================

echo "======================================"
echo "WebGame 网关路由配置工具"
echo "======================================"
echo ""

# 检查 MySQL 连接
echo "正在检查 MySQL 连接..."
mysql -u root -p -e "SELECT 1;" > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "❌ MySQL 连接失败，请检查用户名和密码"
    exit 1
fi
echo "✅ MySQL 连接成功"
echo ""

# 提示用户输入密码
read -sp "请输入 MySQL root 密码: " MYSQL_PASSWORD
echo ""

# 执行 SQL 脚本
echo "正在执行网关路由配置..."
mysql -u root -p"$MYSQL_PASSWORD" nacos < jeecg-boot/db/webgame_gateway_route.sql

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ 网关路由配置成功！"
    echo ""
    echo "下一步操作："
    echo "1. 重启网关服务（如果使用 SQL 方式）"
    echo "   cd jeecg-server-cloud/jeecg-cloud-gateway"
    echo "   mvn spring-boot:run"
    echo ""
    echo "2. 验证配置是否生效："
    echo "   curl http://localhost:9999/webgame/auth/login"
    echo ""
else
    echo ""
    echo "❌ 配置失败，请检查错误信息"
    echo ""
    echo "或者手动通过 Nacos 控制台配置："
    echo "1. 访问 http://localhost:8848/nacos"
    echo "2. 找到 jeecg-gateway-router.json"
    echo "3. 添加 webgame 路由配置"
    echo ""
fi
