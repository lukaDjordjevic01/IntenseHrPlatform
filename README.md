# HR Platform for Job Candidates and Skills
This is a Java (Maven) project for an HR platform that allows adding, updating, deleting, and searching job candidates and their skills.

## Author
Luka Đorđević

## Swagger
While the app is running you can access swagger specification here: http://localhost:8080/swagger-ui/index.html#/

## Requirements
Each job candidate has a name, date of birth, contact number, email, and a list of skills they possess. Each skill has a name.

The project should have the following functionalities:

1. Create database model and appropriate tables for job candidates and skills (you can use any relational database you prefer).</br>
2. Create appropriate Java DAO and service classes to represent these functionalities.</br>
3. Expose these functionalities through REST web services with the following operations:</br>
    - Add job candidate</br>
    - Add skills</br>
    - Update job candidate with skill</br>
    - Remove skill from candidate</br>
    - Remove candidate</br>
    - Search candidate by name</br>
    - Search all candidates with a given skill(s) (e.g. Java programming)</br>
  
## Technologies
The project uses the following technologies:
- Java 8+
- Maven
- Spring Boot
- Spring Data JPA
- H2 database (can be easily replaced with any other relational database)
