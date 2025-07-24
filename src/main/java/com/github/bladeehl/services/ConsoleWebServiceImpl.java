package com.github.bladeehl.services;

import com.github.bladeehl.io.PokemonWebIO;
import com.github.bladeehl.io.UtilWebIO;
import com.github.bladeehl.model.ConsoleHistory;
import com.github.bladeehl.repositories.ConsoleHistoryRepository;
import com.github.bladeehl.services.handlers.*;
import com.github.bladeehl.io.TrainerWebIO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
class ConsoleWebServiceImpl implements ConsoleWebService {
    @NonNull PokemonServiceImpl pokemonService;
    @NonNull ConsoleBattleWebService battleWebService;
    @NonNull ConsoleSessionState sessionState;
    @NonNull ConsoleHistoryRepository historyRepository;
    @NonNull SelectTrainerHandler selectTrainerHandler;
    @NonNull SelectPokemonForBattleHandler selectPokemonForBattleHandler;
    @NonNull ShowPokemonsHandler showPokemonsHandler;
    @NonNull TrainerMenuChoiceHandler trainerMenuChoiceHandler;
    @NonNull TrainerNameHandler trainerNameHandler;
    @NonNull TrainerIndexHandler trainerIndexHandler;
    @NonNull TrainerActionChoiceHandler trainerActionChoiceHandler;
    @NonNull CreatePokemonHandler createPokemonHandler;
    @NonNull UpdatePokemonOutputHandler updatePokemonOutputHandler;
    @NonNull UpdatePokemonHandler updatePokemonHandler;
    @NonNull FirstPokemonIndexHandler firstPokemonIndexHandler;
    @NonNull SecondPokemonIndexHandler secondPokemonIndexHandler;
    @NonNull DeletePokemonIndexHandler deletePokemonIndexHandler;
    @NonNull CommonHandlers commonHandlers;

    public String getCurrentOutput() {
        val output = new StringBuilder();

        try {
            switch (sessionState.getState() != null
                ? sessionState.getState()
                : "trainerMenu") {

                case "trainerMenu" -> output.append(TrainerWebIO.getTrainerMenu());
                case "createTrainer" -> output.append(TrainerWebIO.getCreateTrainerPrompt());
                case "selectTrainer" -> selectTrainerHandler.handle(output);
                case "trainerActions" -> output.append(TrainerWebIO.getTrainerActions(sessionState.getTrainer()));
                case "createPokemon" -> output.append(PokemonWebIO.getCreatePokemonPrompt());
                case "selectPokemonForBattle" -> selectPokemonForBattleHandler.handle(output);
                case "selectSecondPokemon" -> output.append(PokemonWebIO.getSelectSecondPokemonPrompt(pokemonService.getByTrainer(sessionState.getTrainer())));
                case "battle" -> output.append(battleWebService.getCurrentOutput());
                case "updatePokemon" -> updatePokemonOutputHandler.handle(output);
                case "deletePokemon" -> output.append(PokemonWebIO.getDeletePokemonPrompt(pokemonService.getByTrainer(sessionState.getTrainer())));
                case "showPokemons" -> showPokemonsHandler.handle(output);
                default -> {
                    log.error("Неизвестное состояние: {}", sessionState.getState());
                    commonHandlers.returnToTrainerMenu(output, sessionState);
                }
            }
        } catch (Exception thrown) {
            log.error("Ошибка: {}", thrown.getMessage(), thrown);
            output.append(UtilWebIO.getErrorMessage(thrown.getMessage()));
        }
        return output.toString();
    }

    public String processInput(@NonNull final String input) {
        val output = new StringBuilder();
        val historyEntry = ConsoleHistory.builder()
            .input(input)
            .timestamp(Instant.now())
            .output("")
            .build();

        try {
            switch (sessionState.getInputType() != null
                ? sessionState.getInputType()
                : "trainerMenuChoice") {

                case "trainerMenuChoice" -> trainerMenuChoiceHandler.handle(input, output);
                case "trainerName" -> trainerNameHandler.handle(input, output);
                case "trainerIndex" -> trainerIndexHandler.handle(input, output);
                case "trainerActionChoice" -> trainerActionChoiceHandler.handle(input, output);
                case "createPokemon" -> createPokemonHandler.handle(input, output);
                case "firstPokemonIndex" -> firstPokemonIndexHandler.handle(input, output);
                case "secondPokemonIndex" -> secondPokemonIndexHandler.handle(input, output);
                case "battleChoice" -> output.append(battleWebService.processInput(input));
                case "updatePokemon" -> updatePokemonHandler.handle(input, output);
                case "deletePokemonIndex" -> deletePokemonIndexHandler.handle(input, output);
                default -> {
                    log.error("Неизвестный ввод: {}", sessionState.getInputType());
                    commonHandlers.returnToTrainerMenu(output, sessionState);
                }
            }
        } catch (Exception thrown) {
            log.error("Error in processInput: input={}, state={}, inputType={}",
                input, sessionState.getState(), sessionState.getInputType(), thrown);
            output.append(UtilWebIO.getErrorMessage(thrown.getMessage()));
        }

        historyEntry.setOutput(output.toString());
        historyRepository.save(historyEntry);

        return "%s%n[ID записи: %d]".formatted(output.toString(), historyEntry.getId());
    }

    public List<ConsoleHistory> getHistorySince(long lastEntryId) {
        return historyRepository.findByIdGreaterThanOrderByIdAsc(lastEntryId);
    }
}
