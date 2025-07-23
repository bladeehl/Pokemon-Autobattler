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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class UpdatePokemonHandler {
    @NonNull PokemonService pokemonService;
    @NonNull ConsoleSessionState sessionState;

    public void handle(
        @NonNull final String input,
        @NonNull final StringBuilder output) {

        val trainer = sessionState.getTrainer();
        val pokemons = pokemonService.getByTrainer(trainer);

        try {
            val data = input.split(" ");
            val index = UtilWebIO.parseInt(data[0].trim(), output) - 1;

            if (index < 0 || index >= pokemons.size()) {
                output.append(UtilWebIO.getInvalidChoiceMessage());
                output.append(PokemonWebIO.getSelectPokemonToUpdatePrompt(pokemons));
                return;
            }

            val pokemon = pokemons.get(index);
            val fieldData = data[1].trim().split(":");
            val field = fieldData[0].trim();
            val value = fieldData[1].trim();

            switch (field) {
                case "name" -> pokemon.setName(value);
                case "hp" -> pokemon.setHealth(UtilWebIO.parseInt(value, output));
                case "damage" -> pokemon.setDamage(UtilWebIO.parseInt(value, output));
                default -> {
                    output.append("Неверное поле.");
                    output.append(PokemonWebIO.getSelectPokemonToUpdatePrompt(pokemons));
                    return;
                }
            }

            pokemonService.updatePokemon(pokemon);
            output.append(PokemonWebIO.getPokemonUpdatedMessage());
            sessionState.setState("trainerActions");
            output.append(TrainerWebIO.getTrainerActions(trainer));
            sessionState.setInputType("trainerActionChoice");
        } catch (Exception thrown) {
            log.error("Ошибка во время обновления: {}", thrown.getMessage(), thrown);
        }
    }
}
