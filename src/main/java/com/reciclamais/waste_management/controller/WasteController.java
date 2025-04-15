package com.reciclamais.waste_management.controller;

import com.reciclamais.waste_management.model.Waste;
import com.reciclamais.waste_management.service.WasteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/waste")
public class WasteController {

    @Autowired
    private WasteService wasteService;

    @GetMapping("/register")
    public String cadastrar(Model model) {
        model.addAttribute("waste", new Waste());
        model.addAttribute("titulo", "Registro de Resíduo");
        return "waste/register";  // Aqui o Spring vai procurar pelo arquivo waste/register.html
    }

    @PostMapping("/register")
    public String registerWaste(@ModelAttribute Waste waste, 
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        try {
            // Define a data atual
            waste.setDate(LocalDate.now());
            
            // Salva o resíduo
            wasteService.save(waste, userDetails.getUsername());
            
            redirectAttributes.addFlashAttribute("message", "Resíduo registrado com sucesso!");
            redirectAttributes.addFlashAttribute("messageType", "alert-success");
            
            return "redirect:/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Erro ao registrar resíduo: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "alert-danger");
            return "redirect:/waste/register";
        }
    }

    /*public void registerWaste(Waste waste) {
        WasteService wasteService = new WasteService();
        wasteService.save(waste);
    }*/
}
