package com.reciclamais.waste_management.controller;

import com.reciclamais.waste_management.dto.TypeWasteDTO;
import com.reciclamais.waste_management.dto.UserRankingDTO;
import com.reciclamais.waste_management.service.WasteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    
    @Autowired
    private final WasteService wasteService;

    public DashboardController(WasteService wasteService) {
        this.wasteService = wasteService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        double totalWaste = wasteService.getTotalWaste();
        double wasteRecycled = wasteService.getWasteRecycled();
        double recyclingRate = wasteService.getRecyclingRate();
        List<TypeWasteDTO> typeWaste = wasteService.getWasteByType();
        List<UserRankingDTO> rankingUsers = wasteService.getUserRankings();

        // Log the data
        logger.info("Total Waste: {}", totalWaste);
        logger.info("Waste Recycled: {}", wasteRecycled);
        logger.info("Recycling Rate: {}", recyclingRate);
        logger.info("Type Waste Data: {}", typeWaste);
        logger.info("User Rankings: {}", rankingUsers);

        // Add attributes to model
        model.addAttribute("totalWaste", String.format("%.1f", totalWaste));
        model.addAttribute("wasteRecycled", String.format("%.1f", wasteRecycled));
        model.addAttribute("recyclingRate", String.format("%.1f", recyclingRate));
        model.addAttribute("typeWaste", typeWaste);
        model.addAttribute("rankingUsers", rankingUsers);

        // Log model attributes
        logger.info("Model attributes: {}", model.asMap());

        return "dashboard";
    }
} 