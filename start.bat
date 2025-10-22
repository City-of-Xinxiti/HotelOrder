@echo off
echo 🚀 启动酒店预订系统...

REM 检查Docker是否安装
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Docker未安装，请先安装Docker Desktop
    pause
    exit /b 1
)

docker-compose --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Docker Compose未安装，请先安装Docker Compose
    pause
    exit /b 1
)

REM 停止现有容器
echo 🛑 停止现有容器...
docker-compose down

REM 构建并启动服务
echo 🔨 构建并启动服务...
docker-compose up --build -d

REM 等待服务启动
echo ⏳ 等待服务启动...
timeout /t 30 /nobreak >nul

REM 检查服务状态
echo 📊 检查服务状态...
docker-compose ps

echo.
echo ✅ 服务启动完成！
echo 🌐 前端访问地址: http://localhost
echo 🔧 后端API地址: http://localhost:8082
echo 🗄️  数据库端口: 3306
echo.
echo 📝 查看日志: docker-compose logs -f
echo 🛑 停止服务: docker-compose down
pause
