package com.armi.infrastructure.adapter.in.web;

import com.armi.domain.port.in.UserUseCase;
import com.armi.model.AppUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserWebAdapter {

    private final UserUseCase userUseCase;

    public UserWebAdapter(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        return ResponseEntity.ok(userUseCase.getAllUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody AppUser user) {
        try {
            AppUser created = userUseCase.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/users/bulk")
    public ResponseEntity<List<AppUser>> createUsersBulk(@RequestBody List<AppUser> users) {
        return ResponseEntity.ok(userUseCase.createUsersBulk(users));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body("Debe proporcionar correo y contraseña");
        }

        Optional<AppUser> userOpt = userUseCase.login(email, password);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas o usuario no registrado por el administrador");
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userUseCase.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users/deduplicate")
    public ResponseEntity<String> deduplicateUsers() {
        List<AppUser> allUsers = userUseCase.getAllUsers();
        java.util.Set<String> seenEmails = new java.util.HashSet<>();
        int deleted = 0;

        for (AppUser u : allUsers) {
            String emailKey = u.getEmail() != null ? u.getEmail().trim().toLowerCase() : "";
            if (emailKey.isEmpty()) continue;

            if (seenEmails.contains(emailKey)) {
                userUseCase.deleteUser(u.getId());
                deleted++;
            } else {
                seenEmails.add(emailKey);
            }
        }

        return ResponseEntity.ok("Eliminados " + deleted + " usuarios duplicados.");
    }
}
