FROM eclipse-temurin:23-jdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} run-journal.jar
ENTRYPOINT ["java","-jar","/run-journal.jar"]