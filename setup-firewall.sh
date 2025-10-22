#!/bin/bash

# 阿里云服务器防火墙配置脚本
# 适用于CentOS/RHEL/Alibaba Cloud Linux

echo "🔥 配置阿里云服务器防火墙..."

# 检查是否为root用户
if [ "$EUID" -ne 0 ]; then
    echo "❌ 请使用root用户运行此脚本"
    exit 1
fi

# 安装并启动firewalld
echo "📦 安装防火墙..."
yum install -y firewalld
systemctl start firewalld
systemctl enable firewalld

# 检查防火墙状态
if ! systemctl is-active --quiet firewalld; then
    echo "❌ 防火墙启动失败"
    exit 1
fi

echo "✅ 防火墙已启动"

# 配置防火墙规则
echo "🔧 配置防火墙规则..."

# 开放HTTP端口
firewall-cmd --add-port=80/tcp --permanent
echo "✅ 开放端口 80 (HTTP)"

# 开放HTTPS端口（可选）
firewall-cmd --add-port=443/tcp --permanent
echo "✅ 开放端口 443 (HTTPS)"

# 开放SSH端口
firewall-cmd --add-port=22/tcp --permanent
echo "✅ 开放端口 22 (SSH)"

# 关闭不必要的端口
firewall-cmd --remove-port=8082/tcp --permanent
firewall-cmd --remove-port=3306/tcp --permanent
echo "✅ 关闭端口 8082 (后端)"
echo "✅ 关闭端口 3306 (数据库)"

# 重新加载防火墙规则
firewall-cmd --reload

# 显示当前规则
echo ""
echo "📋 当前防火墙规则："
firewall-cmd --list-ports

echo ""
echo "🎉 防火墙配置完成！"
echo ""
echo "📱 访问信息："
echo "  - HTTP: http://your-server-ip:80"
echo "  - HTTPS: https://your-server-ip:443 (如配置SSL)"
echo ""
echo "🔒 安全说明："
echo "  - 后端服务(8082)不对外暴露"
echo "  - 数据库(3306)不对外暴露"
echo "  - 只开放必要的HTTP/HTTPS端口"
