package com.example.demo.blog;

import com.example.demo.blog.dto.PostDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public PostDto create(PostDto dto) {
        Post post = PostMapper.toEntity(dto);
        Post saved = repository.save(post);
        return PostMapper.toDto(saved);
    }

    public List<PostDto> findAll() {
        return repository.findAll().stream().map(PostMapper::toDto).collect(Collectors.toList());
    }

    public Optional<PostDto> findById(Long id) {
        return repository.findById(id).map(PostMapper::toDto);
    }

    public Optional<PostDto> update(Long id, PostDto dto) {
        return repository.findById(id).map(existing -> {
            existing.setTitle(dto.getTitle());
            existing.setContent(dto.getContent());
            existing.setAuthor(dto.getAuthor());
            Post saved = repository.save(existing);
            return PostMapper.toDto(saved);
        });
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
