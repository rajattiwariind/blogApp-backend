# BlogSphere

BlogSphere is a **RESTful backend application for a blogging platform**, developed using **Java and Spring Boot**. The project provides APIs for user authentication, blog post management, and database operations with a focus on security, clean architecture, and scalable backend development.

## Features

* User registration and login
* JWT-based authentication and authorization
* Secure REST APIs
* Create, read, update, and delete blog posts
* User management
* MySQL database integration
* JPA/Hibernate for database persistence
* Spring Data JPA repositories
* Entity-based data modeling
* Password security
* Role-based access control
* Exception handling
* RESTful API architecture

## Tech Stack

| Technology      | Usage                          |
| --------------- | ------------------------------ |
| Java            | Backend programming            |
| Spring Boot     | Backend framework              |
| Spring Web      | REST API development           |
| Spring Data JPA | Database operations            |
| Hibernate       | ORM                            |
| Spring Security | Authentication & authorization |
| JWT             | Token-based authentication     |
| MySQL           | Database                       |
| Maven           | Dependency management          |
| Lombok          | Reducing boilerplate code      |
| IntelliJ IDEA   | Development environment        |

## Project Architecture

The project follows a layered backend architecture:

```text
BlogSphere
│
├── Controller
│   └── Handles HTTP requests and REST endpoints
│
├── Service
│   └── Contains business logic
│
├── Repository
│   └── Handles database operations
│
├── Entity
│   └── Defines database entities
│
├── DTO
│   └── Handles API request/response data
│
├── Security
│   └── JWT authentication and authorization
│
└── Exception
    └── Handles application exceptions
```

## Database

BlogSphere uses **MySQL** as its relational database and **JPA/Hibernate** for object-relational mapping.

Example database:

```text
blogsphere
```

The backend stores application data such as:

* Users
* Blog posts
* Authentication-related information
* Other platform entities

## Authentication

BlogSphere uses **JWT (JSON Web Token)** based authentication.

The authentication flow works approximately as follows:

```text
User
  ↓
Register / Login
  ↓
Backend validates credentials
  ↓
JWT Token generated
  ↓
Client sends JWT with requests
  ↓
Spring Security validates token
  ↓
Protected API accessed
```

Protected endpoints require a valid JWT token.

## API Operations

The backend is designed around RESTful APIs.

Typical operations include:

### Authentication

```http
POST /api/auth/register
POST /api/auth/login
```

### Blog Posts

```http
GET    /api/posts
GET    /api/posts/{id}
POST   /api/posts
PUT    /api/posts/{id}
DELETE /api/posts/{id}
```

> Update the endpoint paths above if your current implementation uses different routes.

## Example Request

### Create Post

```json
{
  "title": "Introduction to Spring Boot",
  "content": "Spring Boot makes it easier to build production-ready Java applications."
}
```

### Example Response

```json
{
  "id": 1,
  "title": "Introduction to Spring Boot",
  "content": "Spring Boot makes it easier to build production-ready Java applications."
}
```

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/blogsphere.git
```

### 2. Open the Project

Open the project in:

* IntelliJ IDEA
* Eclipse
* VS Code

Make sure Java and Maven are installed.

### 3. Configure MySQL

Create a database:

```sql
CREATE DATABASE blogsphere;
```

Then configure your database credentials in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/blogsphere
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Do not commit your actual database password or JWT secret to GitHub.

## Running the Application

Using Maven:

```bash
mvn spring-boot:run
```

Or run the main Spring Boot application class directly from IntelliJ IDEA.

The application will normally start at:

```text
http://localhost:8080
```

## Testing the API

You can test the REST APIs using tools such as:

* Postman
* Insomnia
* IntelliJ HTTP Client
* cURL

Example:

```bash
curl http://localhost:8080/api/posts
```

For protected APIs, include the JWT token:

```http
Authorization: Bearer YOUR_JWT_TOKEN
```

## Security

The project uses Spring Security with JWT authentication to protect private endpoints.

Important security practices:

* Passwords should never be stored as plain text.
* JWT secrets should be stored securely.
* Database credentials should not be committed to GitHub.
* Protected endpoints should require authentication.

## Future Improvements

Planned improvements for BlogSphere may include:

* Comments and replies
* Like and bookmark functionality
* User profiles
* Post categories and tags
* Search functionality
* Pagination and sorting
* Image upload
* Email verification
* Password reset
* Admin dashboard
* Swagger/OpenAPI documentation
* Docker support
* Deployment to a cloud platform

## Learning Outcomes

Through this project, I worked with:

* Java backend development
* Spring Boot
* REST API development
* Spring Security
* JWT authentication
* MySQL
* JPA/Hibernate
* Repository pattern
* Layered architecture
* CRUD operations
* API testing
* Backend project structure
* Git and GitHub

## Project Status

**🚧 Currently in development**

BlogSphere is being developed as a backend-focused blogging platform and will be enhanced with additional features over time.

## Author

**Rajat Tiwari**

GitHub: `rajattiwariind`

---

## License

This project is created for learning and development purposes.
