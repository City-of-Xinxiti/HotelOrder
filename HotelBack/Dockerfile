# 多阶段构建
FROM registry.cn-hangzhou.aliyuncs.com/openjdk/openjdk:21-jre-slim as builder

# 设置工作目录
WORKDIR /app

# 复制gradle wrapper文件
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 复制源代码
COPY src src

# 构建应用
RUN chmod +x gradlew
RUN ./gradlew build -x test

# 生产镜像
FROM openjdk:21-jre-slim

# 安装curl用于健康检查
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# 创建应用用户
RUN groupadd -r appuser && useradd -r -g appuser appuser

# 设置工作目录
WORKDIR /app

# 从构建阶段复制jar文件
COPY --from=builder /app/build/libs/HotelPriceComparison-0.0.1-SNAPSHOT.jar app.jar

# 创建日志目录
RUN mkdir -p /app/logs && chown -R appuser:appuser /app

# 切换到应用用户
USER appuser

# 暴露端口
EXPOSE 8082

# 设置JVM参数
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:+UseContainerSupport"

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8082/actuator/health || exit 1

# 启动应用
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
