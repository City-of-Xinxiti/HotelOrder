#!/bin/bash

# 酒店预订系统部署脚本
# 适用于阿里云服务器生产环境

set -e

echo "🚀 开始部署酒店预订系统..."

# 检查环境配置文件
if [ ! -f "env.production" ]; then
    echo "❌ 未找到生产环境配置文件 env.production"
    echo "请先创建 env.production 文件并配置相关参数"
    exit 1
fi

# 加载环境变量
set -a  # 自动导出所有变量
source env.production
set +a  # 关闭自动导出

echo "📋 当前配置："
echo "  - 数据库: 阿里云RDS (${RDS_URL})"
echo "  - 后端端口: ${BACKEND_PORT:-8082}"
echo "  - 前端端口: ${FRONTEND_PORT:-80}"
echo "  - 锦江API: ${JINJIANG_API_BASE_URL}"

# 停止现有服务
echo "🛑 停止现有服务..."
docker-compose down --remove-orphans

# 清理旧镜像（可选）
read -p "是否清理旧的Docker镜像？(y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "🧹 清理旧镜像..."
    docker system prune -f
fi

# 构建并启动服务
echo "🔨 构建并启动服务..."
docker-compose --env-file env.production up -d --build

# 等待服务启动
echo "⏳ 等待服务启动..."
sleep 30

# 检查服务状态
echo "🔍 检查服务状态..."
docker-compose ps

# 检查健康状态
echo "🏥 检查服务健康状态..."
for i in {1..30}; do
    if curl -f http://localhost:${FRONTEND_PORT:-80} > /dev/null 2>&1; then
        echo "✅ 前端服务健康"
        break
    fi
    echo "⏳ 等待前端服务启动... ($i/30)"
    sleep 2
done

for i in {1..30}; do
    if curl -f http://localhost:${BACKEND_PORT:-8082}/actuator/health > /dev/null 2>&1; then
        echo "✅ 后端服务健康"
        break
    fi
    echo "⏳ 等待后端服务启动... ($i/30)"
    sleep 2
done

# 获取服务器IP
SERVER_IP=$(curl -s ifconfig.me || curl -s ipinfo.io/ip || echo "localhost")

# 显示访问信息
echo ""
echo "🎉 部署完成！"
echo ""
echo "📱 访问信息："
echo "  - 前端地址: http://${SERVER_IP}:${FRONTEND_PORT:-80}"
echo "  - 后端地址: http://${SERVER_IP}:${BACKEND_PORT:-8082}"
echo "  - 数据库端口: ${MYSQL_PORT:-3306}"
echo ""
echo "🌐 阿里云安全组配置："
echo "  - 开放端口 ${FRONTEND_PORT:-80} (HTTP)"
echo "  - 开放端口 443 (HTTPS，如需要)"
echo "  - 后端端口 ${BACKEND_PORT:-8082} (内部通信，无需对外开放)"
echo "  - 数据库使用阿里云RDS，请确保ECS能访问RDS"
echo ""
echo "📊 管理命令："
echo "  - 查看日志: docker-compose logs -f"
echo "  - 停止服务: docker-compose down"
echo "  - 重启服务: docker-compose restart"
echo "  - 查看状态: docker-compose ps"
echo ""
echo "🔧 故障排除："
echo "  - 查看详细日志: docker-compose logs [service_name]"
echo "  - 进入容器: docker exec -it [container_name] /bin/sh"
echo "  - 重启特定服务: docker-compose restart [service_name]"
