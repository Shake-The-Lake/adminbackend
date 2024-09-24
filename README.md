
# Shake The Lake Backend

This project is the backend service for the **Shake The Lake** application.
It is a Java-based backend project utilizing Maven and PostgreSQL.


## Table of Contents
- [Description](#description)
- [Requirements](#requirements)
- [Quick Start](#quick-start)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [Database Configuration](#database-configuration)
- [Development Settings](#development-settings)
- [Deployment](#deployment)
- [Coding Guidelines](#coding-guidelines)     
- [Troubleshooting](#troubleshooting)

## Description

The Shake The Lake Backend is a Spring Boot application that provides RESTful APIs for the Shake The Lake platform.
It uses Maven for dependency management and PostgreSQL as the database.

## Requirements

Ensure that you have the following tools installed on your system:

- Java 17
- Docker & Docker Compose (for running the database / application)
- PostgreSQL (optional, for manual database setup)
- Maven (optional, for manual builds)

## Quick Start

Follow these instructions to set up and run the project locally.

1. Clone the repository:
   ```bash
   git clone git@gitlab.fhnw.ch:ip34-24bb/ip34-24bb_shakethelake/shakethelakebackend.git
   ```

2. Navigate into the project directory:
   ```bash
   cd shakethelakebackend
   ```
3. Open the project in IntelliJ IDEA (Recommended IDE for Java applications).

4. Import the Maven project in IntelliJ IDEA.

5. Run the application by either:
    - Using the `ShakeTheLakeBackend` compose configuration in IntelliJ IDEA.
    - Or, from your terminal, run:
      ```bash
      docker compose up
      ```


## Installation

1. **Clone the repository**:
   ```bash
   git clone git@gitlab.fhnw.ch:ip34-24bb/ip34-24bb_shakethelake/shakethelakebackend.git
   cd shakethelakebackend
   ```

2. **Open the project in IntelliJ IDEA**:
    - Open the folder in IntelliJ IDEA.
    - Select **Import Project** as a Maven project.

## Running the Application

You can run the application using Docker Compose or via IntelliJ IDEA.

### 1. Docker Compose

To run the backend and database using Docker Compose, execute:

```bash
docker compose up
```

This command will start the backend and the PostgreSQL database.

### 2. IntelliJ IDEA

If you are using IntelliJ IDEA, follow these steps:

1. Open the project.
2. Run the `ShakeTheLakeBackend` compose configuration.

### 3. Running manually
If you want to develop without Docker, you can run the application manually:
1. **Start the Database**:
    - Start the PostgreSQL database using Docker:
      ```bash
      docker compose up db
      ```
2. **Run the Application**:
    - Run the Spring Boot application in IntelliJ IDEA `ShakeTheLakeBackendApplication` or with maven:
      ```bash
      mvn spring-boot:run
      ```


## Database Configuration

### Connecting to the Database

1. Run the database by either:
    - Running the `ShakeTheLakeBackend` compose configuration in IntelliJ IDEA.
    - Or, from your terminal, run:
      ```bash
      docker compose up db
      ```

2. **Configure the database in IntelliJ IDEA**:

    - Open the **Database** tab.
    - Add a new **Data Source**.
    - Select **PostgreSQL**.
    - Use the following connection details:
        - **Host**: `localhost:5432`
        - **User**: `shakethelake`
        - **Password**: `shakethelake`

3. **Test the connection**, and then click **Apply** and **OK**.

## Development Settings

To ensure consistent code formatting and improved development workflows, apply the following settings in IntelliJ IDEA:

1. **Code Style**:
    - Go to `Editor -> Code Style -> Java`.
    - Set the scheme to **Project**.

2. **Save Actions**:
    - Go to `Tools -> Save Actions`.
    - Enable **Save Actions on save** to ensure automatic code reformatting.


## Deployment

The deployment process is automated using a **CI/CD pipeline** with **Docker** on **Azure**. The pipeline ensures that code changes are automatically built, tested, and deployed. The key steps in the deployment process are:

1. **CI/CD Pipeline**: Managed via Azure Pipelines, the process starts when changes are pushed to the main branch on the repository. The pipeline builds the Docker image.
2. **Docker Deployment**: Once the image is built, it is deployed to the production environment on Azure using container services, ensuring fast and scalable deployment.

This setup ensures efficient continuous integration and delivery, providing a streamlined development-to-production workflow.

## Coding Guidelines

To maintain code quality and consistency, the project follows specific Java coding standards. We provide a **`java-formatter.xml`** file which can be imported into IntelliJ IDEA to automatically format code according to the project’s standards.

### Steps to import the Java formatter:

1. Open IntelliJ IDEA.
2. Navigate to `Settings -> Editor -> Code Style -> Java`.
3. Click on the **gear icon** next to the "Scheme" dropdown and select **Import Scheme**.
4. Choose **IntelliJ IDEA code style XML**, and import the `formatter.xml` file provided in the project.

By following these guidelines, developers can ensure a consistent code style across the entire project, improving readability and maintainability.

## Troubleshooting

- If the application fails to start, ensure that Docker and Docker Compose are properly installed.
- Verify that port `5432` is available for the PostgreSQL database.
- Check logs from Docker containers by running:
  ```bash
  docker compose logs
    ```



This concludes the README for the **Shake The Lake Backend** project.
