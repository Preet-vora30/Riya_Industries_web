package com.riyaindustries.config;

import com.riyaindustries.entity.AdminUser;
import com.riyaindustries.entity.Role;
import com.riyaindustries.repository.AdminUserRepository;
import com.riyaindustries.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Bootstraps the initial ROLE_ADMIN and admin user from environment variables
 * if no admin account exists yet. Never logs or hardcodes credentials.
 */
@Component
public class AdminBootstrapRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminBootstrapRunner.class);

    private final RoleRepository roleRepository;
    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.init-username:admin}")
    private String initUsername;

    @Value("${app.admin.init-email:admin@riyaindustries.example}")
    private String initEmail;

    @Value("${app.admin.init-password:}")
    private String initPassword;

    public AdminBootstrapRunner(
        RoleRepository roleRepository,
        AdminUserRepository adminUserRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
            .orElseGet(() -> {
                Role r = new Role();
                r.setName("ROLE_ADMIN");
                r.setDescription("Full workshop & website administration access");
                return roleRepository.save(r);
            });

        if (adminUserRepository.count() == 0) {
            if (initPassword == null || initPassword.isBlank()) {
                log.warn("No admin user exists and ADMIN_INIT_PASSWORD is not set. Set ADMIN_INIT_PASSWORD to bootstrap the initial admin.");
                return;
            }
            AdminUser admin = new AdminUser();
            admin.setUsername(initUsername);
            admin.setEmail(initEmail);
            admin.setFullName("Workshop Administrator");
            admin.setPasswordHash(passwordEncoder.encode(initPassword));
            admin.setActive(true);
            admin.setRoles(Set.of(adminRole));
            adminUserRepository.save(admin);
            log.info("Initialized default admin user '{}'.", initUsername);
        }
    }
}
