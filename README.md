# Sample app with Spring Security and JWT token authentication

Login with /app/login, all pages with app/secure/** will be authenticated via JWT token.

### Prerequisite
- Java 11
- Gradle 6

### Build
gradle clean build

### Run
java -jar build/libs/spring-security-jwt-0.1.jar --spring.config.location=src/main/resources/application.properties

#### Login 
http://localhost:8080/app/login 

user:user1, password:{bcrypt}user1
