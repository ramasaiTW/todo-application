
# TaskMaster Application

Welcome to our TaskMaster! - TaskMaster is a productivity tool designed to help individuals and teams manage their tasks and projects efficiently. With features like user login, task and project creation, updating, and deletion, TaskMaster aims to streamline your workflow and boost productivity.

In TaskMaster, users can create accounts and log in securely to access their tasks and projects. Once logged in, users can create new tasks, assign them to specific projects, set deadlines, and track progress. They can also create new projects, organize tasks within projects, and collaborate with team members.

## Meet the Team:

As a member of TaskMaster's development team, you'll play a crucial role in enhancing the application's features and user experience. With your expertise, we aim to deliver a seamless and intuitive task management solution to our users.

## Your Mission:

TaskMaster is like a special tool to help people get things done. You and your team are here to make it even better! Even though some team members are out, you and your buddy will keep things moving forward.

#### Here's what you'll do:

***Make it easy:*** People should be able to use TaskMaster without any trouble.

***Add cool stuff:*** Think about what would make TaskMaster even more helpful.

***Fix Bugs:*** Sometimes things don't work the way they should. Your job is to find those problems and fix them so TaskMaster runs smoothly for everyone.

## Requirements:
This project is developed using Java 17 and Spring Boot 3, and it utilizes Maven for dependency management. Swagger is integrated into the project for API documentation and testing. Data will store on SQLite

- Java 17 or higher.
- Spring Boot 3
- Spring Security
- Maven
- JWT
- Swagger UI
- SQLite

## Useful Maven Commands
The project uses Maven for dependency management and building. Here are some useful
Maven commands:

### Build the project
To compile the project and create an executable JAR file, run:
```
mvn clean install
```
### Run the application
To run the application using the executable JAR file produced by the Maven build task, run:
```
java -jar target/taskmaster-java.jar
```
### Swagger API Documentation
Swagger is integrated into the project for API documentation and testing. You can access the Swagger UI by navigating to:
```
http://localhost:8080/swagger-ui/index.html
```
### Run tests
To run the unit tests only, run:
```
mvn test
```
To run functional tests only, run:
```
mvn failsafe:integration-test
```
To run both unit and functional tests, run:
```
mvn verify
```

## API Reference
Below is a list of API endpoints with their respective input and output. Please note that the application needs to be running for the following endpoints to work. For more information about how to run the application, please refer to run the application section above.

#### Accessing APIs
- JWT Token is not required for Signup and Login APIs.
- Other APIs required JWT Token.
- Login APIs will give the JWT Token, you can use this JWT Token to access other APIs
- Include the JWT token: Add the received JWT token to the request header for subsequent API calls.
```
Authorization: "Bearer <JWT Token>"
```

### User Controllers
#### Signup Request
This endpoint allows users to create a new resource.
```
POST /api/v1/auth/signup
```
Request body
```
{
  "firstName": "<string>",
  "lastName": "<string>",
  "email": "<string>",
  "password": "<string>"
}
```
| Parameter   | Type     | Description                                    |
|:------------|:---------|:-----------------------------------------------|
| `firstName` | `string` | **Required**. Enter the first name of the user |
| `lastName`  | `string` | **Required**. Enter the last name of the user  |
| `email`     | `string` | **Required**. Enter the email of the user      |
| `password`  | `string` | **Required**. Enter the password of the user   |

#### Login Request
This endpoint allows users to login.
```
POST /api/v1/auth/login
```
Request body
```
{
  "email": "<string>",
  "password": "<string>"
}
```
| Parameter  | Type     | Description                                  |
|:-----------|:---------|:---------------------------------------------|
| `email`    | `string` | **Required**. Enter the email of the eser    |
| `password` | `string` | **Required**. Enter the password of the eser |

Response:
```
{
  "token": "<JWT Token>",
  "type": "Bearer",
  "id": <int>,
  "firstName": "<string>",
  "lastName": "<string>",
  "email": "<string>",
  "roles":[
    "<string>"
  ]
}
```
### Task Controllers
#### Create Task
This endpoint allows users to create a task into the application.

**Headers Required:**
- `Content-Type: application/json`
- `Authorization: Bearer <Jwt Token>`

```
PUT /api/v1/tasks/{id}
```

#### Update Task
This endpoint allows users to update a task in the application.

**Headers Required:**
- `Content-Type: application/json`
- `Authorization: Bearer <Jwt Token>`
```
POST /api/v1/tasks
```

#### Get All Tasks
This endpoint allows users to get all tasks in the application.

**Headers Required:**
- `Authorization: Bearer <Jwt Token>`
```
GET /api/v1/tasks
```

#### Get Task By Id
This endpoint allows users to get a task by ID in the application.

**Headers Required:**
- `Authorization: Bearer <Jwt Token>`
```
GET /api/v1/tasks/{id}
```

#### Delete Task By Id
This endpoint allows users to delete a task by ID in the application.

**Headers Required:**
- `Authorization: Bearer <Jwt Token>`
```
DELETE /api/v1/tasks/{id}
```

### Project Controllers
#### Create Project
This endpoint allows users to create a project into the application.

**Headers Required:**
- `Content-Type: application/json`
- `Authorization: Bearer <Jwt Token>`
```
POST /api/v1/projects
```

#### Update Project
This endpoint allows users to update a project in the application.

**Headers Required:**
- `Content-Type: application/json`
- `Authorization: Bearer <Jwt Token>`
```
PUT /api/v1/projects/{id}
```

#### Get All Projects
This endpoint allows users to get all projects in the application.

**Headers Required:**
- `Authorization: Bearer <Jwt Token>`
```
GET /api/v1/projects
```

#### Get Project By ID
This endpoint allows users to get a project by ID in the application.

**Headers Required:**
- `Authorization: Bearer <Jwt Token>`
```
GET /api/v1/projects/{id}
```

#### Delete Project By Id
This endpoint allows users to delete a project by ID in the application.

**Headers Required:**
- `Authorization: Bearer <Jwt Token>`
```
DELETE /api/v1/projects/{id}
```
