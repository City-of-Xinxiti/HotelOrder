#!/bin/bash

# RDS数据库连接测试脚本
# 用于验证ECS服务器是否能正常连接到阿里云RDS

echo "🔍 阿里云RDS连接测试"
echo "================================"
echo ""

# RDS连接信息
RDS_HOST="rm-bp19l0vw49988qi5b.mysql.rds.aliyuncs.com"
RDS_PORT="3306"
RDS_USER="wzx"
RDS_PASSWORD="QWer741852963!"
RDS_DATABASE="hotelorder"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}📋 连接信息:${NC}"
echo "   RDS地址: $RDS_HOST"
echo "   端口: $RDS_PORT"
echo "   用户名: $RDS_USER"
echo "   数据库: $RDS_DATABASE"
echo ""

# 检查MySQL客户端是否安装
echo "第1步: 检查MySQL客户端..."
if ! command -v mysql &> /dev/null; then
    echo -e "${RED}❌ 未安装MySQL客户端${NC}"
    echo ""
    echo "请先安装MySQL客户端:"
    echo "  CentOS/RHEL: yum install -y mysql"
    echo "  Ubuntu/Debian: apt install -y mysql-client"
    echo "  Alibaba Cloud Linux: yum install -y mysql"
    exit 1
fi
echo -e "${GREEN}✅ MySQL客户端已安装${NC}"
MYSQL_VERSION=$(mysql --version)
echo "   版本: $MYSQL_VERSION"
echo ""

# 检查网络连通性
echo "第2步: 检查网络连通性..."
if ping -c 2 -W 3 $RDS_HOST &> /dev/null; then
    echo -e "${GREEN}✅ 网络连通正常${NC}"
else
    echo -e "${YELLOW}⚠️  无法ping通RDS地址（这是正常的，RDS通常禁用ICMP）${NC}"
fi
echo ""

# 检查端口连通性
echo "第3步: 检查端口连通性..."
if timeout 5 bash -c "cat < /dev/null > /dev/tcp/$RDS_HOST/$RDS_PORT" 2>/dev/null; then
    echo -e "${GREEN}✅ 端口 $RDS_PORT 可访问${NC}"
elif command -v nc &> /dev/null; then
    if nc -z -w 5 $RDS_HOST $RDS_PORT &> /dev/null; then
        echo -e "${GREEN}✅ 端口 $RDS_PORT 可访问${NC}"
    else
        echo -e "${RED}❌ 无法连接到端口 $RDS_PORT${NC}"
        echo ""
        echo "可能的原因:"
        echo "  1. RDS白名单未添加ECS内网IP"
        echo "  2. 安全组配置阻止了访问"
        echo "  3. 网络配置问题"
        exit 1
    fi
else
    echo -e "${YELLOW}⚠️  跳过端口检查（nc命令不可用）${NC}"
fi
echo ""

# 获取本机内网IP
echo "第4步: 获取ECS内网IP..."
PRIVATE_IP=$(ip addr show eth0 2>/dev/null | grep "inet " | awk '{print $2}' | cut -d/ -f1)
if [ -z "$PRIVATE_IP" ]; then
    PRIVATE_IP=$(hostname -I | awk '{print $1}')
fi
echo "   内网IP: $PRIVATE_IP"
echo -e "${YELLOW}   ⚠️  请确保此IP已添加到RDS白名单！${NC}"
echo ""

# 测试数据库连接
echo "第5步: 测试数据库连接..."
echo "正在连接到 $RDS_HOST ..."

