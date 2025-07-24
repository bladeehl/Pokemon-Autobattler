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

import static com.github.bladeehl.io.PokemonWebIO.*;
import static com.github.bladeehl.io.TrainerWebIO.*;
import static com.github.bladeehl.model.InputType.*;
import static com.github.bladeehl.model.State.*;

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
            switch (UtilWebIO.parseState(sessionState.getState()) != null
                ? UtilWebIO.parseState(sessionState.getState())
                : TRAINER_MENU) {

                case TRAINER_MENU -> output.append(TRAINER_MENU_PROMT);
                case CREATE_TRAINER -> output.append(CREATE_TRAINER_PROMPT);
                case SELECT_TRAINER -> selectTrainerHandler.handle(output);
                case TRAINER_ACTIONS -> output.append(TrainerWebIO.getTrainerActions(sessionState.getTrainer()));
                case CREATE_POKEMON -> output.append(CREATE_POKEMON_PROMPT);
                case SELECT_POKEMON_FOR_BATTLE -> selectPokemonForBattleHandler.handle(output);
                case SELECT_SECOND_POKEMON -> output.append(PokemonWebIO.getSelectSecondPokemonPrompt(pokemonService.getByTrainer(sessionState.getTrainer())));
                case BATTLE -> output.append(battleWebService.getCurrentOutput());
                case UPDATE_POKEMON -> updatePokemonOutputHandler.handle(output);
                case DELETE_POKEMON  -> output.append(PokemonWebIO.getDeletePokemonPrompt(pokemonService.getByTrainer(sessionState.getTrainer())));
                case SHOW_POKEMONS-> showPokemonsHandler.handle(output);
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
            switch (UtilWebIO.parseInputType(sessionState.getInputType()) != null
                ? UtilWebIO.parseInputType(sessionState.getInputType())
                : TRAINER_MENU) {

                case TRAINER_MENU_CHOICE -> trainerMenuChoiceHandler.handle(input, output);
                case TRAINER_NAME -> trainerNameHandler.handle(input, output);
                case TRAINER_INDEX -> trainerIndexHandler.handle(input, output);
                case TRAINER_ACTION_CHOICE -> trainerActionChoiceHandler.handle(input, output);
                case CREATE_POKEMON_MENU -> createPokemonHandler.handle(input, output);
                case FIRST_POKEMON_INDEX -> firstPokemonIndexHandler.handle(input, output);
                case SECOND_POKEMON_INDEX -> secondPokemonIndexHandler.handle(input, output);
                case BATTLE_CHOICE -> output.append(battleWebService.processInput(input));
                case UPDATE_POKEMON_MENU -> updatePokemonHandler.handle(input, output);
                case DELETE_POKEMON_INDEX -> deletePokemonIndexHandler.handle(input, output);
                default -> {
                    log.error("Неизвестный ввод: {}", sessionState.getInputType());
                    commonHandlers.returnToTrainerMenu(output, sessionState);
                }
            }
        } catch (Exception thrown) {
            log.error("Ошибка: {}", thrown.getMessage(), thrown);
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
