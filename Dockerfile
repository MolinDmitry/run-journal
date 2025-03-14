FROM eclipse-temurin:23-jdk
ARG JAR_FILE=target/test-docker-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} test-docker-0.0.1-snapshot.jar
ENTRYPOINT ["java","-jar","/test-docker-0.0.1-snapshot.jar"]