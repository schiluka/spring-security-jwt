# Sample app with Spring Security and JWT token authentication

### Prerequisite
- Java 11
- Gradle 6

### Build
gradle clean build

### Run
java -jar build/libs/spring-security-jwt-0.1.jar --spring.config.location=./application.properties

#### Login 
http://localhost:8080/app/login 

user:user1, password:{bcrypt}user1
