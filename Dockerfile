FROM openjdk:21
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ARG PROFILE=local
ENV SPRING_PROFILES_ACTIVE=${PROFILE}

ENTRYPOINT ["java","-jar","/app.jar"]