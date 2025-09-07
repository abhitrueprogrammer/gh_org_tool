# Cookoff Backend

This is the backend for the gh-org-tools platform. It is a Spring Boot application that provides APIs for user gh-org-tools.

## Technologies Used

*   **Java 21**
*   **Spring Boot 3.2.0**
*   **Gradle**
*   **Spring Web**
*   **Spring Data JPA**
*   **Spring Security**
*   **My SQL**
*   **Flyway** for database migrations
*   **JWT** for authentication
*   **Lombok**
*   **Judge0** for code execution

## Setup and Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/abhitrueprogrammer/gh-org-tools.git
    ```
2.  **Navigate to the project directory:**
    ```bash
    cd gh-org-tools
    ```
3.  **Build the project:**
    ```bash
    ./gradlew build
    ```

## Configuration

The application uses a `.env` file for configuration. Create a `.env` file in the root of the project with the following variables:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/cookoff
SPRING_DATASOURCE_USERNAME=your-username
SPRING_DATASOURCE_PASSWORD=your-password
SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver

```

## Running the application

You can run the application using the following command:

```bash
./gradlew bootRun
```

The application will be available at `http://localhost:8080`.



