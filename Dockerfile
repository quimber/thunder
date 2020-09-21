FROM maven:3.6.3-openjdk-11-slim as builder
WORKDIR /simianchecker/
COPY pom.xml /simianchecker/
COPY src /simianchecker/src/
RUN mvn install -Dmaven.test.skip=true

FROM openjdk:11-jre-slim-buster
WORKDIR /simianchecker/
COPY --from=builder /simianchecker/target/simianchecker-1.0.0-SNAPSHOT.jar app.jar
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.7.3/wait /simianchecker/wait
RUN chmod +x /simianchecker/wait
CMD ["java", "-jar", "/simianchecker/app.jar"]
