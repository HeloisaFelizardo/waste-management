package com.reciclamais.waste_management.repository;

import com.reciclamais.waste_management.model.Waste;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WasteRepository extends JpaRepository<Waste, Long> {

}
