package com.reciclamais.waste_management.controller;

import com.reciclamais.waste_management.model.Waste;
import com.reciclamais.waste_management.model.Type;
import com.reciclamais.waste_management.service.WasteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class TestDataController {

    private final WasteService wasteService;

    public TestDataController(WasteService wasteService) {
        this.wasteService = wasteService;
    }

    @GetMapping("/test/generate-data")
    public String generateTestData(Authentication authentication, RedirectAttributes redirectAttributes) {
        String userEmail = authentication.getName();
        
        // Dados dos últimos 3 meses
        LocalDate now = LocalDate.now();
        
        // Mês atual
        Waste waste1 = new Waste();
        waste1.setDate(now);
        waste1.setWeight(20.0);
        waste1.setRecycled(true);
        waste1.setType(Type.PLASTICO);
        waste1.setDescription("Resíduos plásticos diversos coletados no mês atual");
        wasteService.save(waste1, userEmail);

        // Mês anterior
        Waste waste2 = new Waste();
        waste2.setDate(now.minusMonths(1));
        waste2.setWeight(15.0);
        waste2.setRecycled(true);
        waste2.setType(Type.PAPEL);
        waste2.setDescription("Papéis e papelões coletados no mês anterior");
        wasteService.save(waste2, userEmail);

        // Dois meses atrás
        Waste waste3 = new Waste();
        waste3.setDate(now.minusMonths(2));
        waste3.setWeight(10.0);
        waste3.setRecycled(true);
        waste3.setType(Type.VIDRO);
        waste3.setDescription("Garrafas e frascos de vidro coletados há dois meses");
        wasteService.save(waste3, userEmail);

        redirectAttributes.addFlashAttribute("message", "Dados de teste gerados com sucesso!");
        return "redirect:/dashboard";
    }
} 