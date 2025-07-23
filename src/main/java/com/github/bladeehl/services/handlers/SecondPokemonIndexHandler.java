package com.github.bladeehl.services.handlers;

import com.github.bladeehl.io.TrainerWebIO;
import com.github.bladeehl.io.UtilWebIO;
import com.github.bladeehl.services.PokemonService;
import com.github.bladeehl.services.ConsoleBattleWebService;
import com.github.bladeehl.services.ConsoleSessionState;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class SecondPokemonIndexHandler {
    @NonNull PokemonService pokemonService;
    @NonNull ConsoleBattleWebService battleWebService;
    @NonNull ConsoleSessionState sessionState;

    public void handle(
        @NonNull final String input,
        @NonNull final StringBuilder output) {

        val trainer = sessionState.getTrainer();
        val pokemons = pokemonService.getByTrainer(trainer);
        val index = UtilWebIO.parseInt(input, output) - 1;
        val firstPokemon = sessionState.getFirstPokemon();

        if (index < 0
            || index >= pokemons.size()
            || pokemons
            .get(index)
            .equals(firstPokemon)) {

            output.append(UtilWebIO.getInvalidPokemonSelectionMessage());
            sessionState.setState("trainerActions");
            output.append(TrainerWebIO.getTrainerActions(trainer));
            sessionState.setInputType("trainerActionChoice");
            return;
        }

        val secondPokemon = pokemons.get(index);

        sessionState.setSecondPokemon(secondPokemon);
        battleWebService.startBattle(firstPokemon, secondPokemon);
        sessionState.setState("battle");
        output.append(battleWebService.getCurrentOutput());
        sessionState.setInputType("battleChoice");
    }
}
