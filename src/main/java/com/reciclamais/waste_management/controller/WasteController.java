package com.reciclamais.waste_management.controller;

import com.reciclamais.waste_management.model.Waste;
import com.reciclamais.waste_management.service.WasteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/waste-register")
public class WasteController {

    @GetMapping
    public String cadastrar(Model model) {
        model.addAttribute("titulo", "Teste");
        return "waste";  // Aqui o Spring vai procurar pelo arquivo waste.html
    }

    /*public void registerWaste(Waste waste) {
        WasteService wasteService = new WasteService();
        wasteService.save(waste);
    }*/
}
