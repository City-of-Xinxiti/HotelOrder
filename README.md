# 🏨 酒店预订系统 (Hotel Order System)

[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?logo=docker)](https://www.docker.com/)
[![阿里云](https://img.shields.io/badge/阿里云-ECS+RDS-FF6A00?logo=alibaba-cloud)](https://www.aliyun.com/)
[![Status](https://img.shields.io/badge/Status-Production%20Ready-success)]()

一个基于 Spring Boot + React 的酒店预订系统，支持与锦江酒店API对接，可快速部署到阿里云。

## 🚀 快速开始

### 服务器信息
- **ECS公网IP**: 118.31.121.25
- **RDS数据库**: rm-bp19l0vw49988qi5b.mysql.rds.aliyuncs.com
- **访问地址**: http://118.31.121.25/

### 一键部署

```bash
# 1. SSH连接到服务器
ssh root@118.31.121.25

# 2. 克隆项目
git clone <your-repo-url> HotelOrder
cd HotelOrder

# 3. 测试RDS连接
chmod +x test-rds-connection.sh
./test-rds-connection.sh

# 4. 一键部署
chmod +x deploy.sh
./deploy.sh

# 5. 验证部署
chmod +x verify-deployment.sh
./verify-deployment.sh
```

**详细部署指南**: 请查看 [快速部署指南](DEPLOY-QUICK-START.md)

---

## 📦 项目架构

### 技术栈

**后端**:
- Spring Boot 3.x
- MyBatis Plus
- MySQL 8.0 (阿里云RDS)
- Java 21

**前端**:
- React 18
- Vite
- TailwindCSS
- Axios

**部署**:
- Docker + Docker Compose
- Nginx (反向代理)
- 阿里云 ECS + RDS

### 系统架构

```
Internet
   ↓
[ECS 118.31.121.25]
   ↓
[Nginx :80] → 前端静态文件
   ↓
[Nginx Proxy /api/*] → [Backend :8082]
   ↓
[阿里云 RDS MySQL]
```

---

## ✅ 配置修复说明

### 已修复的关键问题

本项目已完成生产环境配置优化，修复了以下关键问题：

1. **✅ RDS数据库配置** - 已更新为实际的阿里云RDS连接信息
2. **✅ 前端API地址** - 修复了硬编码问题，支持生产/开发环境自动切换
3. **✅ Nginx代理路径** - 修正了API代理配置，确保路径正确转发
4. **✅ Docker健康检查** - 后端容器健康检查已修复
5. **✅ 部署脚本优化** - 适配阿里云RDS环境

**详细修复说明**: 请查看 [Docker修复文档](DOCKER-FIXES.md)

---

## 📋 部署前准备

### 1. 阿里云安全组配置

| 协议 | 端口 | 授权对象 | 说明 |
|-----|------|---------|------|
| TCP | 80 | 0.0.0.0/0 | HTTP访问 |
| TCP | 22 | 你的IP/32 | SSH管理 |
| ❌ | 8082 | 禁止 | 后端端口（内部通信） |
| ❌ | 3306 | 禁止 | 数据库（使用RDS） |

### 2. RDS白名单配置

1. 登录阿里云RDS控制台
2. 找到实例: `rm-bp19l0vw49988qi5b`
3. 进入 **数据安全性** → **白名单设置**
4. 添加ECS的**内网IP**（不是公网IP）
   - 获取内网IP: `ip addr show eth0 | grep inet`
   - 格式: `172.x.x.x/32`

### 3. 数据库准备

```sql
-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS hotelorder 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- 验证连接
mysql -hrm-bp19l0vw49988qi5b.mysql.rds.aliyuncs.com -P3306 -uwzx -p
```

---

## 🔍 验证部署

### 自动验证

```bash
./verify-deployment.sh
```

### 手动验证

```bash
# 1. 检查服务状态
docker-compose ps

# 2. 查看日志
docker-compose logs -f

# 3. 测试前端
curl http://localhost:80

# 4. 测试后端
curl http://localhost:8082/actuator/health

# 5. 浏览器访问
http://118.31.121.25/
```

---

## 🛠️ 常用命令

### 服务管理
```bash
# 启动服务
docker-compose up -d

# 停止服务
docker-compose down

# 重启服务
docker-compose restart

# 查看状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

### 更新部署
```bash
# 拉取最新代码
git pull

# 重新构建并部署
docker-compose down
docker-compose up -d --build

# 查看启动日志
docker-compose logs -f
```

### 故障排查
```bash
# 查看后端日志
docker-compose logs backend | grep -i error

# 进入容器调试
docker exec -it hotel-backend sh

# 查看资源使用
docker stats
```

---

## 📚 文档索引

| 文档 | 说明 |
|-----|------|
| [快速部署指南](DEPLOY-QUICK-START.md) | 针对 118.31.121.25 的快速部署步骤 |
| [部署总结](README-DEPLOYMENT-SUMMARY.md) | 完整的部署检查清单和管理命令 |
| [Docker修复说明](DOCKER-FIXES.md) | Docker配置问题详细说明和解决方案 |
| [安全注意事项](SECURITY-NOTES.md) | 生产环境安全配置指南 |
| [阿里云部署](README-ALIYUN.md) | 阿里云完整部署指南 |
| [生产环境说明](README-PRODUCTION.md) | 生产环境配置和维护 |

---

## 🔐 安全建议

### 必须执行
- ✅ 限制SSH端口(22)仅对你的IP开放
- ✅ 不要开放8082、3306等内部端口
- ✅ 设置 `env.production` 文件权限为 600
- ✅ 定期备份RDS数据库

### 建议执行
- 🔄 修改RDS默认密码为更强的密码
- 🔄 配置HTTPS证书
- 🔄 启用阿里云DDoS防护
- 🔄 设置监控告警

**详细安全配置**: 请查看 [安全注意事项](SECURITY-NOTES.md)

---

## 🎯 功能特性

### 已实现功能
- ✅ 酒店列表查询
- ✅ 酒店详情查看
- ✅ 房型信息展示
- ✅ 实时房态查询
- ✅ 订单管理
- ✅ 锦江API对接

### 规划中功能
- 🚧 在线支付
- 🚧 用户认证
- 🚧 订单评价
- 🚧 数据统计

---

## 🐛 故障排查

### 常见问题

**问题1: 无法访问 http://118.31.121.25/**
- 检查阿里云安全组是否开放80端口
- 检查 `docker-compose ps` 服务是否运行
- 检查防火墙: `firewall-cmd --list-ports`

**问题2: API请求失败**
- 检查后端日志: `docker-compose logs backend`
- 检查RDS连接: `./test-rds-connection.sh`
- 检查Nginx代理配置

**问题3: 数据库连接失败**
- 确认RDS白名单包含ECS内网IP
- 确认密码正确（注意特殊字符）
- 测试连接: `mysql -hrm-bp19l0vw49988qi5b... -uwzx -p`

**更多问题**: 请查看 [Docker修复文档](DOCKER-FIXES.md)

---

## 📞 技术支持

### 自助排查
1. 运行验证脚本: `./verify-deployment.sh`
2. 查看日志: `docker-compose logs -f`
3. 参考文档: [故障排查](DOCKER-FIXES.md#故障排查)

### 阿里云支持
- 客服电话: 95187
- 工单系统: https://workorder.console.aliyun.com/

---

## 📝 更新日志

### v1.0.0 (2024)
- ✅ 完成Docker配置修复
- ✅ 适配阿里云RDS环境
- ✅ 修复前端API地址硬编码
- ✅ 修复Nginx代理路径
- ✅ 优化部署脚本
- ✅ 添加自动化验证脚本
- ✅ 完善文档体系

---

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

### 开发环境

**后端**:
```bash
cd HotelBack
./gradlew bootRun
```

**前端**:
```bash
cd HotelOrder
npm install
npm run dev
```

---

## 📄 许可证

[MIT License](LICENSE)

---

## 🎉 致谢

- 感谢锦江酒店提供API支持
- 感谢阿里云提供云服务
- 感谢所有贡献者

---

**部署状态**: ✅ 生产就绪  
**最后更新**: 2024年  
**维护者**: 开发团队  

🚀 **快速开始**: [DEPLOY-QUICK-START.md](DEPLOY-QUICK-START.md)