package com.reciclamais.waste_management.controller;

import com.reciclamais.waste_management.model.Waste;
import com.reciclamais.waste_management.service.WasteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/**
 * Controller responsável por gerenciar as operações relacionadas a resíduos.
 * Este controller lida com o registro e visualização de resíduos no sistema.
 */
@Controller
@RequestMapping("/waste")
public class WasteController {

    private static final Logger logger = LoggerFactory.getLogger(WasteController.class);
    private static final String REGISTER_VIEW = "waste/register";
    private static final String DASHBOARD_REDIRECT = "redirect:/dashboard";
    private static final String TITULO_ATTR = "titulo";
    private static final String TITULO_VALUE = "Registro de Resíduo";

    @Autowired
    private WasteService wasteService;

    /**
     * Exibe o formulário de registro de resíduo.
     * Este método é chamado quando o usuário acessa a página de registro.
     * 
     * Fluxo:
     * 1. Cria um novo objeto Waste
     * 2. Define a data atual no objeto
     * 3. Adiciona o objeto ao modelo para ser usado no formulário
     * 4. Retorna a view do formulário
     *
     * @param model Modelo Spring para passar dados para a view
     * @return String Nome da view a ser renderizada
     */
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        logger.info("Acessando página de registro de resíduo");
        
        Waste waste = new Waste();
        waste.setDate(LocalDate.now());
        logger.info("Data definida no objeto Waste: {}", waste.getDate());
        
        model.addAttribute("waste", waste);
        model.addAttribute(TITULO_ATTR, TITULO_VALUE);
        
        return REGISTER_VIEW;
    }

    /**
     * Processa o envio do formulário de registro de resíduo.
     * Este método é chamado quando o usuário submete o formulário.
     * 
     * Fluxo:
     * 1. Valida os dados do formulário
     * 2. Se houver erros de validação, retorna ao formulário
     * 3. Verifica a autenticação do usuário
     * 4. Salva o resíduo no banco de dados
     * 5. Redireciona para o dashboard em caso de sucesso
     *
     * @param waste Objeto Waste com os dados do formulário
     * @param bindingResult Resultado da validação do formulário
     * @param model Modelo Spring para passar dados para a view
     * @param redirectAttributes Atributos para redirecionamento
     * @return String Nome da view a ser renderizada ou redirecionamento
     */
    @PostMapping("/register")
    public String registerWaste(@Valid @ModelAttribute Waste waste, 
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        logger.info("Tentando registrar resíduo: {}", waste);
        
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult, model);
        }

        try {
            String username = getAuthenticatedUsername();
            if (username == null) {
                return handleUnauthorized(model);
            }
            
            wasteService.save(waste, username);
            logger.info("Resíduo salvo com sucesso");
            
            addSuccessMessage(redirectAttributes);
            return DASHBOARD_REDIRECT;
        } catch (Exception e) {
            return handleException(e, model);
        }
    }
    
    /**
     * Trata erros de validação do formulário.
     * Este método é chamado quando há erros de validação nos dados do formulário.
     * 
     * Fluxo:
     * 1. Registra os erros no log
     * 2. Adiciona o título ao modelo
     * 3. Retorna para o formulário com os erros
     *
     * @param bindingResult Resultado da validação do formulário
     * @param model Modelo Spring para passar dados para a view
     * @return String Nome da view a ser renderizada
     */
    private String handleValidationErrors(BindingResult bindingResult, Model model) {
        logger.warn("Erros de validação encontrados: {}", bindingResult.getAllErrors());
        model.addAttribute(TITULO_ATTR, TITULO_VALUE);
        return REGISTER_VIEW;
    }
    
    /**
     * Trata casos de usuário não autenticado.
     * Este método é chamado quando o usuário não está autenticado.
     * 
     * Fluxo:
     * 1. Registra o erro no log
     * 2. Adiciona mensagem de erro ao modelo
     * 3. Retorna para o formulário com a mensagem
     *
     * @param model Modelo Spring para passar dados para a view
     * @return String Nome da view a ser renderizada
     */
    private String handleUnauthorized(Model model) {
        logger.error("Usuário não autenticado");
        model.addAttribute(TITULO_ATTR, TITULO_VALUE);
        model.addAttribute("errorMessage", "Você precisa estar logado para registrar resíduos.");
        return REGISTER_VIEW;
    }
    
    /**
     * Trata exceções gerais durante o registro.
     * Este método é chamado quando ocorre uma exceção durante o processo de registro.
     * 
     * Fluxo:
     * 1. Registra o erro no log
     * 2. Adiciona mensagem de erro ao modelo
     * 3. Retorna para o formulário com a mensagem
     *
     * @param e Exceção ocorrida
     * @param model Modelo Spring para passar dados para a view
     * @return String Nome da view a ser renderizada
     */
    private String handleException(Exception e, Model model) {
        logger.error("Erro ao registrar resíduo: {}", e.getMessage(), e);
        model.addAttribute(TITULO_ATTR, TITULO_VALUE);
        model.addAttribute("errorMessage", "Erro ao registrar resíduo: " + e.getMessage());
        return REGISTER_VIEW;
    }
    
    /**
     * Obtém o nome do usuário autenticado.
     * Este método verifica se o usuário está autenticado e retorna seu nome.
     * 
     * Fluxo:
     * 1. Obtém o objeto de autenticação
     * 2. Registra informações de autenticação no log
     * 3. Verifica se o usuário está autenticado
     * 4. Retorna o nome do usuário ou null se não autenticado
     *
     * @return String Nome do usuário autenticado ou null
     */
    private String getAuthenticatedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Usuário autenticado: {}, Principal: {}", 
                   auth != null ? auth.getName() : "null", 
                   auth != null ? auth.getPrincipal() : "null");
                   
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        
        return auth.getName();
    }
    
    /**
     * Adiciona mensagem de sucesso após o registro.
     * Este método é chamado quando o registro é bem-sucedido.
     * 
     * Fluxo:
     * 1. Adiciona mensagem de sucesso aos atributos de redirecionamento
     * 2. Configura o tipo da mensagem (alert-success)
     *
     * @param redirectAttributes Atributos para redirecionamento
     */
    private void addSuccessMessage(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Resíduo registrado com sucesso!");
        redirectAttributes.addFlashAttribute("messageType", "alert-success");
    }
}
