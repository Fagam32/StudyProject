FROM openjdk:11
COPY /target/StudyProject-0.0.1-SNAPSHOT.jar /opt/backend/
CMD ["java", "-jar", "/opt/backend/StudyProject-0.0.1-SNAPSHOT.jar"]
VOLUME /var/lib/backend
EXPOSE 8000