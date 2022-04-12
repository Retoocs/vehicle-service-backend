FROM maven:3-jdk-11 AS build
MAINTAINER Netgrif <devops@netgrif.com>
WORKDIR /backend
COPY src /backend/src
COPY pom.xml /backend
RUN mvn -P docker-build -DskipTests=true -f /backend/pom.xml clean package install


FROM openjdk:11-jdk
MAINTAINER Netgrif <devops@netgrif.com>
COPY --from=build backend/target/example.jar /example.jar
COPY --from=build backend/src/main/resources  /src/main/resources
EXPOSE 8080
ENTRYPOINT ["java","-jar","/example.jar"]
