# 🔒 阿里云部署安全注意事项

## ⚠️ 重要提醒

### 1. 保护敏感配置文件

**env.production** 文件包含敏感信息，请务必：

- ✅ **不要将此文件提交到Git仓库**
  ```bash
  # 确保 .gitignore 中包含
  env.production
  *.env
  ```

- ✅ **设置正确的文件权限**
  ```bash
  chmod 600 env.production
  ```

- ✅ **使用安全的方式传输配置文件**
  - 使用 `scp` 通过SSH传输
  - 或在服务器上直接创建并编辑
  ```bash
  scp env.production root@your-server-ip:/path/to/HotelOrder/
  ```

---

## 🛡️ 阿里云安全组配置

### 入方向规则（严格控制）

| 优先级 | 协议 | 端口 | 授权对象 | 说明 |
|-------|------|------|---------|------|
| 1 | TCP | 22 | **您的IP地址** | SSH管理（不要开放0.0.0.0/0） |
| 2 | TCP | 80 | 0.0.0.0/0 | HTTP访问 |
| 3 | TCP | 443 | 0.0.0.0/0 | HTTPS访问（可选） |
| 4 | TCP | 8082 | **拒绝** | 后端端口不对外开放 |

**关键安全建议：**
- ❌ **绝对不要**将 8082 端口对外开放
- ❌ **绝对不要**将 SSH(22) 开放给 0.0.0.0/0
- ✅ 使用白名单限制 SSH 访问源IP

### RDS安全配置

1. **白名单设置**
   - 登录阿里云RDS控制台
   - 数据安全性 → 白名单设置
   - 添加ECS服务器的内网IP（不是公网IP）

2. **网络类型**
   - 确保RDS和ECS在同一VPC内
   - 使用内网连接，不要使用公网地址

3. **账号权限**
   - 不要使用root账号连接应用
   - 创建专用账号，仅授予必要的数据库权限
   ```sql
   CREATE USER 'hotelapp'@'%' IDENTIFIED BY 'strong_password';
   GRANT SELECT, INSERT, UPDATE, DELETE ON hotelorder.* TO 'hotelapp'@'%';
   FLUSH PRIVILEGES;
   ```

---

## 🔐 密码安全

### 当前配置中的密码问题

您的RDS密码包含特殊字符：`QWer741852963!`

**已处理：**
- ✅ 在 `env.production` 中已将 `!` 编码为 `%21`
- ✅ JDBC URL 中的特殊字符已正确转义

**建议：**
- 🔄 **强烈建议修改默认密码**
- 使用更复杂的密码（16+字符，包含大小写、数字、特殊字符）
- 定期更换密码（建议每3-6个月）

### 修改RDS密码后的更新步骤

```bash
# 1. 在阿里云RDS控制台修改密码
# 2. 更新env.production文件
vim env.production
# 修改 RDS_PASSWORD=新密码（记得URL编码特殊字符）

# 3. 重启服务
docker-compose down
docker-compose up -d --build
```

---

## 🚨 常见安全隐患

### ❌ 不安全的做法

1. **在代码中硬编码密码**
   ```javascript
   // ❌ 错误示例
   const password = "QWer741852963!";
   ```

2. **将配置文件提交到Git**
   ```bash
   # ❌ 错误操作
   git add env.production
   git commit -m "add config"
   ```

3. **使用弱密码**
   ```
   ❌ password: 123456
   ❌ password: admin123
   ❌ password: qwerty
   ```

4. **开放不必要的端口**
   ```
   ❌ 0.0.0.0/0 → 22 (SSH)
   ❌ 0.0.0.0/0 → 3306 (MySQL)
   ❌ 0.0.0.0/0 → 8082 (后端API)
   ```

### ✅ 安全的做法

1. **使用环境变量**
   ```javascript
   // ✅ 正确示例
   const password = process.env.DB_PASSWORD;
   ```

2. **使用 .gitignore**
   ```bash
   # ✅ .gitignore
   env.production
   .env
   *.log
   ```

3. **使用强密码**
   ```
   ✅ password: K7$mP9#nQ2@wE5xR8!tY4
   ✅ 长度 ≥ 16 字符
   ✅ 包含大小写字母、数字、特殊字符
   ```

4. **最小权限原则**
   ```
   ✅ 仅开放必要的端口
   ✅ 使用白名单限制访问源
   ✅ 应用账号仅授予必要权限
   ```

---

## 🔍 安全审计检查清单

部署前请确认：

- [ ] `env.production` 文件权限设置为 600
- [ ] `env.production` 已添加到 `.gitignore`
- [ ] 所有密码已修改为强密码
- [ ] 阿里云安全组规则配置正确
- [ ] RDS白名单仅包含ECS内网IP
- [ ] SSH端口仅对特定IP开放
- [ ] 8082端口未对外开放
- [ ] 已启用阿里云云盾和DDoS防护
- [ ] 定期备份数据库数据

部署后请验证：

```bash
# 1. 检查端口开放情况
nmap -p 22,80,443,8082,3306 your-server-ip

# 预期结果：
# 22/tcp   open  ssh
# 80/tcp   open  http
# 443/tcp  可选  https
# 8082/tcp filtered/closed  ✅ 不应该是 open
# 3306/tcp filtered/closed  ✅ 不应该是 open

# 2. 测试后端API不可直接访问
curl http://your-server-ip:8082/api/hotel/hotels
# 预期结果：连接失败或超时 ✅

# 3. 测试前端可以正常访问
curl http://your-server-ip/
# 预期结果：返回HTML内容 ✅
```

---

## 📱 应急响应

### 如果发现安全问题

1. **立即修改密码**
   ```bash
   # RDS控制台修改数据库密码
   # 更新 env.production
   # 重启服务
   ```

2. **检查访问日志**
   ```bash
   # 查看Nginx访问日志
   docker-compose logs frontend | grep "GET\|POST"
   
   # 查看后端日志
   docker-compose logs backend | grep "ERROR\|WARN"
   ```

3. **临时封禁IP**
   ```bash
   # 在阿里云安全组中添加拒绝规则
   # 或使用防火墙
   firewall-cmd --add-rich-rule='rule family="ipv4" source address="恶意IP" reject'
   ```

4. **联系技术支持**
   - 阿里云工单系统
   - 电话：95187

---

## 📚 推荐阅读

- [阿里云ECS安全最佳实践](https://help.aliyun.com/document_detail/25387.html)
- [阿里云RDS安全白皮书](https://help.aliyun.com/document_detail/26136.html)
- [OWASP安全开发指南](https://owasp.org/)
- [Docker安全最佳实践](https://docs.docker.com/engine/security/)

---

## 🆘 紧急联系方式

- **阿里云客服**: 95187
- **阿里云工单**: https://workorder.console.aliyun.com/
- **安全应急响应**: security@aliyun.com

---

**最后更新**: 2024年
**维护者**: 开发团队

**重要提醒**: 安全是持续的过程，请定期检查和更新安全配置！