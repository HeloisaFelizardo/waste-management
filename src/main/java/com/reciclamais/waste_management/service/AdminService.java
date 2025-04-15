package com.reciclamais.waste_management.service;

import com.reciclamais.waste_management.model.TypeUser;
import com.reciclamais.waste_management.model.User;
import com.reciclamais.waste_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${security.admin.email:admin@reciclamais.com}")
    private String adminEmail;

    @Value("${security.admin.initial-password:admin123}")
    private String adminInitialPassword;

    public void initializeAdminIfNotExists() {
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setName("Administrador");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminInitialPassword));
            admin.setTypeUser(TypeUser.ADMIN);
            userRepository.save(admin);
        }
    }

    public void changeAdminPassword(String currentPassword, String newPassword) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Administrador não encontrado"));

        if (!passwordEncoder.matches(currentPassword, admin.getPassword())) {
            throw new RuntimeException("Senha atual incorreta");
        }

        admin.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(admin);
    }
} 