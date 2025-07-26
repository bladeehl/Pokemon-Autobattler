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

import static com.github.bladeehl.io.PokemonWebIO.*;
import static com.github.bladeehl.io.UtilWebIO.*;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class DeletePokemonIndexHandler {
    @NonNull PokemonService pokemonService;
    @NonNull ConsoleSessionState sessionState;

    public void handle(
        @NonNull final String input,
        @NonNull final StringBuilder output) {

        val trainer = sessionState.getTrainer();
        val pokemons = pokemonService.getByTrainer(trainer);
        val index = UtilWebIO.parseInt(input, output).orElse(-1);

        if (index < 0 || index >= pokemons.size()) {
            output.append(INVALID_CHOICE);
            output.append(PokemonWebIO.getDeletePokemonPrompt(pokemons));
            return;
        }

        pokemonService.deletePokemon(pokemons.get(index));
        output.append(POKEMON_DELETED_MESSAGE);
        sessionState.setState("trainerActions");
        output.append(TrainerWebIO.getTrainerActions(trainer));
        sessionState.setInputType("trainerActionChoice");
    }
}
