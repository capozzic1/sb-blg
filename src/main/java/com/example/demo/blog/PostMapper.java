package com.example.demo.blog;

import com.example.demo.blog.dto.PostDto;

public class PostMapper {

    public static PostDto toDto(Post p) {
        if (p == null) return null;
        return new PostDto(p.getId(), p.getTitle(), p.getContent(), p.getAuthor(), p.getCreatedAt());
    }

    public static Post toEntity(PostDto dto) {
        if (dto == null) return null;
        Post p = new Post();
        p.setTitle(dto.getTitle());
        p.setContent(dto.getContent());
        p.setAuthor(dto.getAuthor());
        // Do NOT copy createdAt from incoming DTO — createdAt is managed by the backend
        return p;
    }
}
