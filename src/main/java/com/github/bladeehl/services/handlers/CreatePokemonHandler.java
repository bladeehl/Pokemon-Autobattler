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
public class CreatePokemonHandler {
    @NonNull PokemonService pokemonService;
    @NonNull ConsoleSessionState sessionState;

    public void handle(
        @NonNull final String input,
        @NonNull final StringBuilder output) {

        val trainer = sessionState.getTrainer();
        try {
            val data = input.split(" ");

            if (data.length != 5) {
                output.append("Неверный формат.");
                output.append(PokemonWebIO.getCreatePokemonPrompt());
                return;
            }

            val type = UtilWebIO.parseInt(data[0].trim(), output);
            val name = data[1].trim();
            val hp = UtilWebIO.parseInt(data[2].trim(), output);
            val damage = UtilWebIO.parseInt(data[3].trim(), output);
            val res = UtilWebIO.parseInt(data[4].trim(), output);
            val pwr = UtilWebIO.parseInt(data[5].trim(), output);

            switch (type) {
                case 1 -> pokemonService.saveFirePokemon(trainer, name, hp, damage, res, pwr);
                case 2 -> pokemonService.saveWaterPokemon(trainer, name, hp, damage, res, pwr);
                default -> {
                    output.append(UtilWebIO.getInvalidChoiceMessage());
                    output.append(PokemonWebIO.getCreatePokemonPrompt());
                    return;
                }
            }
            output.append(PokemonWebIO.getPokemonCreatedMessage());
            sessionState.setState("trainerActions");
            output.append(TrainerWebIO.getTrainerActions(trainer));
            sessionState.setInputType("trainerActionChoice");
        } catch (Exception thrown) {
            log.error("Ошибка во время создания: {}", thrown.getMessage(), thrown);
        }
    }
}
