# User Service

This project is designed to manage user data for applications requiring user registration, 
updating, and retrieval functionalities. It provides a robust backend solution for 
handling user-related operations efficiently.

## 🎯 Key Features

- **User Registration:** Allows creating new user profiles with full details.
- **Update User Information:** Supports both partial and complete updates to user profiles.
- **Delete User Profiles:** Enables the deletion of user profiles from the system.
- **Query Users:** Users can be queried by unique identifiers like email or ID, 
and supports complex queries such as birthdate range.

## ⚙️ How to Run

Ensure you have JDK 17+ and PostgreSQL 16+ installed on your machine before proceeding.

1. **Clone the repository** to your local machine.
2. **Configure Database Access:** Navigate to `src/main/resources/application.properties` 
and replace the database connection properties `[DB_NAME], [USERNAME], [PASSWORD]` 
with your PostgreSQL database credentials.
3. **Build the project** by running the command `mvn clean package` in your terminal.
4. **Run the project** by executing the built jar file or using Spring Boot Maven plugin with `mvn spring-boot:run`.

Now, you can test the application using [Postman collection.](
https://www.postman.com/gooooodvin/workspace/public-collection/collection/21990349-99f2ca2e-23b1-44a3-b1ed-a803953b4055?action=share&creator=21990349
)

## 📁 Architecture

- `controller`: Handles API requests, mapping them to service operations.
- `dto`: Data Transfer Objects for transferring essential user data.
- `exception`: Custom exceptions for handling specific error cases in user management.
- `model`: Data models representing the user entities in the system.
- `repository`: Interfaces for database interactions, primarily with PostgreSQL.
- `service`: Business logic and service layer handling the logic behind the controllers. Also contain aspect for logging

## 🛠 Technologies Used

- **Java 17**
- **Spring Boot 3.3.0**
- **PostgreSQL 16.2**
