FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY build/libs/bank-of-oliver-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]