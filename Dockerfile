FROM eclipse-temurin:23-jdk
ARG JAR_FILE=target/run-journal-1.0.0-snapshot.jar
COPY ${JAR_FILE} run-journal-1.0.0-snapshot.jar
ENTRYPOINT ["java","-jar","/run-journal-1.0.0-snapshot.jar"]