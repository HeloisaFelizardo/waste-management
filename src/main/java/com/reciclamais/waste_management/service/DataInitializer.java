package com.reciclamais.waste_management.service;

import com.reciclamais.waste_management.model.TypeUser;
import com.reciclamais.waste_management.model.User;
import com.reciclamais.waste_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = new User();
            admin.setName("Administrador");
            admin.setEmail("admin@example.com");

            // Gera uma nova senha encriptada
            String encodedPassword = passwordEncoder.encode("admin123");
            System.out.println("Senha encriptada do admin: " + encodedPassword);

            admin.setPassword(encodedPassword);
            admin.setTypeUser(TypeUser.ADMIN);
            userRepository.save(admin);

            System.out.println("Usuário administrador criado com sucesso!");
        }

        // Criar um usuário de teste também
        if (userRepository.findByEmail("test@example.com").isEmpty()) {
            User testUser = new User();
            testUser.setName("Usuário de Teste");
            testUser.setEmail("test@example.com");

            String encodedPassword = passwordEncoder.encode("password123");
            System.out.println("Senha encriptada do usuário de teste: " + encodedPassword);

            testUser.setPassword(encodedPassword);
            testUser.setTypeUser(TypeUser.USER);
            userRepository.save(testUser);

            System.out.println("Usuário de teste criado com sucesso!");
        }
    }
}