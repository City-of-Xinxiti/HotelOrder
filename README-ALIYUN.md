# 酒店预订系统 - 阿里云服务器部署指南

## 🚀 快速部署到阿里云

### 1. 服务器要求
- **操作系统**: CentOS 7+ / Ubuntu 18.04+ / Alibaba Cloud Linux 2+
- **内存**: 最少 2GB，推荐 4GB+
- **磁盘**: 最少 20GB，推荐 50GB+
- **网络**: 公网IP，开放80端口

### 2. 阿里云安全组配置

#### 入方向规则
| 协议类型 | 端口范围 | 授权对象 | 描述 |
|---------|---------|---------|------|
| TCP | 80 | 0.0.0.0/0 | HTTP访问 |
| TCP | 443 | 0.0.0.0/0 | HTTPS访问（可选） |
| TCP | 22 | 您的IP | SSH管理 |

#### 出方向规则
| 协议类型 | 端口范围 | 授权对象 | 描述 |
|---------|---------|---------|------|
| TCP | 80/443 | 0.0.0.0/0 | 外网访问 |
| TCP | 3306 | 内网 | 数据库访问 |
| TCP | 8082 | 内网 | 后端服务 |

### 3. 部署步骤

#### 3.1 连接服务器
```bash
ssh root@your-server-ip
```

#### 3.2 更新系统
```bash
# CentOS/RHEL
yum update -y

# Ubuntu/Debian
apt update && apt upgrade -y

# Alibaba Cloud Linux
yum update -y
```

#### 3.3 克隆项目
```bash
# 安装Git（如果没有）
yum install -y git  # CentOS
# apt install -y git  # Ubuntu

# 克隆项目
git clone <your-repository-url>
cd Hotel
```

#### 3.4 配置环境
```bash
# 复制环境配置文件
cp env.production.example env.production

# 编辑配置文件（重要：修改密码）
vim env.production
```

#### 3.5 执行部署
```bash
# 给脚本执行权限
chmod +x deploy.sh

# 执行部署
./deploy.sh
```

### 4. 环境配置说明

#### 4.1 数据库密码配置
```bash
# 编辑 env.production 文件
MYSQL_ROOT_PASSWORD=your_very_secure_password_here
MYSQL_PASSWORD=your_secure_app_password_here
```

#### 4.2 锦江API配置
```bash
# 如果锦江API有变化，修改以下配置
JINJIANG_API_BASE_URL=https://your-api-domain.com
JINJIANG_APP_ID=your-app-id
JINJIANG_APP_SECRET=your-app-secret
```

### 5. 验证部署

#### 5.1 检查服务状态
```bash
# 查看所有服务状态
docker-compose ps

# 查看服务日志
docker-compose logs -f
```

#### 5.2 测试访问
```bash
# 测试前端
curl http://localhost:80

# 测试后端
curl http://localhost:8082/actuator/health

# 测试数据库连接
docker exec hotel-mysql mysql -u root -p -e "SHOW DATABASES;"
```

### 6. 域名配置（可选）

#### 6.1 购买域名
- 在阿里云购买域名
- 配置域名解析到服务器IP

#### 6.2 配置HTTPS
```bash
# 安装Certbot
yum install -y certbot python3-certbot-nginx

# 申请SSL证书
certbot --nginx -d your-domain.com

# 自动续期
echo "0 12 * * * /usr/bin/certbot renew --quiet" | crontab -
```

### 7. 监控和维护

#### 7.1 系统监控
```bash
# 查看系统资源使用
htop
# 或
top

# 查看磁盘使用
df -h

# 查看内存使用
free -h
```

#### 7.2 应用监控
```bash
# 查看Docker容器状态
docker stats

# 查看应用日志
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f mysql
```

