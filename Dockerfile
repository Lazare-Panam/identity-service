FROM eclipse-temurin:25-jdk-noble AS build
WORKDIR /identity-service

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -B

COPY src src
RUN ./mvnw package -DskipTests -B

# Stage 2: run the jar
FROM eclipse-temurin:25-jre-noble
WORKDIR /identity-service

COPY --from=build /identity-service/target/identity-service-0.0.1-SNAPSHOT.jar app.jar

USER ubuntu

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "app.jar"]
