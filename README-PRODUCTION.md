# 酒店预订系统 - 生产环境部署指南

## 🚀 快速部署

### 1. 环境要求
- Docker 20.10+
- Docker Compose 2.0+
- 至少 2GB 内存
- 至少 10GB 磁盘空间

### 2. 部署步骤

```bash
# 1. 克隆项目
git clone <repository-url>
cd Hotel

# 2. 配置环境变量
cp env.production.example env.production
# 编辑 env.production 文件，设置数据库密码等

# 3. 执行部署
chmod +x deploy.sh
./deploy.sh
```

## 📋 配置说明

### 环境变量配置 (env.production)

```bash
# 数据库配置
MYSQL_ROOT_PASSWORD=your_secure_password_here
MYSQL_DATABASE=hotelorder
MYSQL_USER=hotel_user
MYSQL_PASSWORD=your_mysql_password_here
MYSQL_PORT=3306

# 服务端口配置
BACKEND_PORT=8082
FRONTEND_PORT=80

# 锦江API配置
JINJIANG_API_BASE_URL=https://bizfzout.bestwehotel.com/proxy/ms-corp-directly-connect
JINJIANG_APP_ID=924497e9-be62-4128-950d-f626657fcd64
JINJIANG_APP_SECRET=65b299b0-27cb-4360-b181-accf1034bef2
```

## 🏗️ 架构说明

### 服务组件
- **MySQL 8.0**: 数据库服务
- **Backend**: Spring Boot 应用 (端口 8082)
- **Frontend**: Nginx + React 应用 (端口 80)

### 网络架构
```
Internet → Nginx (Frontend) → Spring Boot (Backend) → MySQL
```

## 🔧 管理命令

### 基本操作
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

### 服务管理
```bash
# 重启特定服务
docker-compose restart backend
docker-compose restart frontend
docker-compose restart mysql

# 查看特定服务日志
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f mysql
```

### 数据库管理
```bash
# 进入数据库容器
docker exec -it hotel-mysql mysql -u root -p

# 备份数据库
docker exec hotel-mysql mysqldump -u root -p hotelorder > backup.sql

# 恢复数据库
docker exec -i hotel-mysql mysql -u root -p hotelorder < backup.sql
```

## 📊 监控和健康检查

### 健康检查端点
- 前端: `http://localhost/health`
- 后端: `http://localhost:8082/actuator/health`

### 日志位置
- 应用日志: `docker-compose logs [service_name]`
- Nginx日志: 容器内 `/var/log/nginx/`
- 数据库日志: `docker-compose logs mysql`

## 🔒 安全配置

### 生产环境安全建议

1. **修改默认密码**
   ```bash
   # 修改数据库密码
   MYSQL_ROOT_PASSWORD=your_very_secure_password
   MYSQL_PASSWORD=your_secure_app_password
   ```

2. **配置防火墙**
   ```bash
   # 只开放必要端口
   ufw allow 80
   ufw allow 443  # 如果使用HTTPS
   ufw deny 3306  # 禁止外部访问数据库
   ufw deny 8082  # 禁止外部访问后端
   ```

3. **使用HTTPS** (推荐)
   - 配置SSL证书
   - 修改Nginx配置支持HTTPS

## 🚨 故障排除

### 常见问题

1. **服务启动失败**
   ```bash
   # 检查日志
   docker-compose logs [service_name]
   
   # 检查端口占用
   netstat -tulpn | grep :80
   netstat -tulpn | grep :8082
   ```

2. **数据库连接失败**
   ```bash
   # 检查数据库状态
   docker-compose logs mysql
   
   # 检查网络连接
   docker exec hotel-backend ping mysql
   ```

3. **前端无法访问**
   ```bash
   # 检查Nginx配置
   docker exec hotel-frontend nginx -t
   
   # 重启前端服务
   docker-compose restart frontend
   ```

### 性能优化

1. **数据库优化**
   ```sql
   -- 创建索引
   CREATE INDEX idx_inn_id ON jinjiang_room_status(inn_id);
   CREATE INDEX idx_end_of_day ON jinjiang_room_status(end_of_day);
   ```

2. **应用优化**
   - 调整JVM参数
   - 配置连接池
   - 启用缓存

## 📈 扩展部署

### 水平扩展
```yaml
# docker-compose.override.yml
version: '3.8'
services:
  backend:
    deploy:
      replicas: 3
  frontend:
    deploy:
      replicas: 2
```

### 负载均衡
- 使用Nginx作为负载均衡器
- 配置多个后端实例
- 实现会话保持

## 🔄 更新部署

### 滚动更新
```bash
# 1. 拉取最新代码
git pull

# 2. 重新构建镜像
docker-compose build

# 3. 滚动更新
docker-compose up -d --no-deps backend
docker-compose up -d --no-deps frontend
```

## 📞 技术支持

如有问题，请检查：
1. 服务日志: `docker-compose logs`
2. 系统资源: `docker stats`
3. 网络连接: `docker network ls`
4. 数据持久化: `docker volume ls`

---

**注意**: 生产环境部署前请务必：
- 修改所有默认密码
- 配置防火墙规则
- 备份重要数据
- 测试所有功能
