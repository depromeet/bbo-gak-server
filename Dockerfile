FROM openjdk:21
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ARG apm_agent=apm-agent/*.jar
COPY ${apm_agent} apm-agent.jar

ARG PROFILE=dev
ENV SPRING_PROFILES_ACTIVE=${PROFILE}

ENTRYPOINT ["java", \
"-javaagent:/apm-agent.jar", \
"-Delastic.apm.server_urls=http://114.70.23.79:8200", \
"-Delastic.apm.service_name=boot-apm-agent", \
"-Delastic.apm.application_packages=com.server", \
"-Delastic.apm.environment=dev", \
"-jar", \
"/app.jar"]