package com.reciclamais.waste_management.service;

import com.reciclamais.waste_management.model.TypeUser;
import com.reciclamais.waste_management.model.User;
import com.reciclamais.waste_management.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${security.admin.email:admin@reciclamais.com}")
    private String adminEmail;

    @Value("${security.admin.initial-password:admin123}")
    private String adminPassword;

    @Autowired
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        logger.info("DataInitializer construído com sucesso");
    }

    @Override
    public void run(ApplicationArguments args) {
        logger.info("Iniciando DataInitializer");
        
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            logger.info("Criando usuário administrador");
            User admin = new User();
            admin.setName("Administrador");
            admin.setEmail(adminEmail);

            // Gera uma nova senha encriptada
            String encodedPassword = passwordEncoder.encode(adminPassword);
            logger.info("Senha encriptada do admin: {}", encodedPassword);

            admin.setPassword(encodedPassword);
            admin.setTypeUser(TypeUser.ADMIN);
            userRepository.save(admin);

            logger.info("Usuário administrador criado com sucesso!");
        } else {
            logger.info("Usuário administrador já existe");
        }

        // Criar um usuário de teste também
        if (userRepository.findByEmail("test@example.com").isEmpty()) {
            logger.info("Criando usuário de teste");
            User testUser = new User();
            testUser.setName("Usuário de Teste");
            testUser.setEmail("test@example.com");

            String encodedPassword = passwordEncoder.encode("password123");
            logger.info("Senha encriptada do usuário de teste: {}", encodedPassword);

            testUser.setPassword(encodedPassword);
            testUser.setTypeUser(TypeUser.USER);
            userRepository.save(testUser);

            logger.info("Usuário de teste criado com sucesso!");
        } else {
            logger.info("Usuário de teste já existe");
        }
        
        logger.info("DataInitializer concluído");
    }
}