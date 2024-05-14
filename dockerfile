FROM maven:latest AS build

# SET THE ENVIRONMENT VARIABLES
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod

WORKDIR /code
COPY pom.xml .
COPY src ./src

RUN ["mvn", "package", "-DskipTests"]

FROM openjdk:19-ea

WORKDIR /code

# Copy the application's jar to the container
COPY --from=build /code/target/shake-the-lake-backend.jar ./

# Run the application
ENTRYPOINT ["java", "-jar", "shake-the-lake-backend.jar"]
