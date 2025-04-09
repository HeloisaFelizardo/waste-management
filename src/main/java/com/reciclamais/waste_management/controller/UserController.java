package com.reciclamais.waste_management.controller;

import com.reciclamais.waste_management.model.TypeUser;
import com.reciclamais.waste_management.model.User;
import com.reciclamais.waste_management.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("tiposUsuario", TypeUser.values());
        return "userRegister";
    }

    @PostMapping("/save")
    public String salvarUsuario(@Valid @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "userRegister";
        }
        try {
            userService.registerUser(user);
            return "redirect:/login";
        } catch (RuntimeException e) {
            result.rejectValue("email", "error.user", e.getMessage());
            return "userRegister";
        }
    }
}