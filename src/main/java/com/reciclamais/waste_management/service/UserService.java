
package com.reciclamais.waste_management.service;

import com.reciclamais.waste_management.model.User;
//import com.reciclamais.waste_management.repository.UserRepository;
import com.reciclamais.waste_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        // Verifica se o usuário já existe
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Usuário já cadastrado com este e-mail.");
        }

        // Criptografa a senha antes de salvar
        String hashedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(hashedPassword);

        // Salva o usuário no banco de dados
        return userRepository.save(user);
    }
}

