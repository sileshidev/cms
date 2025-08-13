package com.cms.auth.service;

import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final Map<String, UserRecord> users = new HashMap<>();
    private final JwtService jwtService;

    public AuthController(
            @Value("${auth.jwt.secret:ZGV2LXNlY3JldC1wbGVhc2UtY2hhbmdlLW1lLXN1cGVyLWxvbmctdW5zYWZl}") String base64Secret,
            @Value("${auth.jwt.ttlSeconds:86400}") long ttlSeconds
    ) {
        this.jwtService = new JwtService(base64Secret, ttlSeconds);
        users.put("admin", new UserRecord("admin", "password", List.of("ADMIN"), "System Admin"));
        users.put("officer", new UserRecord("officer", "password", List.of("OFFICER"), "Field Officer"));
        users.put("auditor", new UserRecord("auditor", "password", List.of("AUDITOR"), "Auditor"));
        users.put("supervisor", new UserRecord("supervisor", "password", List.of("SUPERVISOR"), "Supervisor"));
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}
    public record LoginResponse(String accessToken, long expiresIn, String tokenType, String username, List<String> roles) {}
    public record UserRecord(String username, String password, List<String> roles, String fullName) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        UserRecord u = users.get(req.username());
        if (u == null || !Objects.equals(u.password(), req.password())) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid_credentials"));
        }
        String token = jwtService.generateToken(u.username(), u.fullName(), u.roles());
        return ResponseEntity.ok(new LoginResponse(token, 86400, "Bearer", u.username(), u.roles()));
    }

    @GetMapping("/users")
    public List<Map<String, Object>> listUsers() {
        List<Map<String, Object>> out = new ArrayList<>();
        for (UserRecord u : users.values()) {
            out.add(Map.of(
                    "username", u.username(),
                    "fullName", u.fullName(),
                    "roles", u.roles()
            ));
        }
        return out;
    }
}
