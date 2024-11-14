# 첫 번째 단계: 애플리케이션 빌드
FROM gradle:jdk21-corretto-al2023 AS build

# 작업 디렉토리 설정
WORKDIR /home/app

# Gradle 캐시를 활용하기 위해 종속성 파일 복사
COPY build.gradle settings.gradle ./
COPY gradle gradle

# Gradle Wrapper가 있는 경우 복사 (없다면 생략 가능)
# COPY gradlew gradlew
# RUN chmod +x gradlew

# 종속성 다운로드 (캐시 활용)
RUN gradle dependencies --no-daemon

# 소스 코드 복사
COPY src src

# 애플리케이션 빌드 (테스트 제외)
RUN gradle build -x test --no-daemon

# 두 번째 단계: 런타임 환경 설정
FROM alpine:latest

# 필요한 패키지 설치: OpenJDK 21 JRE
RUN apk add --no-cache openjdk21-jre

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일 복사
COPY --from=build /home/app/build/libs/*.jar app.jar

# 포트 노출 (예: 8080)
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
