# TODO APP

### Overview

This project consists of a Todo List Management REST API application.

### Technologies Used

* Java 21
* Spring Boot
* Spring Data JPA
* Spring Security
* JUnit
* PostgreSQL
* Maven
* Flyway
* Swagger
* SonarLint
* Docker
* Git

### Prerequisites

* Java JDK 21: Ensure you have JDK 21 installed.
* Maven: Install Maven to manage project dependencies and build.
* PostgreSQL: Set up a PostgreSQL database (if not using Docker).
* Docker: Install Docker for container management.

### Project Setup

Below you may find the instructions for setting up the project with two methods:

* [Maven](#maven)
* [Docker](#docker)

Feel free to choose the one you prefer.

### Maven

#### Configure Database
In PostgreSQL, create a new database and update the application.properties file 
with your database credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/<your-database>
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
```

#### Build the Project
Open a terminal in the project root directory. Run the following command to build the project:

`./mvnw clean package`

This command compiles the code, runs tests, and packages the application into a JAR file. As output it generates a **target** folder with the compiled code.

#### Running the Application
You can run the application with the following command:

`java -jar target/todoapp-0.0.1-SNAPSHOT.jar`

#### Accessing the Application
Once the application is running, you may access it at:
http://localhost:8080

#### Accessing the Swagger UI
You may access the Swagger UI at: http://localhost:8080/swagger-ui/index.html

### Docker

#### Build a Docker Image
   In the root directory of the project, using the following spring-boot command, you are able to build a docker image for the application:

`./mvnw spring-boot:build-image`

The output of the command will be:

```shell
[INFO] Successfully built image 'docker.io/library/todoapp:0.0.1-SNAPSHOT'
```

I have created a docker-compose file to run this image with a database container. I have added this file to the docker directory in the root project.

#### Running the Application

Navigating to the docker directory:

```shell
cd docker
```

Then running docker compose up will start both containers. Use the following command:

```shell
docker compose up
```

The output will be:

```shell
database-1  | 2024-10-24 10:47:32.294 UTC [1] LOG:  database system is ready to accept connections
todoapp-1   | 2024-10-24T10:47:44.589Z  INFO 1 --- [todoapp] [main] c.c.c.todoapp.TodoappApplication : Started TodoappApplication in 11.565 seconds (process running for 12.65)
```

#### Accessing the application
The application will be accessible at http://localhost:8080

For the Swagger UI,  please go to http://localhost:8080/swagger-ui/index.html


### Testing the Application

For the detailed instructions on how to test the APIs, please refer to **TESTING.pdf** file.