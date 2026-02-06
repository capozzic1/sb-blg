package com.example.demo.blog.security;

import java.util.List;

public record AuthUserDto(Long id, String username, String email, List<String> roles) { }

