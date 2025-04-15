package com.reciclamais.waste_management.service;

import com.reciclamais.waste_management.model.Waste;
import com.reciclamais.waste_management.model.User;
import com.reciclamais.waste_management.repository.WasteRepository;
import com.reciclamais.waste_management.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WasteService {

    private final WasteRepository wasteRepository;
    private final UserRepository userRepository;

    public WasteService(WasteRepository wasteRepository, UserRepository userRepository) {
        this.wasteRepository = wasteRepository;
        this.userRepository = userRepository;
    }

    public void save(Waste waste, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        waste.setUser(user);
        wasteRepository.save(waste);
    }

    public List<Waste> findAll() {
        return wasteRepository.findAll();
    }
}