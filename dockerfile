FROM openjdk:19-ea-alpine3.16

# SET THE ENVIRONMENT VARIABLES
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod

# Set the working directory
WORKDIR /opt/shakethelacke

# Copy the application's jar to the container
COPY target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
