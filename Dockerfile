# --- Multistage build starts here ---
FROM maven:3.9.11-eclipse-temurin-21 AS stage1

WORKDIR /opt

LABEL MAINTAINER="VIGNAN" 

COPY . .

RUN mvn clean package -DskipTests

# Use a lightweight Java 21 runtime image for running the application

# --- Stage 2 starts here ---
FROM eclipse-temurin:21-jre

# Copy the built JAR file from the build stage to the runtime image
COPY --from=stage1 /opt/target/gs-spring-boot-0.1.0.jar ./app.jar

# Expose port 8081 for the application
EXPOSE 8081

# Set the default command to run the Spring Boot application
ENTRYPOINT ["java","-jar","app.jar"]


















###############################################################################
# # Use an official Maven image with OpenJDK 17
# FROM maven:3.9.9-eclipse-temurin-17

# # Set the maintainer
# LABEL maintainer="vignan <vignan1234.email@example.com>"

# # Set the working directory in the container
# WORKDIR /app

# # Copy the Maven project files to the container
# COPY . .


# # Build the Maven project
# RUN mvn clean package -DskipTests

# # Expose the application port
# EXPOSE 8081

# # Run the application
# ENTRYPOINT ["java", "-jar", "target/gs-spring-boot-0.1.0.jar"]
