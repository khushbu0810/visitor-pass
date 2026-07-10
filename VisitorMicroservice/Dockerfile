FROM maven:3.9.6-eclipse-temurin-22 AS builder

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:22-jdk-alpine AS runner

WORKDIR /app

COPY --from=builder /app/VisitorMicroservice/target/VisitorMicroservice-0.0.1-SNAPSHOT.jar /app/app.jar

COPY --from=builder /app/VisitorMicroservice/src/main/resources/kafka /app/kafka

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
