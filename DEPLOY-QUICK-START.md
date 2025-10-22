# 🚀 快速部署指南

## 服务器信息

- **ECS公网IP**: `118.31.121.25`
- **RDS地址**: `rm-bp19l0vw49988qi5b.mysql.rds.aliyuncs.com`
- **数据库用户**: `wzx`
- **前端访问**: http://118.31.121.25/
- **后端端口**: 8082（内部通信，不对外）

---

## 📋 部署前准备清单

### 1. 阿里云安全组配置

登录阿里云ECS控制台，配置安全组规则：

**入方向规则**：
```
协议类型  端口范围  授权对象        描述
TCP      80       0.0.0.0/0      HTTP访问
TCP      22       你的IP/32      SSH管理（限制你的IP）
TCP      443      0.0.0.0/0      HTTPS（可选）
```

**⚠️ 重要**：不要开放 8082、3306 等内部端口！

### 2. RDS白名单配置

1. 登录阿里云RDS控制台
2. 找到实例：`rm-bp19l0vw49988qi5b`
3. 进入 **数据安全性** → **白名单设置**
4. 添加ECS的**内网IP**（不是公网IP 118.31.121.25）
   - 获取内网IP：SSH到ECS后执行 `ip addr show eth0`
   - 格式示例：`172.16.0.10/32`

### 3. 确认数据库已创建

```bash
# 本地测试RDS连接
mysql -hrm-bp19l0vw49988qi5b.mysql.rds.aliyuncs.com -P3306 -uwzx -p
# 输入密码：QWer741852963!

# 检查数据库是否存在
SHOW DATABASES;

# 如果没有 hotelorder 数据库，创建它
CREATE DATABASE IF NOT EXISTS hotelorder DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

---

## 🚀 一键部署步骤

### Step 1: 连接到服务器

```bash
# 从你的本地电脑SSH连接到ECS
ssh root@118.31.121.25

# 输入服务器密码
```

### Step 2: 上传项目文件

**方法A：使用Git（推荐）**
```bash
# 在ECS上执行
cd /root
git clone <your-repository-url> HotelOrder
cd HotelOrder
```

**方法B：使用SCP上传**
```bash
# 在你的本地电脑上执行
cd C:\Users\wangz\IdeaProjects
scp -r HotelOrder root@118.31.121.25:/root/
```

### Step 3: 验证配置文件

```bash
# 在ECS上执行
cd /root/HotelOrder

# 查看配置文件
cat env.production

# 确认显示：
# RDS_URL=jdbc:mysql://rm-bp19l0vw49988qi5b.mysql.rds.aliyuncs.com:3306/hotelorder...
# RDS_USERNAME=wzx
# RDS_PASSWORD=QWer741852963%21
```

### Step 4: 测试RDS连接

```bash
# 在ECS上测试数据库连接
mysql -hrm-bp19l0vw49988qi5b.mysql.rds.aliyuncs.com -P3306 -uwzx -p

# 输入密码：QWer741852963!

# 如果连接成功，输入以下命令退出
exit
```

**如果连接失败**：
- ❌ 检查RDS白名单是否添加了ECS的**内网IP**
- ❌ 确认密码正确
- ❌ 检查RDS实例是否运行中

### Step 5: 执行一键部署

```bash
# 给部署脚本执行权限
chmod +x deploy.sh

# 执行部署（会自动安装Docker、构建镜像、启动服务）
./deploy.sh

# 部署过程约需 5-10 分钟，会显示进度信息
```

**部署过程中会自动：**
1. ✅ 检查并安装 Docker
2. ✅ 检查并安装 Docker Compose
3. ✅ 停止旧服务（如果有）
4. ✅ 构建前端镜像
5. ✅ 构建后端镜像
6. ✅ 启动所有服务
7. ✅ 等待健康检查通过

### Step 6: 验证部署

```bash
# 查看服务状态
docker-compose ps

# 应该看到：
# NAME              STATUS
# hotel-backend     Up (healthy)
# hotel-frontend    Up (healthy)

