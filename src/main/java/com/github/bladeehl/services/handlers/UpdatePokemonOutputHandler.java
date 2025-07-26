package com.github.bladeehl.services.handlers;

import com.github.bladeehl.io.PokemonWebIO;
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
public class UpdatePokemonOutputHandler {
    @NonNull PokemonService pokemonService;
    @NonNull ConsoleSessionState sessionState;

    public void handle(@NonNull final StringBuilder output) {
        val pokemons = pokemonService.getByTrainer(sessionState.getTrainer());

        output.append(PokemonWebIO.getSelectPokemonToUpdatePrompt(pokemons));
        sessionState.setInputType("updatePokemon");
    }
}
