package com.github.bladeehl.services.handlers;

import com.github.bladeehl.io.PokemonWebIO;
import com.github.bladeehl.io.TrainerWebIO;
import com.github.bladeehl.io.UtilWebIO;
import com.github.bladeehl.services.PokemonService;
import com.github.bladeehl.services.ConsoleSessionState;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class FirstPokemonIndexHandler {
    @NonNull PokemonService pokemonService;
    @NonNull ConsoleSessionState sessionState;

    public void handle(
        @NonNull final String input,
        @NonNull final StringBuilder output) {

        val trainer = sessionState.getTrainer();
        val pokemons = pokemonService.getByTrainer(trainer);
        val index = UtilWebIO.parseInt(input, output) - 1;

        if (index < 0 || index >= pokemons.size()) {
            output.append(UtilWebIO.getInvalidPokemonSelectionMessage());
            sessionState.setState("trainerActions");
            output.append(TrainerWebIO.getTrainerActions(trainer));
            sessionState.setInputType("trainerActionChoice");
            return;
        }

        sessionState.setFirstPokemon(pokemons.get(index));
        sessionState.setState("selectSecondPokemon");
        output.append(PokemonWebIO.getSelectSecondPokemonPrompt(pokemons));
        sessionState.setInputType("secondPokemonIndex");
    }
}
