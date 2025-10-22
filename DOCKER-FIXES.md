# 🔧 Docker配置问题修复总结

## 📋 修复概览

本次修复解决了项目部署到阿里云ECS服务器时的关键配置问题，确保与阿里云RDS数据库正确集成。

---

## ✅ 已修复的问题

### 1. **RDS数据库连接配置** ⚠️ 严重

**问题描述：**
- `env.production` 文件中的数据库配置仍是示例数据
- 未配置实际的阿里云RDS连接信息
- 会导致后端服务无法连接数据库，启动失败

**修复内容：**
```bash
# 修改前
RDS_URL=jdbc:mysql://your-rds-endpoint.mysql.rds.aliyuncs.com:3306/hotelorder...
RDS_USERNAME=your_rds_username
RDS_PASSWORD=your_rds_password

# 修改后
RDS_URL=jdbc:mysql://rm-bp19l0vw49988qi5b.mysql.rds.aliyuncs.com:3306/hotelorder?useUnicode=true&characterEncoding=utf8&useSSL=true&serverTimezone=Asia/Shanghai
RDS_USERNAME=wzx
RDS_PASSWORD=QWer741852963%21
```

**注意：** 密码中的特殊字符 `!` 已正确编码为 `%21`

---

### 2. **前端API地址硬编码** ⚠️ 严重

**问题描述：**
- `api.js` 中多处硬编码 `http://localhost:8082`
- 生产环境会直接调用 `localhost`，导致API请求失败
- 影响范围：订单管理、酒店图片、房态查询等功能

**修复内容：**
- 统一使用环境变量判断
- 开发环境：`http://localhost:8082/api`
- 生产环境：`/api`（通过Nginx代理）

**修复文件：**
- `HotelOrder/src/services/api.js`

**关键修改：**
```javascript
// 修改前
const response = await fetch(`http://localhost:8082/api/orders`, {...});

// 修改后
const BASE_URL = process.env.NODE_ENV === 'production' 
  ? '/api' 
  : 'http://localhost:8082/api';
const response = await fetch(`${BASE_URL}/orders`, {...});
```

---

### 3. **Nginx代理路径配置错误** ⚠️ 严重

**问题描述：**
- Nginx配置 `proxy_pass http://backend:8082;` 会导致路径丢失
- 请求 `/api/hotel/hotels` → 代理到 `http://backend:8082/hotel/hotels`
- 缺少 `/api` 前缀，后端无法识别

**修复内容：**
```nginx
# 修改前
location /api/ {
    proxy_pass http://backend:8082;
}

# 修改后
location /api/ {
    proxy_pass http://backend:8082/api/;
}
```

**验证方法：**
```bash
# 前端请求
curl http://your-server-ip/api/hotel/hotels

# Nginx代理到
curl http://backend:8082/api/hotel/hotels  ✅ 正确
```

---

### 4. **后端健康检查失败** ⚠️ 中等

**问题描述：**
- Dockerfile 使用 `curl` 进行健康检查
- `openjdk:21-jre-slim` 镜像不包含 curl
- 健康检查会一直失败，影响容器状态监控

**修复内容：**
```dockerfile
# 修改前
FROM openjdk:21-jre-slim

# 修改后
FROM openjdk:21-jre-slim

# 安装curl用于健康检查
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
```

**影响：**
- Docker Compose 健康检查正常工作
- 可以正确监控后端服务状态

---

### 5. **部署脚本优化** ⚠️ 低

**问题描述：**
- `deploy.sh` 仍在检查 MySQL 相关配置
- 使用 RDS 时不需要本地 MySQL 容器

**修复内容：**
```bash
# 修改前
echo "  - 数据库: ${MYSQL_DATABASE:-hotelorder}"
echo "  - 关闭端口 ${MYSQL_PORT:-3306} (数据库不对外)"

# 修改后
echo "  - 数据库: 阿里云RDS (${RDS_URL})"
echo "  - 数据库使用阿里云RDS，请确保ECS能访问RDS"
```

---

## 🏗️ 架构说明

### 当前部署架构

