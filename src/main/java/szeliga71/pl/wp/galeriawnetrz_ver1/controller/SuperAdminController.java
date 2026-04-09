package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.UserCreateDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.AppUser;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/super")
@RequiredArgsConstructor // Automatyczny konstruktor dla UserService
public class SuperAdminController {

    private final UserService userService;

    @DeleteMapping("/user/id/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    // Zmieniłem endpoint, aby nie kolidował z Long id (ambiguous mapping)
    @DeleteMapping("/user/name/{username}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Void> deleteUserByName(@PathVariable String username) {
        userService.deleteUserByName(username);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<String> createUser(@RequestBody UserCreateDto req) {
        AppUser saved = userService.createUser(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved.getUsername());
    }

    @PutMapping("/users/{id}/roles")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Void> setRoles(@PathVariable Long id, @RequestBody List<String> roles) {
        userService.assignRolesToUser(id, roles);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<AppUser> users = userService.getAllUsers();

        // Używamy streamów dla czystszego kodu zamiast pętli for z HashMapami
        List<Map<String, Object>> result = users.stream().map(user -> Map.of(
                "id", (Object) user.getId(),
                "username", user.getUsername(),
                "roles", user.getRoles()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}