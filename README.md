# Demo Blog API (Spring Boot)

A simple blog REST API built with Spring Boot, Spring Data JPA, Lombok and JWT-based authentication.

This project is intended as a small starter/demo application that stores posts and secures write operations with JWT authentication (HttpOnly cookies). It was developed to run with MySQL for persistent storage and can be switched to H2 for local testing.

Key features
- Spring Boot REST API
- Spring Data JPA repositories
- JWT authentication
- Lombok for boilerplate reduction
- MySQL (production) / H2 (local) support

Tech stack
- Java 21
- Spring Boot
- MySQL database 


mvn "-Dspring-boot.run.profiles=local" "-Dmaven.test.skip=true" spring-boot:run

debugger
mvn spring-boot:run "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005'"



