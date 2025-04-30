package com.reciclamais.waste_management.service;

import com.reciclamais.waste_management.dto.TypeWasteDTO;
import com.reciclamais.waste_management.dto.UserRankingDTO;
import com.reciclamais.waste_management.model.Waste;
import com.reciclamais.waste_management.model.User;
import com.reciclamais.waste_management.model.Type;
import com.reciclamais.waste_management.repository.WasteRepository;
import com.reciclamais.waste_management.repository.UserRepository;
import com.reciclamais.waste_management.exceptions.UserNotFoundException;
import com.reciclamais.waste_management.exceptions.WasteValidationException;
import com.reciclamais.waste_management.exceptions.WastePersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Comparator;

/**
 * Serviço responsável pela lógica de negócio relacionada a resíduos.
 * 
 * Esta classe encapsula todas as operações e regras de negócio relacionadas a resíduos,
 * incluindo:
 * - Cadastro e validação de resíduos
 * - Consultas e relatórios
 * - Cálculos de métricas de reciclagem
 * - Rankings de usuários
 * 
 * Principais funcionalidades:
 * 1. Gestão de Resíduos
 *    - Cadastro de novos resíduos
 *    - Validação de dados
 *    - Busca por usuário e período
 * 
 * 2. Métricas e Relatórios
 *    - Total de resíduos
 *    - Taxa de reciclagem
 *    - Distribuição por tipo
 *    - Ranking de usuários
 * 
 * 3. Validações
 *    - Verificação de dados obrigatórios
 *    - Validação de regras de negócio
 *    - Tratamento de exceções específicas
 * 
 * @author Sistema de Gestão de Resíduos
 * @version 1.0
 */
@Service
public class WasteService {

    private static final Logger logger = LoggerFactory.getLogger(WasteService.class);
    private static final int MIN_DESCRIPTION_LENGTH = 10;
    
    private final WasteRepository wasteRepository;
    private final UserRepository userRepository;

    public WasteService(WasteRepository wasteRepository, UserRepository userRepository) {
        this.wasteRepository = wasteRepository;
        this.userRepository = userRepository;
        logger.info("WasteService construído com sucesso");
    }

