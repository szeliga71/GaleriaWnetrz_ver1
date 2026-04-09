/*package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.CreateUserRequest;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.AppUser;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Role;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.RoleRepository;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service

public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AppUser createUser(CreateUserRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("User exist !");
        }

        AppUser user = new AppUser();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        Set<Role> roles = new HashSet<>();
        if (req.getRoles() != null) {
            for (String r : req.getRoles()) {
                String roleName = r.startsWith("ROLE_") ? r : "ROLE_" + r;
                Role role = roleRepo.findByName(roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Role not exist: " + roleName));
                roles.add(role);
            }
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Transactional
    public void assignRolesToUser(Long userId, List<String> rolesNames) {
        AppUser user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Set<Role> roles = new HashSet<>();
        for (String r : rolesNames) {
            String roleName = r.startsWith("ROLE_") ? r : "ROLE_" + r;
            Role role = roleRepo.findByName(roleName).orElseThrow(() -> new IllegalArgumentException("Role not exist: " + roleName));
            roles.add(role);
        }
        user.setRoles(roles);
        userRepository.save(user);
    }
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteUserByName(String username) {
        userRepository.deleteByUsername(username);
    }
}*/
package szeliga71.pl.wp.galeriawnetrz_ver1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import szeliga71.pl.wp.galeriawnetrz_ver1.dto.UserCreateDto;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.AppUser;
import szeliga71.pl.wp.galeriawnetrz_ver1.model.Role;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.RoleRepository;
import szeliga71.pl.wp.galeriawnetrz_ver1.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AppUser createUser(UserCreateDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("User already exists!");
        }

        AppUser user = new AppUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(mapRoles(dto.getRoles()));

        return userRepository.save(user);
    }

    @Transactional
    public void assignRolesToUser(Long userId, List<String> rolesNames) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setRoles(mapRoles(rolesNames));
        userRepository.save(user);
    }

    // Metoda pomocnicza do mapowania ról ze Stringów na obiekty Role
    private Set<Role> mapRoles(List<String> rolesNames) {
        if (rolesNames == null) return new HashSet<>();

        return rolesNames.stream()
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                .map(roleName -> roleRepo.findByName(roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Role does not exist: " + roleName)))
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteUserByName(String username) {
        userRepository.deleteByUsername(username);
    }
}
