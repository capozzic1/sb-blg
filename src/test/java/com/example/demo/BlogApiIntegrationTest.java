package com.example.demo;

import com.example.demo.blog.post.PostDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BlogApiIntegrationTest {

    @LocalServerPort
    private int port;

    // Use RestTemplate to avoid unresolved TestRestTemplate in some IDE setups
    private final RestTemplate rest = new RestTemplate();

    private String url(String path) {
        return "http://localhost:" + port + "/api/posts" + path;
    }

    @Test
    public void fullCrudFlow() {
        // create
        PostDto create = new PostDto(null, "First Post", "Hello world", "Alice", null);
        ResponseEntity<PostDto> resp = rest.postForEntity(url(""), create, PostDto.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        PostDto created = resp.getBody();
        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();

        Long id = created.getId();

        // get
        ResponseEntity<PostDto> getResp = rest.getForEntity(url("/" + id), PostDto.class);
        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResp.getBody()).isNotNull();
        assertThat(getResp.getBody().getTitle()).isEqualTo("First Post");

        // list
        ResponseEntity<PostDto[]> listResp = rest.getForEntity(url(""), PostDto[].class);
        assertThat(listResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<PostDto> posts = Arrays.asList(Objects.requireNonNull(listResp.getBody()));
        assertThat(posts).isNotEmpty();

        // update
        PostDto update = new PostDto(null, "Updated", "Updated content", "Bob", null);
        HttpEntity<PostDto> req = new HttpEntity<>(update);
        ResponseEntity<PostDto> updateResp = rest.exchange(url("/" + id), HttpMethod.PUT, req, PostDto.class);
        assertThat(updateResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResp.getBody()).isNotNull();
        assertThat(updateResp.getBody().getTitle()).isEqualTo("Updated");

        // delete
        ResponseEntity<Void> delResp = rest.exchange(url("/" + id), HttpMethod.DELETE, null, Void.class);
        assertThat(delResp.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // not found after delete
        ResponseEntity<PostDto> afterDel = rest.getForEntity(url("/" + id), PostDto.class);
        assertThat(afterDel.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
