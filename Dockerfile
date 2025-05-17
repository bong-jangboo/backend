# === Stage 1: Build Spring Boot Application ===
FROM gradle:8.5.0-jdk17 AS builder

WORKDIR /app

# 빌드 캐시 최적화를 위한 Gradle 설정 복사
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# 의존성 캐싱
RUN gradle build --no-daemon -x test -x asciidoctor || return 0

# 전체 프로젝트 복사
COPY . .

# 실제 빌드 (test, asciidoctor 생략)
RUN gradle build --no-daemon -x test -x asciidoctor

# === Stage 2: Create Lightweight Runtime Image ===
FROM openjdk:17-jdk-slim

WORKDIR /app

# 빌드된 jar 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# Spring Boot 기본 포트
EXPOSE 8080

# 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
