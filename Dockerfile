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

# Install JavaFX
RUN apt-get update && apt-get install -y wget unzip libgtk-3-0 libcanberra-gtk-module libx11-xcb1 xvfb x11vnc \
    && wget https://download2.gluonhq.com/openjfx/17.0.2/openjfx-17.0.2_linux-x64_bin-sdk.zip \
    && unzip openjfx-17.0.2_linux-x64_bin-sdk.zip -d /opt \
    && rm openjfx-17.0.2_linux-x64_bin-sdk.zip

# Set JavaFX environment variables
ENV PATH="/opt/javafx-sdk-17.0.2/bin:${PATH}"
ENV JAVA_HOME="/opt/javafx-sdk-17.0.2"

# Copy the built JARs from the previous stage
COPY --from=build /app/ui/target/*.jar ./
COPY --from=build /app/data/target/*.jar ./
COPY --from=build /app/service/target/*.jar ./

ENTRYPOINT ["sh", "-c", "Xvfb :99 -screen 0 1024x768x16 & x11vnc -display :99 -forever -nopw -rfbport 5900 & export DISPLAY=:99 && java -Djava.awt.headless=true --module-path /opt/javafx-sdk-17.0.2/lib --add-modules javafx.controls,javafx.fxml -cp 'ui-1.0-SNAPSHOT.jar:data-1.0-SNAPSHOT.jar:service-1.0-SNAPSHOT.jar' com.javathehutt.Main"]