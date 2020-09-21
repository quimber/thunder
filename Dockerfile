FROM maven:3.6.3-openjdk-11-slim as builder
WORKDIR /simianchecker/
COPY pom.xml /simianchecker/
COPY src /simianchecker/src/
RUN mvn package

FROM openjdk:11-jre-slim-buster
WORKDIR /simianchecker/
COPY --from=builder /simianchecker/target/simianchecker-1.0.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "/simianchecker/app.jar"]
