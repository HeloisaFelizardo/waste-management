package com.reciclamais.waste_management.controller;

import com.reciclamais.waste_management.model.TypeUser;
import com.reciclamais.waste_management.model.User;
import com.reciclamais.waste_management.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String exibirFormularioCadastro(Model model) {
        logger.info("Exibindo formulário de cadastro");
        model.addAttribute("user", new User());
        return "userRegister";
    }

    @PostMapping("/save")
    public String salvarUsuario(@Valid @ModelAttribute("user") User user, BindingResult result) {
        logger.info("Tentando salvar usuário: {}", user.getEmail());
        
        if (result.hasErrors()) {
            logger.warn("Erros de validação encontrados: {}", result.getAllErrors());
            return "userRegister";
        }
        
        try {
            // Define o tipo de usuário como USER automaticamente
            user.setTypeUser(TypeUser.USER);
            logger.info("Tipo de usuário definido como USER");
            
            userService.registerUser(user);
            logger.info("Usuário registrado com sucesso: {}", user.getEmail());
            
            return "redirect:/login";
        } catch (RuntimeException e) {
            logger.error("Erro ao registrar usuário: {}", e.getMessage());
            result.rejectValue("email", "error.user", e.getMessage());
            return "userRegister";
        }
    }
}