# 使用mysql命令测试连接
if mysql -h"$RDS_HOST" -P"$RDS_PORT" -u"$RDS_USER" -p"$RDS_PASSWORD" -e "SELECT 1" &> /dev/null; then
    echo -e "${GREEN}✅ 数据库连接成功！${NC}"
    echo ""

    # 显示数据库信息
    echo "第6步: 查询数据库信息..."
    echo "----------------------------------------"

    # 查询MySQL版本
    echo "MySQL版本:"
    mysql -h"$RDS_HOST" -P"$RDS_PORT" -u"$RDS_USER" -p"$RDS_PASSWORD" -e "SELECT VERSION();" 2>/dev/null | grep -v "VERSION()"
    echo ""

    # 查询数据库列表
    echo "可用数据库:"
    mysql -h"$RDS_HOST" -P"$RDS_PORT" -u"$RDS_USER" -p"$RDS_PASSWORD" -e "SHOW DATABASES;" 2>/dev/null
    echo ""

    # 检查目标数据库是否存在
    DB_EXISTS=$(mysql -h"$RDS_HOST" -P"$RDS_PORT" -u"$RDS_USER" -p"$RDS_PASSWORD" -e "SHOW DATABASES LIKE '$RDS_DATABASE';" 2>/dev/null | grep -c "$RDS_DATABASE")
    if [ "$DB_EXISTS" -gt 0 ]; then
        echo -e "${GREEN}✅ 数据库 '$RDS_DATABASE' 存在${NC}"

        # 查询表列表
        echo ""
        echo "数据库 '$RDS_DATABASE' 中的表:"
        mysql -h"$RDS_HOST" -P"$RDS_PORT" -u"$RDS_USER" -p"$RDS_PASSWORD" "$RDS_DATABASE" -e "SHOW TABLES;" 2>/dev/null

        # 查询表数量
        TABLE_COUNT=$(mysql -h"$RDS_HOST" -P"$RDS_PORT" -u"$RDS_USER" -p"$RDS_PASSWORD" "$RDS_DATABASE" -e "SHOW TABLES;" 2>/dev/null | grep -cv "Tables_in_")
        echo ""
        echo "   共 $TABLE_COUNT 个表"

    else
        echo -e "${RED}❌ 数据库 '$RDS_DATABASE' 不存在${NC}"
        echo ""
        echo "创建数据库的命令:"
        echo "  mysql -h$RDS_HOST -P$RDS_PORT -u$RDS_USER -p"
        echo "  CREATE DATABASE $RDS_DATABASE DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
    fi

    echo ""
    echo "================================"
    echo -e "${GREEN}🎉 测试完成！连接正常！${NC}"
    echo "================================"
    echo ""
    echo "✨ 可以进行以下操作:"
    echo "   1. 执行部署脚本: ./deploy.sh"
    echo "   2. 启动服务: docker-compose up -d"
    echo ""

else
    echo -e "${RED}❌ 数据库连接失败！${NC}"
    echo ""
    echo "可能的原因:"
    echo "  1. ❌ RDS白名单未添加ECS内网IP ($PRIVATE_IP)"
    echo "  2. ❌ 用户名或密码错误"
    echo "  3. ❌ RDS实例未运行或已暂停"
    echo "  4. ❌ 网络安全组配置问题"
    echo ""
    echo "解决方案:"
    echo "  1. 登录阿里云RDS控制台"
    echo "  2. 找到实例: $RDS_HOST"
    echo "  3. 进入 '数据安全性' -> '白名单设置'"
    echo "  4. 添加ECS内网IP: $PRIVATE_IP/32"
    echo "  5. 等待1-2分钟后重试"
    echo ""
    echo "手动测试连接:"
    echo "  mysql -h$RDS_HOST -P$RDS_PORT -u$RDS_USER -p"
    echo ""
    exit 1
fi

echo "💡 JDBC连接字符串:"
echo "   jdbc:mysql://$RDS_HOST:$RDS_PORT/$RDS_DATABASE?useUnicode=true&characterEncoding=utf8&useSSL=true&serverTimezone=Asia/Shanghai"
echo ""

echo "💡 环境变量配置 (env.production):"
echo "   RDS_URL=jdbc:mysql://$RDS_HOST:$RDS_PORT/$RDS_DATABASE?useUnicode=true&characterEncoding=utf8&useSSL=true&serverTimezone=Asia/Shanghai"
echo "   RDS_USERNAME=$RDS_USER"
echo "   RDS_PASSWORD=QWer741852963%21"
echo "   (注意: 密码中的 ! 需要URL编码为 %21)"
echo ""

exit 0
