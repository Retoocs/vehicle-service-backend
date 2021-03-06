FROM maven:3-jdk-11 AS build
MAINTAINER Timak <timakprojekt@gmail.com>
WORKDIR /app
COPY src /app/src
COPY pom.xml /app
RUN mvn -P docker-build -DskipTests=true -f /app/pom.xml clean package install


FROM openjdk:11-jdk
MAINTAINER Timak <timakprojekt@gmail.com>
COPY --from=build app/target/app.jar /app.jar
COPY --from=build app/src/main/resources  /src/main/resources
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