# 查看日志（检查是否有错误）
docker-compose logs -f

# 按 Ctrl+C 退出日志查看
```

---

## ✅ 部署成功验证

### 1. 在服务器上测试

```bash
# 测试前端（应返回HTML内容）
curl http://localhost:80

# 测试后端健康检查（应返回JSON健康信息）
curl http://localhost:8082/actuator/health

# 测试API接口（应返回JSON数据）
curl http://localhost:8082/api/hotel/hotels
```

### 2. 在浏览器中访问

**前端访问地址**：
```
http://118.31.121.25/
```

**预期效果**：
- ✅ 能看到酒店预订系统的前端页面
- ✅ 可以正常浏览酒店列表
- ✅ 浏览器控制台无API错误

**如果无法访问**：
1. 检查阿里云安全组是否开放了 80 端口
2. 检查防火墙：`firewall-cmd --list-ports`
3. 查看服务状态：`docker-compose ps`

### 3. 测试API功能

打开浏览器开发者工具（F12），在前端页面操作时观察：

**Network标签应该看到**：
```
Status: 200 OK
Request URL: http://118.31.121.25/api/hotel/hotels
```

---

## 📊 服务管理命令

### 查看服务状态
```bash
docker-compose ps
```

### 查看实时日志
```bash
# 所有服务日志
docker-compose logs -f

# 仅查看后端日志
docker-compose logs -f backend

# 仅查看前端日志
docker-compose logs -f frontend

# 查看最近100行日志
docker-compose logs --tail=100
```

### 重启服务
```bash
# 重启所有服务
docker-compose restart

# 重启特定服务
docker-compose restart backend
docker-compose restart frontend
```

### 停止服务
```bash
docker-compose down
```

### 更新代码后重新部署
```bash
# 拉取最新代码
git pull

# 重新构建并启动
docker-compose down
docker-compose up -d --build

# 查看启动日志
docker-compose logs -f
```

### 进入容器调试
```bash
# 进入后端容器
docker exec -it hotel-backend sh

# 进入前端容器
docker exec -it hotel-frontend sh

# 查看环境变量
docker exec hotel-backend env | grep SPRING

# 退出容器
exit
```

---

## 🔍 常见问题排查

### 问题1: 无法访问 http://118.31.121.25/

**排查步骤**：
```bash
# 1. 检查服务是否运行
docker-compose ps
# 应该看到 hotel-frontend 是 Up 状态

# 2. 检查端口监听
netstat -tulpn | grep :80
# 应该看到 docker-proxy 在监听 80 端口

# 3. 检查防火墙
firewall-cmd --list-ports
# 如果看不到 80/tcp，执行：
firewall-cmd --add-port=80/tcp --permanent
firewall-cmd --reload

# 4. 检查阿里云安全组
# 登录阿里云控制台确认已开放 80 端口
```

### 问题2: API请求失败（前端显示数据加载失败）

**排查步骤**：
```bash
# 1. 检查后端服务状态
docker-compose ps backend
# 应该是 Up (healthy)

# 2. 查看后端日志
docker-compose logs backend | grep -i error

# 3. 测试后端API
curl http://localhost:8082/api/hotel/hotels

# 4. 检查数据库连接
docker-compose logs backend | grep -i "database\|mysql\|connection"
```

### 问题3: 后端无法连接数据库

**症状**：
```
java.sql.SQLException: Access denied
或
Communications link failure
```

**排查步骤**：
```bash
# 1. 在ECS上测试RDS连接
mysql -hrm-bp19l0vw49988qi5b.mysql.rds.aliyuncs.com -P3306 -uwzx -p

# 2. 查看后端环境变量
docker exec hotel-backend env | grep SPRING_DATASOURCE

# 3. 检查RDS白名单
# 登录阿里云RDS控制台，确认ECS内网IP在白名单中

# 4. 获取ECS内网IP
ip addr show eth0 | grep inet
```

### 问题4: 容器健康检查失败

**症状**：
```bash
docker-compose ps
# Status: Up (unhealthy)
```

**排查步骤**：
```bash
# 1. 查看健康检查日志
docker inspect hotel-backend | grep -A 10 Health

