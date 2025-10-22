#!/bin/bash

# 酒店预订系统部署验证脚本
# 服务器: 118.31.121.25
# 用途: 自动检查所有服务是否正常运行

set -e

echo "🔍 开始验证部署状态..."
echo "================================"
echo ""

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 计数器
PASS=0
FAIL=0

# 检查函数
check_pass() {
    echo -e "${GREEN}✅ PASS${NC}: $1"
    ((PASS++))
}

check_fail() {
    echo -e "${RED}❌ FAIL${NC}: $1"
    ((FAIL++))
}

check_warn() {
    echo -e "${YELLOW}⚠️  WARN${NC}: $1"
}

echo "📋 第1步: 检查Docker服务"
echo "--------------------------------"
if command -v docker &> /dev/null; then
    check_pass "Docker已安装"
    if systemctl is-active --quiet docker; then
        check_pass "Docker服务运行中"
    else
        check_fail "Docker服务未运行"
    fi
else
    check_fail "Docker未安装"
fi
echo ""

echo "📋 第2步: 检查Docker Compose"
echo "--------------------------------"
if command -v docker-compose &> /dev/null; then
    check_pass "Docker Compose已安装"
    VERSION=$(docker-compose version --short)
    echo "   版本: $VERSION"
else
    check_fail "Docker Compose未安装"
fi
echo ""

echo "📋 第3步: 检查配置文件"
echo "--------------------------------"
if [ -f "env.production" ]; then
    check_pass "env.production 文件存在"

    # 检查配置内容
    if grep -q "rm-bp19l0vw49988qi5b.mysql.rds.aliyuncs.com" env.production; then
        check_pass "RDS地址配置正确"
    else
        check_fail "RDS地址配置错误"
    fi

    if grep -q "RDS_USERNAME=wzx" env.production; then
        check_pass "数据库用户名配置正确"
    else
        check_fail "数据库用户名配置错误"
    fi

    # 检查文件权限
    PERM=$(stat -c "%a" env.production 2>/dev/null || stat -f "%A" env.production 2>/dev/null)
    if [ "$PERM" = "600" ]; then
        check_pass "env.production 文件权限正确 (600)"
    else
        check_warn "env.production 文件权限为 $PERM，建议设置为 600"
        echo "   执行: chmod 600 env.production"
    fi
else
    check_fail "env.production 文件不存在"
fi

if [ -f "docker-compose.yml" ]; then
    check_pass "docker-compose.yml 文件存在"
else
    check_fail "docker-compose.yml 文件不存在"
fi
echo ""

echo "📋 第4步: 检查容器状态"
echo "--------------------------------"
if docker ps &> /dev/null; then
    # 检查后端容器
    if docker ps | grep -q "hotel-backend"; then
        check_pass "后端容器运行中"

        # 检查健康状态
        BACKEND_HEALTH=$(docker inspect --format='{{.State.Health.Status}}' hotel-backend 2>/dev/null || echo "unknown")
        if [ "$BACKEND_HEALTH" = "healthy" ]; then
            check_pass "后端容器健康状态正常"
        elif [ "$BACKEND_HEALTH" = "starting" ]; then
            check_warn "后端容器启动中，请稍后再检查"
        else
            check_fail "后端容器健康检查失败 (状态: $BACKEND_HEALTH)"
        fi
    else
        check_fail "后端容器未运行"
    fi

    # 检查前端容器
    if docker ps | grep -q "hotel-frontend"; then
        check_pass "前端容器运行中"

        # 检查健康状态
        FRONTEND_HEALTH=$(docker inspect --format='{{.State.Health.Status}}' hotel-frontend 2>/dev/null || echo "unknown")
        if [ "$FRONTEND_HEALTH" = "healthy" ]; then
            check_pass "前端容器健康状态正常"
        elif [ "$FRONTEND_HEALTH" = "starting" ]; then
            check_warn "前端容器启动中，请稍后再检查"
        else
            check_fail "前端容器健康检查失败 (状态: $FRONTEND_HEALTH)"
        fi
    else
        check_fail "前端容器未运行"
    fi
else
    check_fail "无法访问Docker服务"
fi
echo ""

echo "📋 第5步: 检查端口监听"
echo "--------------------------------"
if netstat -tulpn 2>/dev/null | grep -q ":80 "; then
    check_pass "80端口正在监听 (HTTP)"
elif ss -tulpn 2>/dev/null | grep -q ":80 "; then
    check_pass "80端口正在监听 (HTTP)"
else
    check_fail "80端口未监听"
fi

if netstat -tulpn 2>/dev/null | grep -q ":8082 "; then
    check_pass "8082端口正在监听 (后端)"
elif ss -tulpn 2>/dev/null | grep -q ":8082 "; then
    check_pass "8082端口正在监听 (后端)"
else
    check_fail "8082端口未监听"
fi
echo ""

echo "📋 第6步: 测试服务响应"
echo "--------------------------------"
# 测试前端
if curl -s -o /dev/null -w "%{http_code}" http://localhost:80 | grep -q "200"; then
    check_pass "前端HTTP服务响应正常 (200 OK)"
else
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:80)
    check_fail "前端HTTP服务响应异常 (HTTP $HTTP_CODE)"
fi

# 测试后端健康检查
if curl -s http://localhost:8082/actuator/health | grep -q "UP"; then
    check_pass "后端健康检查接口正常"
else
    check_fail "后端健康检查接口异常"
fi

# 测试后端API
if curl -s http://localhost:8082/api/hotel/hotels &> /dev/null; then
    check_pass "后端API接口可访问"
else
    check_fail "后端API接口无法访问"
fi
echo ""