    /**
     * Salva um novo resíduo associado a um usuário.
     * 
     * Este método implementa as seguintes regras de negócio:
     * 1. Valida se o usuário existe
     * 2. Associa o resíduo ao usuário
     * 3. Valida os dados do resíduo
     * 4. Persiste o resíduo no banco de dados
     *
     * @param waste Resíduo a ser salvo
     * @param userEmail Email do usuário que está registrando o resíduo
     * @throws IllegalArgumentException Se o email do usuário for inválido
     * @throws UserNotFoundException Se o usuário não for encontrado
     * @throws WasteValidationException Se os dados do resíduo forem inválidos
     * @throws WastePersistenceException Se ocorrer erro ao persistir o resíduo
     */
    @Transactional
    public void save(Waste waste, String userEmail) {
        if (!StringUtils.hasText(userEmail)) {
            throw new IllegalArgumentException("Email do usuário não pode ser vazio");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o email: " + userEmail));

        waste.setUser(user);
        validateWaste(waste);

        try {
            wasteRepository.save(waste);
            logger.info("Resíduo salvo com sucesso: {}", waste);
        } catch (Exception e) {
            logger.error("Erro ao salvar resíduo: {}", e.getMessage());
            throw new WastePersistenceException("Erro ao salvar resíduo", e);
        }
    }

    /**
     * Busca todos os resíduos cadastrados no sistema.
     * 
     * @return Lista de todos os resíduos
     */
    public List<Waste> findAll() {
        logger.info("Buscando todos os resíduos");
        return wasteRepository.findAll();
    }

    /**
     * Busca resíduos por período específico.
     * 
     * @param startDate Data inicial do período
     * @param endDate Data final do período
     * @return Lista de resíduos no período especificado
     */
    public List<Waste> findByPeriod(LocalDate startDate, LocalDate endDate) {
        logger.info("Buscando resíduos no período de {} a {}", startDate, endDate);
        // TODO: Implementar busca por período
        return wasteRepository.findAll();
    }

    /**
     * Busca resíduos associados a um usuário específico.
     * 
     * @param userEmail Email do usuário
     * @return Lista de resíduos do usuário
     * @throws IllegalArgumentException Se o email do usuário for inválido
     */
    public List<Waste> findByUser(String userEmail) {
        if (!StringUtils.hasText(userEmail)) {
            throw new IllegalArgumentException("Email do usuário não pode ser vazio");
        }
        logger.info("Buscando resíduos do usuário: {}", userEmail);
        // TODO: Implementar busca por usuário
        return wasteRepository.findAll();
    }

    /**
     * Valida os dados de um resíduo antes de salvá-lo.
     * 
     * Regras de validação:
     * - Resíduo não pode ser nulo
     * - Data é obrigatória
     * - Peso deve ser maior que zero
     * - Tipo é obrigatório
     * - Descrição deve ter pelo menos 10 caracteres
     * 
     * @param waste Resíduo a ser validado
     * @throws WasteValidationException Se alguma regra de validação não for atendida
     */
    private void validateWaste(Waste waste) {
        if (waste == null) {
            throw new WasteValidationException("Resíduo não pode ser nulo");
        }
        if (waste.getDate() == null) {
            throw new WasteValidationException("Data do resíduo é obrigatória");
        }
        if (waste.getWeight() == null || waste.getWeight() <= 0) {
            throw new WasteValidationException("Peso do resíduo deve ser maior que zero");
        }
        if (waste.getType() == null) {
            throw new WasteValidationException("Tipo do resíduo é obrigatório");
        }
        if (!StringUtils.hasText(waste.getDescription()) || waste.getDescription().length() < MIN_DESCRIPTION_LENGTH) {
            throw new WasteValidationException("Descrição deve ter pelo menos " + MIN_DESCRIPTION_LENGTH + " caracteres");
        }
    }

    /**
     * Calcula a taxa de reciclagem do sistema.
     * 
     * A taxa é calculada como a razão entre o total de resíduos reciclados
     * e o total de resíduos, multiplicada por 100 para obter a porcentagem.
     * 
     * @return Taxa de reciclagem em porcentagem
     */
    public double getRecyclingRate() {
        double totalWaste = getTotalWaste();
        if (totalWaste == 0) {
            return 0.0;
        }
        double recycledWaste = getWasteRecycled();
        return (recycledWaste / totalWaste) * 100;
    }

    /**
     * Calcula o total de resíduos cadastrados no sistema.
     * 
     * @return Total de resíduos em kg
     */
    public double getTotalWaste() {
        List<Waste> allWastes = wasteRepository.findAll();
        if (allWastes.isEmpty()) {
            return 0.0;
        }
        return allWastes.stream()
                .mapToDouble(Waste::getWeight)
                .sum();
    }

    /**
     * Calcula o total de resíduos reciclados no sistema.
     * 
     * @return Total de resíduos reciclados em kg
     */
    public double getWasteRecycled() {
        List<Waste> allWastes = wasteRepository.findAll();
        if (allWastes.isEmpty()) {
            return 0.0;
        }
        return allWastes.stream()
                .filter(Waste::getRecycled)
                .mapToDouble(Waste::getWeight)
                .sum();
    }

    /**
     * Obtém a distribuição de resíduos por tipo.
     * 
     * Este método:
     * 1. Agrupa os resíduos por tipo
     * 2. Calcula o peso total para cada tipo
     * 3. Calcula a porcentagem de cada tipo em relação ao total
     * 4. Retorna os dados formatados em DTOs
     * 
     * @return Lista de DTOs contendo tipo, quantidade e porcentagem
     */
    public List<TypeWasteDTO> getWasteByType() {
        List<Waste> allWastes = wasteRepository.findAll();
        logger.info("Total wastes found: {}", allWastes.size());
        
        if (allWastes.isEmpty()) {
            return List.of();
        }

        // Group wastes by type and calculate total weight for each type
        Map<Type, Double> typeWeights = allWastes.stream()
                .collect(Collectors.groupingBy(
                        Waste::getType,
                        Collectors.summingDouble(Waste::getWeight)
                ));

        logger.info("Type weights: {}", typeWeights);

        // Calculate total weight for percentage calculation
        double totalWeight = typeWeights.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        logger.info("Total weight: {}", totalWeight);

        // Convert to DTOs with proper quantities and percentages
        List<TypeWasteDTO> result = typeWeights.entrySet().stream()
                .map(entry -> {
                    double weight = entry.getValue();
                    double percentage = (weight / totalWeight) * 100;
                    return new TypeWasteDTO(
                            entry.getKey().name(),
                            (int) Math.round(weight),
                            percentage
                    );
                })
                .sorted(Comparator.comparing(TypeWasteDTO::getQuantity).reversed())
                .collect(Collectors.toList());

        logger.info("Result DTOs: {}", result);
        return result;
    }

    /**
     * Obtém o ranking de usuários por quantidade de resíduos reciclados.
     * 
     * Este método:
     * 1. Agrupa os resíduos reciclados por usuário
     * 2. Calcula o peso total reciclado para cada usuário
     * 3. Ordena os usuários por quantidade reciclada (decrescente)
     * 4. Retorna os dados formatados em DTOs
     * 
     * @return Lista de DTOs contendo nome do usuário e total reciclado
     */
    public List<UserRankingDTO> getUserRankings() {
        List<Waste> allWastes = wasteRepository.findAll();
        logger.info("Calculating user rankings from {} wastes", allWastes.size());

        if (allWastes.isEmpty()) {
            return List.of();
        }

        // Group wastes by user and calculate total recycled weight for each user
        Map<User, Double> userRecycledWeights = allWastes.stream()
                .filter(Waste::getRecycled)
                .collect(Collectors.groupingBy(
                        Waste::getUser,
                        Collectors.summingDouble(Waste::getWeight)
                ));

        logger.info("User recycled weights: {}", userRecycledWeights);

        // Convert to DTOs and sort by total recycled weight
        List<UserRankingDTO> rankings = userRecycledWeights.entrySet().stream()
                .map(entry -> new UserRankingDTO(
                        entry.getKey().getName(),
                        entry.getValue()
                ))
                .sorted(Comparator.comparing(UserRankingDTO::getTotalRecycled).reversed())
                .collect(Collectors.toList());

        logger.info("User rankings: {}", rankings);
        return rankings;
    }
}