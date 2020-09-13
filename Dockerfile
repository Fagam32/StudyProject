FROM maven:3.5-jdk-11 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
COPY mvnw /usr/src/app
COPY mvnw.cmd /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:11
COPY --from=build /usr/src/app/target/StudyProject-0.0.1-SNAPSHOT.jar /usr/app/StudyProject-0.0.1-SNAPSHOT.jar
EXPOSE 8000
ENTRYPOINT ["java","-jar","/usr/app/StudyProject-0.0.1-SNAPSHOT.jar"]