echo "📋 第7步: 检查数据库连接"
echo "--------------------------------"
# 检查后端日志中是否有数据库连接错误
if docker logs hotel-backend 2>&1 | grep -qi "communications link failure\|access denied\|connection refused"; then
    check_fail "后端日志显示数据库连接错误"
    echo "   请检查RDS白名单配置和密码"
else
    check_pass "后端日志无明显数据库连接错误"
fi

# 尝试从ECS连接RDS
echo "   正在测试RDS连接..."
if command -v mysql &> /dev/null; then
    if timeout 5 mysql -hrm-bp19l0vw49988qi5b.mysql.rds.aliyuncs.com -P3306 -uwzx -p'QWer741852963!' -e "SELECT 1" &> /dev/null; then
        check_pass "可以从ECS连接到RDS"
    else
        check_warn "无法从ECS连接到RDS（可能需要配置白名单）"
        echo "   请在阿里云RDS控制台添加ECS内网IP到白名单"
    fi
else
    check_warn "未安装MySQL客户端，跳过RDS连接测试"
    echo "   安装: yum install -y mysql 或 apt install -y mysql-client"
fi
echo ""

echo "📋 第8步: 检查防火墙配置"
echo "--------------------------------"
if command -v firewall-cmd &> /dev/null; then
    if systemctl is-active --quiet firewalld; then
        check_pass "防火墙服务运行中"

        if firewall-cmd --list-ports | grep -q "80/tcp"; then
            check_pass "防火墙已开放80端口"
        else
            check_warn "防火墙未开放80端口"
            echo "   执行: firewall-cmd --add-port=80/tcp --permanent && firewall-cmd --reload"
        fi

        if firewall-cmd --list-ports | grep -q "8082/tcp"; then
            check_warn "防火墙开放了8082端口（建议关闭对外访问）"
        else
            check_pass "防火墙未开放8082端口（正确）"
        fi
    else
        check_warn "防火墙服务未运行"
    fi
else
    check_warn "未安装firewalld，请手动检查防火墙配置"
fi
echo ""

echo "📋 第9步: 检查磁盘空间"
echo "--------------------------------"
DISK_USAGE=$(df -h / | awk 'NR==2 {print $5}' | sed 's/%//')
if [ "$DISK_USAGE" -lt 80 ]; then
    check_pass "磁盘空间充足 (已使用 ${DISK_USAGE}%)"
elif [ "$DISK_USAGE" -lt 90 ]; then
    check_warn "磁盘空间紧张 (已使用 ${DISK_USAGE}%)"
else
    check_fail "磁盘空间不足 (已使用 ${DISK_USAGE}%)"
fi

MEM_USAGE=$(free | awk 'NR==2 {printf "%.0f", $3/$2 * 100}')
if [ "$MEM_USAGE" -lt 80 ]; then
    check_pass "内存使用正常 (已使用 ${MEM_USAGE}%)"
elif [ "$MEM_USAGE" -lt 90 ]; then
    check_warn "内存使用较高 (已使用 ${MEM_USAGE}%)"
else
    check_fail "内存不足 (已使用 ${MEM_USAGE}%)"
fi
echo ""

echo "📋 第10步: 获取访问信息"
echo "--------------------------------"
# 获取公网IP
PUBLIC_IP=$(curl -s ifconfig.me 2>/dev/null || curl -s ipinfo.io/ip 2>/dev/null || echo "未知")
if [ "$PUBLIC_IP" = "118.31.121.25" ]; then
    check_pass "公网IP确认: $PUBLIC_IP"
else
    check_warn "公网IP: $PUBLIC_IP (预期: 118.31.121.25)"
fi

# 获取内网IP
PRIVATE_IP=$(ip addr show eth0 2>/dev/null | grep "inet " | awk '{print $2}' | cut -d/ -f1 || echo "未知")
echo "   内网IP: $PRIVATE_IP"
echo "   (请确保此IP已添加到RDS白名单)"
echo ""

# 汇总结果
echo "================================"
echo "📊 验证结果汇总"
echo "================================"
echo -e "${GREEN}通过检查: $PASS${NC}"
echo -e "${RED}失败检查: $FAIL${NC}"
echo ""

if [ $FAIL -eq 0 ]; then
    echo -e "${GREEN}🎉 恭喜！所有检查都通过了！${NC}"
    echo ""
    echo "✨ 访问信息："
    echo "   前端地址: http://118.31.121.25/"
    echo "   后端API: http://118.31.121.25/api/"
    echo ""
    echo "📝 下一步："
    echo "   1. 在浏览器访问: http://118.31.121.25/"
    echo "   2. 测试酒店查询功能"
    echo "   3. 检查浏览器控制台是否有错误"
    echo ""
elif [ $FAIL -le 3 ]; then
    echo -e "${YELLOW}⚠️  部分检查失败，但可能不影响使用${NC}"
    echo ""
    echo "建议操作："
    echo "   1. 查看失败项目的详细说明"
    echo "   2. 尝试访问 http://118.31.121.25/ 测试"
    echo "   3. 查看日志: docker-compose logs -f"
    echo ""
else
    echo -e "${RED}❌ 多个关键检查失败，部署可能有问题${NC}"
    echo ""
    echo "建议操作："
    echo "   1. 查看详细日志: docker-compose logs"
    echo "   2. 重启服务: docker-compose restart"
    echo "   3. 如果问题持续，重新部署: ./deploy.sh"
    echo ""
fi

echo "💡 常用管理命令："
echo "   查看日志: docker-compose logs -f"
echo "   查看状态: docker-compose ps"
echo "   重启服务: docker-compose restart"
echo "   停止服务: docker-compose down"
echo ""

exit $FAIL
