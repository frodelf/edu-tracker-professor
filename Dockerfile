FROM openjdk:17.0.2-jdk-slim-buster
COPY target/*.jar edu-tracker-professor-0.0.1.jar
ENTRYPOINT ["java", "-jar", "edu-tracker-professor-0.0.1.jar"]