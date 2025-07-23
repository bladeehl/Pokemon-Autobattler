package com.github.bladeehl.services.handlers;

import com.github.bladeehl.io.PokemonWebIO;
import com.github.bladeehl.io.TrainerWebIO;
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
public class SelectPokemonForBattleHandler {
    @NonNull PokemonService pokemonService;
    @NonNull ConsoleSessionState sessionState;

    public void handle(@NonNull final StringBuilder output) {
        val trainer = sessionState.getTrainer();
        val pokemons = pokemonService.getByTrainer(trainer);

        if (!trainer.canBattle()) {
            output.append(PokemonWebIO.getNoPokemonForBattleMessage());
            sessionState.setState("trainerActions");
            output.append(TrainerWebIO.getTrainerActions(trainer));
            sessionState.setInputType("trainerActionChoice");
            return;
        }

        output.append(PokemonWebIO.getSelectPokemonForBattlePrompt(pokemons));
        sessionState.setInputType("firstPokemonIndex");
    }
}
