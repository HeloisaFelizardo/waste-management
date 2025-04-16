package com.reciclamais.waste_management.service;

import com.reciclamais.waste_management.model.Waste;
import com.reciclamais.waste_management.model.User;
import com.reciclamais.waste_management.repository.WasteRepository;
import com.reciclamais.waste_management.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WasteService {

    private static final Logger logger = LoggerFactory.getLogger(WasteService.class);
    
    private final WasteRepository wasteRepository;
    private final UserRepository userRepository;

    public WasteService(WasteRepository wasteRepository, UserRepository userRepository) {
        this.wasteRepository = wasteRepository;
        this.userRepository = userRepository;
        logger.info("WasteService construído com sucesso");
    }

    public void save(Waste waste, String userEmail) {
        logger.info("Tentando salvar resíduo para o usuário: {}", userEmail);
        
        // Verificar se o email está vazio
        if (userEmail == null || userEmail.trim().isEmpty()) {
            logger.error("Email do usuário está vazio");
            throw new RuntimeException("Email do usuário não pode ser vazio");
        }
        
        // Buscar o usuário
        Optional<User> userOpt = userRepository.findByEmail(userEmail);
        logger.info("Usuário encontrado: {}", userOpt.isPresent());
        
        User user = userOpt.orElseThrow(() -> {
            logger.error("Usuário não encontrado com o email: {}", userEmail);
            return new RuntimeException("Usuário não encontrado");
        });
        
        logger.info("Usuário encontrado: {}", user.getName());
        waste.setUser(user);
        
        try {
            wasteRepository.save(waste);
            logger.info("Resíduo salvo com sucesso: {}", waste);
        } catch (Exception e) {
            logger.error("Erro ao salvar resíduo: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao salvar resíduo: " + e.getMessage());
        }
    }

    public List<Waste> findAll() {
        logger.info("Buscando todos os resíduos");
        return wasteRepository.findAll();
    }
}