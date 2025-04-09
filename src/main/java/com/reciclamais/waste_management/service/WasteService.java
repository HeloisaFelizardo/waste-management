package com.reciclamais.waste_management.service;

import com.reciclamais.waste_management.model.Waste;
import com.reciclamais.waste_management.repository.WasteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WasteService {

    private final WasteRepository wasteRepository;

    public WasteService(WasteRepository wasteRepository) {
        this.wasteRepository = wasteRepository;
    }

    public void save(Waste waste) {
        wasteRepository.save(waste);
    }

    public List<Waste> findAll() {
        return wasteRepository.findAll();
    }
}