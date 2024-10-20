# Dockerfile

# jdk17 Image Start
FROM openjdk:17

# jar 파일 복제
COPY ./build/libs/openbook-0.0.1-SNAPSHOT.jar openbook.jar

# 인자 설정 부분과 jar 파일 복제 부분 합쳐서 진행해도 무방
#COPY build/libs/*.jar app.jar

# 실행 명령어
ENTRYPOINT ["java", "-jar", "openbook.jar"]