```
                     Internet
                        |
                        ↓
              [阿里云ECS服务器]
                        |
        ┌───────────────┼───────────────┐
        |               |               |
   [Frontend]      [Backend]       [Nginx]
   Container      Container       (80端口)
        |               |               |
        └───────────────┴───────────────┘
                        |
                        ↓
              [阿里云RDS MySQL]
         (rm-bp19l0vw49988qi5b...)
```

### 请求流程

1. **用户访问** → `http://your-server-ip/`
   - Nginx 返回前端静态文件

2. **前端API请求** → `http://your-server-ip/api/hotel/hotels`
   - Nginx 代理到 → `http://backend:8082/api/hotel/hotels`
   - 后端处理请求

3. **后端数据库查询** → 阿里云RDS
   - 使用内网连接（VPC内）
   - 连接信息从环境变量读取

---

## 🚀 部署步骤

### 1. 准备工作

```bash
# SSH连接到ECS服务器
ssh root@your-server-ip

# 克隆或上传项目代码
git clone <your-repo-url>
cd HotelOrder
```

### 2. 配置文件检查

```bash
# 确认 env.production 配置正确
cat env.production

# 应该看到：
# RDS_URL=jdbc:mysql://rm-bp19l0vw49988qi5b.mysql.rds.aliyuncs.com:3306/hotelorder...
# RDS_USERNAME=wzx
# RDS_PASSWORD=QWer741852963%21
```

### 3. 测试RDS连接

```bash
# 在ECS上测试能否连接RDS
mysql -hrm-bp19l0vw49988qi5b.mysql.rds.aliyuncs.com -P3306 -uwzx -p

# 输入密码：QWer741852963!
# 如果连接成功，说明网络配置正确
```

### 4. 执行部署

```bash
# 给脚本执行权限
chmod +x deploy.sh

# 执行部署（会自动安装Docker和Docker Compose）
./deploy.sh

# 部署过程会：
# 1. 检查并安装Docker
# 2. 检查并安装Docker Compose
# 3. 构建前后端镜像
# 4. 启动所有服务
# 5. 等待服务健康检查通过
```

### 5. 验证部署

```bash
# 查看服务状态
docker-compose ps

# 应该看到：
# hotel-backend    Up (healthy)
# hotel-frontend   Up (healthy)

# 查看日志
docker-compose logs -f

# 测试前端访问
curl http://localhost:80

# 测试后端健康检查
curl http://localhost:8082/actuator/health
```

### 6. 配置阿里云安全组

在阿里云ECS控制台配置：

**入方向规则：**
- TCP 80：`0.0.0.0/0`（HTTP访问）
- TCP 22：`你的IP地址`（SSH管理，不要开放给所有人）
- ❌ **不要**开放 8082 端口

**出方向规则：**
- 默认全部允许（确保能访问RDS）

### 7. RDS白名单配置

在阿里云RDS控制台：
1. 进入数据安全性 → 白名单设置
2. 添加ECS的**内网IP**（不是公网IP）
3. 格式：`172.x.x.x/32`

---

## 🔍 故障排查

### 问题1：后端无法启动

**症状：**
```bash
docker-compose logs backend
# 看到 "Connection refused" 或 "Access denied"
```

**排查步骤：**
```bash
# 1. 检查RDS连接信息
cat env.production | grep RDS

# 2. 在容器内测试连接
docker exec -it hotel-backend sh
# 查看环境变量
env | grep SPRING_DATASOURCE

# 3. 测试RDS连接（从ECS）
mysql -hrm-bp19l0vw49988qi5b.mysql.rds.aliyuncs.com -P3306 -uwzx -p
```

**可能原因：**
- ❌ RDS白名单未添加ECS内网IP
- ❌ 密码错误或未正确URL编码
- ❌ 数据库名称错误
- ❌ 网络安全组限制

---

### 问题2：前端访问后端API失败

**症状：**
```javascript
// 浏览器控制台
Failed to fetch: http://your-ip/api/hotel/hotels
```

