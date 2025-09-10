package szeliga71.pl.wp.galeriawnetrz_ver1.securityConfig;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Role;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.AppUser;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.RoleRepository;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Component
public class DataInitializer {

    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.super.username:superadmin}")
    private String superUsername;

    @Value("${app.super.password:superpassword}")
    private String superPassword;

    public DataInitializer(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        Role r1 = roleRepo.findByName("ROLE_SUPERADMIN")
                .orElseGet(() -> roleRepo.save(createRole("ROLE_SUPERADMIN")));
        Role r2 = roleRepo.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepo.save(createRole("ROLE_ADMIN")));

        if (!userRepo.existsByUsername(superUsername)) {
            AppUser u = new AppUser();
            u.setUsername(superUsername);
            u.setPassword(passwordEncoder.encode(superPassword));
            u.setRoles(Set.of(r1));
            userRepo.save(u);
            System.out.println("Utworzono SUPERADMIN: " + superUsername + " / " + superPassword);
        }
    }

    private Role createRole(String name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }
}
