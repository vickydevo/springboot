FROM maven:3.9.6-eclipse-temurin-21-alpine

LABEL maintainer="vignan"

# Use /app or /opt/app as a standard
WORKDIR /app 

# Copy everything
COPY . .

# Build the jar and cleanup in one layer to save a tiny bit of space
RUN mvn clean package -DskipTests

# Use a more specific path to find your jar
# Note: If your pom.xml generates 'my-app-1.0.jar', this renames it to 'app.jar'
RUN cp target/*.jar app.jar

EXPOSE 8081

# Good practice: Cleanup source code if you aren't using multi-stage
# This keeps the image slightly smaller (though multi-stage is better for this)
RUN rm -rf src pom.xml target

ENTRYPOINT [ "java", "-jar" , "app.jar" ]













# --- Multistage build starts here ---
FROM maven:3.9.12-eclipse-temurin-21 AS stage1
# FROM maven:3.9.12-eclipse-temurin-21-alpine AS stage1

WORKDIR /opt

LABEL MAINTAINER="VIGNAN" 

COPY . .

RUN mvn clean package -DskipTests

# Use a lightweight Java 21 runtime image for running the application

# --- Stage 2 starts here ---
FROM eclipse-temurin:21-jre
# FROM eclipse-temurin:21-jre-ubi9-minimal

# Copy the built JAR file from the build stage to the runtime image
COPY --from=stage1 /opt/target/gs-spring-boot-0.1.0.jar ./app.jar

# Expose port 8081 for the application
EXPOSE 8081

# Set the default command to run the Spring Boot application
ENTRYPOINT ["java","-jar","app.jar"]
