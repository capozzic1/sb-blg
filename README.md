# Simple Blog REST API (Spring Boot)

This project provides a minimal blog REST API (Post entity) backed by an in-memory H2 database for development and testing. It's written with Spring Boot, Spring Data JPA, and includes a small integration test that exercises full CRUD.

What I added
- Entity: `com.example.demo.blog.Post`
- Repository: `com.example.demo.blog.PostRepository`
- Service: `com.example.demo.blog.PostService`
- Mapper: `com.example.demo.blog.PostMapper`
- DTO: `com.example.demo.blog.dto.PostDto`
- Controller: `com.example.demo.blog.PostController` (endpoints under `/api/posts`)
- H2 configuration in `src/main/resources/application.properties`
- Integration test: `src/test/java/com/example/demo/BlogApiIntegrationTest.java`

Run locally (Windows cmd.exe)
- Run tests:
  mvnw.cmd test

- Start the application:
  mvnw.cmd spring-boot:run

- API endpoints (after starting):
  - List: GET  http://localhost:8080/api/posts
  - Get:  GET  http://localhost:8080/api/posts/{id}
  - Create: POST http://localhost:8080/api/posts  (JSON body matching PostDto)
  - Update: PUT  http://localhost:8080/api/posts/{id}
  - Delete: DELETE http://localhost:8080/api/posts/{id}

Example curl (create):

curl -v -H "Content-Type: application/json" -d "{\"title\":\"Hello\",\"content\":\"World\",\"author\":\"Alice\"}" http://localhost:8080/api/posts

H2 console
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:demo` (configured in application.properties)
- User: `sa`, password: empty

Switching to MySQL (when ready)
1. Update `src/main/resources/application.properties`:
   - Set spring.datasource.url to your MySQL URL (e.g. `jdbc:mysql://localhost:3306/demo?useSSL=false&serverTimezone=UTC`)
   - Set `spring.datasource.username` and `spring.datasource.password`
   - Set `spring.jpa.hibernate.ddl-auto=update` (or `validate` in production)
2. The `mysql-connector-j` dependency is already included in `pom.xml` (runtime scope).

Notes, warnings and next steps
- Static analysis reported a dependency security warning: the transitive `org.assertj:assertj-core` brought in by `spring-boot-starter-test` triggered a vulnerability advisory. You can address this by upgrading or overriding the assertj-core version, or excluding it and adding a safe version. Tell me if you want me to apply a fix.

- I attempted to run `mvnw.cmd test` from this environment, but the terminal couldn't be created here. Please run the test command locally (above). If you'd like, I can try again if you enable the terminal or allow execution.

Status (requirements coverage)
- Simple blog REST API (H2): Done
- JPA repository and service wired: Done
- Integration test for CRUD: Created (not executed here due to terminal limitations)
- Prepared for switch to MySQL: Done (pom contains mysql connector + instructions)

If you'd like I can:
- Run the tests here if you enable terminal execution
- Apply an explicit fix for the AssertJ CVE by overriding/excluding the dependency
- Add more fields to Post (tags, comments) or paging/sorting to the list endpoint

Tell me which follow-up you'd like and I'll proceed.
