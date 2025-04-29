package com.reciclamais.waste_management.service;

import com.reciclamais.waste_management.dto.TypeWasteDTO;
import com.reciclamais.waste_management.dto.UserRankingDTO;
import com.reciclamais.waste_management.model.Waste;
import com.reciclamais.waste_management.model.User;
import com.reciclamais.waste_management.model.Type;
import com.reciclamais.waste_management.repository.WasteRepository;
import com.reciclamais.waste_management.repository.UserRepository;
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
 * Esta classe encapsula todas as operações e regras de negócio relacionadas a resíduos.
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
     */
    @Transactional
    public void save(Waste waste, String userEmail) {
        if (!StringUtils.hasText(userEmail)) {
            throw new IllegalArgumentException("Email do usuário não pode ser vazio");
        }

        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + userEmail));

        validateWaste(waste);
        waste.setUser(user);
        
        try {
            wasteRepository.save(waste);
            logger.info("Resíduo salvo com sucesso para o usuário: {}", userEmail);
        } catch (Exception e) {
            logger.error("Erro ao salvar resíduo: {}", e.getMessage());
            throw new RuntimeException("Erro ao salvar resíduo", e);
        }
    }

    /**
     * Busca todos os resíduos cadastrados.
     *
     * @return Lista de todos os resíduos
     */
    public List<Waste> findAll() {
        logger.info("Buscando todos os resíduos");
        return wasteRepository.findAll();
    }

    /**
     * Busca resíduos por período.
     *
     * @param startDate Data inicial
     * @param endDate Data final
     * @return Lista de resíduos no período
     */
    public List<Waste> findByPeriod(LocalDate startDate, LocalDate endDate) {
        logger.info("Buscando resíduos no período de {} a {}", startDate, endDate);
        // TODO: Implementar busca por período
        return wasteRepository.findAll();
    }

    /**
     * Busca resíduos por usuário.
     *
     * @param userEmail Email do usuário
     * @return Lista de resíduos do usuário
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
     * Valida os dados do resíduo.
     *
     * @param waste Resíduo a ser validado
     * @throws WasteValidationException Se os dados forem inválidos
     */
    private void validateWaste(Waste waste) {
        if (waste == null) {
            throw new IllegalArgumentException("Resíduo não pode ser nulo");
        }
        if (waste.getDate() == null) {
            throw new IllegalArgumentException("Data do resíduo é obrigatória");
        }
        if (waste.getWeight() == null || waste.getWeight() <= 0) {
            throw new IllegalArgumentException("Peso do resíduo deve ser maior que zero");
        }
        if (waste.getType() == null) {
            throw new IllegalArgumentException("Tipo do resíduo é obrigatório");
        }
        if (!StringUtils.hasText(waste.getDescription()) || waste.getDescription().length() < MIN_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("Descrição deve ter pelo menos " + MIN_DESCRIPTION_LENGTH + " caracteres");
        }
    }

    public double getRecyclingRate() {
        double totalWaste = getTotalWaste();
        if (totalWaste == 0) {
            return 0.0;
        }
        double recycledWaste = getWasteRecycled();
        return (recycledWaste / totalWaste) * 100;
    }

    public double getTotalWaste() {
        List<Waste> allWastes = wasteRepository.findAll();
        if (allWastes.isEmpty()) {
            return 0.0;
        }
        return allWastes.stream()
                .mapToDouble(Waste::getWeight)
                .sum();
    }

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

/**
 * Exceção lançada quando um usuário não é encontrado.
 */
class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

/**
 * Exceção lançada quando há erro na validação dos dados do resíduo.
 */
class WasteValidationException extends RuntimeException {
    public WasteValidationException(String message) {
        super(message);
    }
}

/**
 * Exceção lançada quando há erro ao persistir o resíduo.
 */
class WastePersistenceException extends RuntimeException {
    public WastePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}