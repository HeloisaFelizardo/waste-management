package com.reciclamais.waste_management.service;

import com.reciclamais.waste_management.model.User;
//import com.reciclamais.waste_management.repository.UserRepository;
import com.reciclamais.waste_management.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        logger.info("Tentando registrar usuário: {}", user.getEmail());
        
        // Verifica se o usuário já existe
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            logger.warn("Usuário já existe com o email: {}", user.getEmail());
            throw new RuntimeException("Usuário já cadastrado com este e-mail.");
        }

        // Criptografa a senha antes de salvar
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        
        logger.info("Senha criptografada com sucesso para o usuário: {}", user.getEmail());

        // Salva o usuário no banco de dados
        User savedUser = userRepository.save(user);
        logger.info("Usuário registrado com sucesso: {}", savedUser.getEmail());
        
        return savedUser;
    }
}

