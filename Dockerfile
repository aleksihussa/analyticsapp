FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy the parent pom.xml and module poms to leverage layer caching
COPY pom.xml .
COPY ui/pom.xml ui/
COPY data/pom.xml data/
COPY service/pom.xml service/

# Download dependencies
RUN mvn dependency:go-offline

# Copy the rest of the project files
COPY . .

# Build the full project
RUN mvn clean package -DskipTests

# Stage 2: Use a JRE for running the application
FROM eclipse-temurin:21-jre-jammy

# Set the working directory for runtime
WORKDIR /app

# Copy the built JARs from the previous stage
COPY --from=build /app/ui/target/*.jar ./
COPY --from=build /app/data/target/*.jar ./
COPY --from=build /app/service/target/*.jar ./

ENTRYPOINT ["java", "-cp", "*", "com.javathehutt.Main"]
