package com.github.bladeehl.services.handlers;

import com.github.bladeehl.io.PokemonWebIO;
import com.github.bladeehl.services.PokemonService;
import com.github.bladeehl.services.ConsoleSessionState;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import com.github.bladeehl.io.UtilWebIO;
import lombok.val;
import org.springframework.stereotype.Component;

import static com.github.bladeehl.io.PokemonWebIO.*;
import static com.github.bladeehl.io.TrainerWebIO.*;
import static com.github.bladeehl.io.UtilWebIO.*;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class TrainerActionChoiceHandler {
    @NonNull PokemonService pokemonService;
    @NonNull ConsoleSessionState sessionState;
    @NonNull SelectPokemonForBattleHandler selectPokemonForBattleHandler;
    @NonNull UpdatePokemonOutputHandler updatePokemonOutputHandler;
    @NonNull ShowPokemonsHandler showPokemonsHandler;

    public void handle(
        @NonNull final String input,
        @NonNull final StringBuilder output) {

        val choice = UtilWebIO.parseInt(input, output).orElse(-1);

        switch (choice) {
            case 1 -> {
                sessionState.setState("selectPokemonForBattle");
                selectPokemonForBattleHandler.handle(output);
            }
            case 2 -> {
                sessionState.setState("createPokemon");
                output.append(POKEMON_CREATED_MESSAGE);
                sessionState.setInputType("createPokemon");
            }
            case 3 -> {
                sessionState.setState("updatePokemon");
                updatePokemonOutputHandler.handle(output);
            }
            case 4 -> {
                sessionState.setState("deletePokemon");
                output.append(PokemonWebIO.getDeletePokemonPrompt(pokemonService.getByTrainer(sessionState.getTrainer())));
                sessionState.setInputType("deletePokemonIndex");
            }
            case 5 -> {
                sessionState.setState("showPokemons");
                showPokemonsHandler.handle(output);
            }
            case 0 -> {
                sessionState.setState("trainerMenu");
                output.append(TRAINER_MENU_PROMT);
                sessionState.setInputType("trainerMenuChoice");
            }
            default -> output.append(INVALID_CHOICE);
        }
    }
}
