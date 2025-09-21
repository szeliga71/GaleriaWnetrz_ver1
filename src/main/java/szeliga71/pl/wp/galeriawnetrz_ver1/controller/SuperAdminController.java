package szeliga71.pl.wp.galeriawnetrz_ver1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.CreateUserRequest;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.AppUser;
import szeliga71.pl.wp.galeriawnetrz_ver1.service.UserService;

@RestController
@RequestMapping("/api/admin/super")
public class SuperAdminController {

    private final UserService userService;

    public SuperAdminController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{username}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Void> deleteUserByName(@PathVariable String username) {
        userService.deleteUserByName(username);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest req) {
        AppUser saved = userService.createUser(req);
        return ResponseEntity.status(201).body(saved.getUsername());
    }

    @PutMapping("/users/{id}/roles")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<?> setRoles(@PathVariable Long id, @RequestBody java.util.List<String> roles) {
        userService.assignRolesToUser(id, roles);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<?> getAllUsers() {
        java.util.List<AppUser> users = userService.getAllUsers();


        java.util.List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();
        for (AppUser user : users) {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", user.getId());
            map.put("username", user.getUsername());
            map.put("roles", user.getRoles());
            result.add(map);
        }

        return ResponseEntity.ok(result);
    }
}

