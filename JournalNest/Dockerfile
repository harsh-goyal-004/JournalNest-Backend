FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

COPY . .

# Add execute permission for mvnw
RUN chmod +x ./mvnw

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
