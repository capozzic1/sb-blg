package com.example.demo.blog.security;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginController(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    record AuthRequest(String username, String password) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req, HttpServletResponse response) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        Optional<User> uOpt = userRepository.findByUsername(req.username());
        if (uOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error","invalid credentials"));
        }
        User u = uOpt.get();
        String token = jwtUtil.generateToken(u.getUsername(), u.getRoles());

        // Build HttpOnly cookie. maxAge in seconds
        long maxAgeSeconds = jwtUtil.getExpirationMs() / 1000L;
        ResponseCookie cookie = ResponseCookie.from("JWT", token)
                .httpOnly(true)
                .secure(false) // set to true in production (requires https)
                .path("/")
                .maxAge(maxAgeSeconds)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(Map.of("message","login successful"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("JWT", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        return ResponseEntity.ok().header("Set-Cookie", cookie.toString()).body(Map.of("message","logged out"));
    }

    // GET /api/auth/me - returns currently authenticated user
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String username = authentication.getName();
        Optional<User> uOpt = userRepository.findByUsername(username);
        if (uOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        User u = uOpt.get();
        List<String> roles = u.getRoles().stream().toList();
        // Note: the current User entity has no email field. If you add one, return it here.
        String email = null;
        AuthUserDto dto = new AuthUserDto(u.getId(), u.getUsername(), email, roles);
        return ResponseEntity.ok(dto);
    }
}