#### 7.3 数据备份
```bash
# 创建备份脚本
cat > backup.sh << 'EOF'
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="/backup"
mkdir -p $BACKUP_DIR

# 备份数据库
docker exec hotel-mysql mysqldump -u root -p$MYSQL_ROOT_PASSWORD hotelorder > $BACKUP_DIR/hotelorder_$DATE.sql

# 备份应用数据
docker cp hotel-backend:/app/logs $BACKUP_DIR/logs_$DATE

# 清理7天前的备份
find $BACKUP_DIR -name "*.sql" -mtime +7 -delete
find $BACKUP_DIR -name "logs_*" -mtime +7 -exec rm -rf {} \;
EOF

chmod +x backup.sh

# 设置定时备份（每天凌晨2点）
echo "0 2 * * * /path/to/backup.sh" | crontab -
```

### 8. 性能优化

#### 8.1 数据库优化
```sql
-- 连接数据库
docker exec -it hotel-mysql mysql -u root -p

-- 创建索引
USE hotelorder;
CREATE INDEX idx_inn_id ON jinjiang_room_status(inn_id);
CREATE INDEX idx_end_of_day ON jinjiang_room_status(end_of_day);
CREATE INDEX idx_room_type_code ON jinjiang_room_status(room_type_code);
```

#### 8.2 应用优化
```bash
# 调整JVM参数
# 编辑 docker-compose.yml，修改JAVA_OPTS
JAVA_OPTS="-Xms1g -Xmx2g -XX:+UseG1GC -XX:+UseContainerSupport"
```

### 9. 故障排除

#### 9.1 常见问题

**问题1: 服务启动失败**
```bash
# 检查日志
docker-compose logs [service_name]

# 检查端口占用
netstat -tulpn | grep :80
netstat -tulpn | grep :8082
```

**问题2: 数据库连接失败**
```bash
# 检查数据库状态
docker-compose logs mysql

# 检查网络连接
docker exec hotel-backend ping mysql
```

**问题3: 前端无法访问**
```bash
# 检查Nginx配置
docker exec hotel-frontend nginx -t

# 检查防火墙
firewall-cmd --list-ports
firewall-cmd --add-port=80/tcp --permanent
firewall-cmd --reload
```

#### 9.2 日志分析
```bash
# 查看系统日志
journalctl -u docker

# 查看应用日志
tail -f /var/log/messages

# 查看Nginx访问日志
docker exec hotel-frontend tail -f /var/log/nginx/access.log
```

### 10. 安全加固

#### 10.1 防火墙配置
```bash
# 安装并配置防火墙
yum install -y firewalld
systemctl start firewalld
systemctl enable firewalld

# 开放必要端口
firewall-cmd --add-port=80/tcp --permanent
firewall-cmd --add-port=443/tcp --permanent
firewall-cmd --add-port=22/tcp --permanent
firewall-cmd --reload
```

#### 10.2 SSH安全
```bash
# 修改SSH端口
vim /etc/ssh/sshd_config
# 修改 Port 22 为其他端口

# 禁用root登录
# 创建普通用户
useradd -m -s /bin/bash admin
passwd admin
usermod -aG docker admin

# 重启SSH服务
systemctl restart sshd
```

### 11. 更新部署

#### 11.1 代码更新
```bash
# 拉取最新代码
git pull

# 重新构建并部署
docker-compose down
docker-compose up -d --build
```

#### 11.2 数据迁移
```bash
# 备份当前数据
./backup.sh

# 更新应用
docker-compose up -d --build

# 验证数据完整性
docker exec hotel-mysql mysql -u root -p -e "USE hotelorder; SHOW TABLES;"
```

---

## 📞 技术支持

### 部署问题排查清单
1. ✅ 服务器资源是否充足（内存、磁盘）
2. ✅ 安全组是否配置正确
3. ✅ 防火墙是否开放端口
4. ✅ Docker服务是否正常运行
5. ✅ 环境变量是否正确配置
6. ✅ 数据库连接是否正常
7. ✅ 应用日志是否有错误

### 联系方式
- 查看详细日志: `docker-compose logs`
- 系统资源监控: `htop` 或 `top`
- 网络连接测试: `telnet your-server-ip 80`

---

**重要提醒**: 
- 生产环境部署前请务必修改所有默认密码
- 定期备份数据库和应用数据
- 监控系统资源使用情况
- 及时更新系统和应用补丁