# 2. 手动执行健康检查命令
docker exec hotel-backend curl -f http://localhost:8082/actuator/health

# 3. 查看详细日志
docker-compose logs backend
```

---

## 🔐 安全建议

### 立即执行的安全措施

```bash
# 1. 限制SSH访问（将 YOUR_IP 替换为你的真实IP）
# 在阿里云安全组中，将22端口的授权对象从 0.0.0.0/0 改为 YOUR_IP/32

# 2. 设置配置文件权限
chmod 600 env.production

# 3. 配置防火墙
systemctl start firewalld
systemctl enable firewalld
firewall-cmd --add-port=80/tcp --permanent
firewall-cmd --reload

# 4. 查看开放的端口（确保只有80和22）
firewall-cmd --list-ports
```

### 强烈建议修改的配置

1. **修改RDS密码**
   - 登录阿里云RDS控制台
   - 修改账号密码为更强的密码（16+字符）
   - 更新 `env.production` 中的 `RDS_PASSWORD`
   - 重启服务：`docker-compose restart backend`

2. **配置HTTPS（推荐）**
   - 申请SSL证书（阿里云提供免费证书）
   - 配置Nginx支持HTTPS
   - 开放安全组 443 端口

3. **定期备份数据库**
   - 在阿里云RDS控制台设置自动备份
   - 建议每天凌晨自动备份

---

## 📱 访问信息汇总

| 服务 | 地址 | 说明 |
|-----|------|------|
| 前端 | http://118.31.121.25/ | 用户访问入口 |
| 后端API | http://118.31.121.25/api/* | 通过Nginx代理 |
| 后端健康检查 | 内部访问 :8082/actuator/health | 不对外开放 |
| RDS数据库 | rm-bp19l0vw49988qi5b... | 内网访问 |

**对外开放的端口**：
- ✅ 80 (HTTP)
- ✅ 22 (SSH，限制IP访问)
- ❌ 8082 (后端，不对外)
- ❌ 3306 (数据库，使用RDS)

---

## 🎯 部署完成检查清单

完成部署后，请逐项确认：

- [ ] 访问 http://118.31.121.25/ 能看到前端页面
- [ ] 前端页面能正常显示酒店列表
- [ ] 浏览器控制台（F12）无API错误
- [ ] `docker-compose ps` 显示两个服务都是 healthy
- [ ] 后端日志无数据库连接错误
- [ ] 阿里云安全组仅开放 80 和 22 端口
- [ ] RDS白名单包含ECS内网IP
- [ ] `env.production` 文件权限设置为 600
- [ ] 防火墙已启用并配置正确

---

## 📞 技术支持

### 如果遇到问题

1. **查看日志**
   ```bash
   docker-compose logs -f
   ```

2. **检查服务状态**
   ```bash
   docker-compose ps
   docker stats
   ```

3. **重启服务尝试**
   ```bash
   docker-compose restart
   ```

4. **完全重新部署**
   ```bash
   docker-compose down
   docker-compose up -d --build --force-recreate
   ```

### 相关文档

- 📖 [DOCKER-FIXES.md](DOCKER-FIXES.md) - 详细故障排查
- 🔒 [SECURITY-NOTES.md](SECURITY-NOTES.md) - 安全配置详解
- ☁️ [README-ALIYUN.md](README-ALIYUN.md) - 阿里云完整指南

---

## ✨ 下一步

部署成功后，你可以：

1. **配置域名**（可选）
   - 购买域名
   - 配置DNS解析到 118.31.121.25
   - 申请SSL证书
   - 配置HTTPS访问

2. **监控和维护**
   - 设置RDS自动备份
   - 配置日志收集
   - 设置监控告警

3. **性能优化**
   - 调整数据库连接池
   - 配置CDN加速
   - 优化Nginx配置

---

**祝部署顺利！** 🎉

如有问题，请查看日志并参考故障排查部分。