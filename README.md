# Sistema de Gestão de Resíduos

Sistema web para gerenciamento de resíduos, desenvolvido com Spring Boot e Thymeleaf.

## Funcionalidades

- Cadastro e autenticação de usuários
- Registro de resíduos
- Dashboard com estatísticas
- Interface responsiva

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- Thymeleaf
- Bootstrap 5
- MySQL
- Lombok

## Estrutura do Projeto

### Controllers

#### WasteController
```java
@Controller
@RequestMapping("/waste")
public class WasteController {
    @GetMapping("/register")
    public String cadastrar(Model model) {
        model.addAttribute("waste", new Waste());
        model.addAttribute("titulo", "Registro de Resíduo");
        return "waste/register";
    }

    @PostMapping("/register")
    public String registerWaste(@ModelAttribute Waste waste, 
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        try {
            waste.setDate(LocalDate.now());
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
}
```

#### Fluxo de Registro de Resíduos

1. **Acesso à Página**:
   - Usuário acessa `/waste/register` (GET)
   - Controller retorna o template `waste/register.html`
   - Template exibe formulário com campos:
     - Tipo de resíduo (select)
     - Peso (number)
     - Descrição (textarea)

2. **Submissão do Formulário**:
   - Formulário envia POST para `/waste/register`
   - Spring converte dados para objeto `Waste`
   - Controller:
     - Define data atual
     - Chama `WasteService.save()`
   - Service:
     - Busca usuário pelo email
     - Associa usuário ao resíduo
     - Salva no banco
   - Controller:
     - Sucesso: redireciona para `/dashboard`
     - Erro: redireciona para `/waste/register`

3. **Tratamento de Erros**:
   - Usuário não encontrado: "Usuário não encontrado"
   - Outros erros: mensagem específica do erro
   - Mensagens usando Bootstrap:
     - Sucesso: `alert-success` (verde)
     - Erro: `alert-danger` (vermelho)

4. **Segurança**:
   - Autenticação necessária
   - Email obtido do contexto de segurança
   - Resíduo associado ao usuário criador

### Models

#### Waste
```java
@Entity
@Table(name = "tb_waste")
public class Waste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Type type;

    private Double weight;

    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
```

#### Type (Enum)
```java
public enum Type {
    PLASTIC,
    ORGANIC,
    PAPER,
    GLASS,
    METAL,
    ELECTRONIC,
    CONSTRUCTION,
    HOSPITAL
}
```

### Services

#### WasteService
```java
@Service
public class WasteService {
    private final WasteRepository wasteRepository;
    private final UserRepository userRepository;

    public void save(Waste waste, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        waste.setUser(user);
        wasteRepository.save(waste);
    }
}
```

## Padrões de Projeto

O sistema segue os seguintes padrões:

1. **MVC (Model-View-Controller)**:
   - Models: representam os dados
   - Views: templates Thymeleaf
   - Controllers: processam requisições

2. **Camadas de Serviço**:
   - Controllers: lidam com HTTP
   - Services: lógica de negócio
   - Repositories: acesso a dados

3. **Segurança**:
   - Spring Security
   - Autenticação por formulário
   - Proteção de rotas

## Como Executar

1. Clone o repositório
2. Configure o banco de dados em `application.properties`
3. Execute `./mvnw spring-boot:run`
4. Acesse `http://localhost:8080`

## Contribuição

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