**排查步骤：**
```bash
# 1. 检查Nginx代理配置
docker exec -it hotel-frontend cat /etc/nginx/nginx.conf | grep proxy_pass

# 应该看到：
# proxy_pass http://backend:8082/api/;

# 2. 测试后端服务
curl http://localhost:8082/api/hotel/hotels

# 3. 检查Docker网络
docker network inspect hotelorder_hotel-network
```

**可能原因：**
- ❌ Nginx配置未正确更新
- ❌ 后端服务未启动
- ❌ Docker网络配置问题

---

### 问题3：健康检查一直失败

**症状：**
```bash
docker-compose ps
# Status: Up (unhealthy)
```

**排查步骤：**
```bash
# 1. 查看健康检查日志
docker inspect hotel-backend | grep -A 10 Health

# 2. 手动执行健康检查命令
docker exec hotel-backend curl -f http://localhost:8082/actuator/health

# 3. 检查curl是否安装
docker exec hotel-backend which curl
```

**可能原因：**
- ❌ curl未安装（已修复）
- ❌ 后端服务启动时间过长
- ❌ 8082端口未正确暴露

---

## 📊 性能监控

### 查看容器资源使用

```bash
# 实时监控
docker stats

# 查看日志大小
docker-compose logs --tail=100 backend
docker-compose logs --tail=100 frontend
```

### 数据库连接池监控

```bash
# 查看后端日志中的连接池信息
docker-compose logs backend | grep -i "hikari"

# 建议配置（已在env.production中设置）：
# SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE=20
# SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE=5
```

---

## 📝 注意事项

### ⚠️ 安全警告

1. **保护配置文件**
   ```bash
   # env.production 包含敏感信息
   chmod 600 env.production
   
   # 不要提交到Git
   echo "env.production" >> .gitignore
   ```

2. **RDS访问控制**
   - ✅ 仅通过内网访问RDS
   - ✅ 使用白名单限制访问源
   - ❌ 不要开启RDS的公网访问

3. **端口安全**
   - ✅ 仅开放80端口（HTTP）
   - ✅ 可选开放443端口（HTTPS）
   - ❌ 不要开放8082端口（后端）
   - ❌ 不要开放3306端口（已使用RDS）

### 🔄 更新部署

```bash
# 拉取最新代码
git pull

# 重新构建并部署
docker-compose down
docker-compose up -d --build

# 不中断服务的滚动更新
docker-compose up -d --no-deps --build backend
docker-compose up -d --no-deps --build frontend
```

### 💾 数据备份

```bash
# 备份RDS数据库（从ECS执行）
mysqldump -hrm-bp19l0vw49988qi5b.mysql.rds.aliyuncs.com \
  -P3306 -uwzx -p hotelorder > backup_$(date +%Y%m%d).sql

# 建议设置自动备份
# 阿里云RDS控制台 → 备份恢复 → 设置自动备份
```

---

## 📚 相关文档

- [README-ALIYUN.md](README-ALIYUN.md) - 阿里云部署详细指南
- [SECURITY-NOTES.md](SECURITY-NOTES.md) - 安全配置注意事项
- [docker-compose.yml](docker-compose.yml) - Docker编排配置
- [env.production](env.production) - 生产环境变量（敏感文件）

---

## ✨ 修复验证清单

部署后请确认以下项目：

- [ ] 前端可以正常访问：`http://your-server-ip/`
- [ ] 后端健康检查通过：`docker-compose ps` 显示 `healthy`
- [ ] API请求正常：在前端操作，检查浏览器控制台无错误
- [ ] 数据库连接正常：后端日志无连接错误
- [ ] 安全组配置正确：仅80端口对外开放
- [ ] RDS白名单包含ECS内网IP
- [ ] 配置文件权限正确：`ls -la env.production` 显示 `600`

---

## 🆘 支持

如遇到问题：

1. 查看日志：`docker-compose logs -f`
2. 检查服务状态：`docker-compose ps`
3. 参考故障排查部分
4. 联系技术支持

---

**修复完成时间**: 2024年
**文档版本**: v1.0
**维护者**: 开发团队