package com.github.bladeehl.io;

import com.github.bladeehl.model.Pokemon;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.List;
import java.util.stream.IntStream;

@UtilityClass
@FieldDefaults(makeFinal = true)
public class PokemonWebIO {
    public String CREATE_POKEMON_PROMPT = "Введите данные покемона (тип,имя,HP,урон,резист,сила):";
    public String NO_POKEMON_FOR_BATTLE_MESSAGE = "Нужно минимум 2 покемона для боя.";
    public String POKEMON_CREATED_MESSAGE = "Покемон создан.\n";
    public String POKEMON_UPDATED_MESSAGE = "Покемон обновлён.\n";
    public String POKEMON_DELETED_MESSAGE = "Покемон удалён.\n";

    private String formatPokemonPrompt(
        @NonNull final List<Pokemon> pokemons,
        @NonNull final String prompt) {

        val output = new StringBuilder();

        appendPokemons(output, pokemons);
        output.append(prompt);
        return output.toString();
    }

    public String getSelectPokemonForBattlePrompt(@NonNull final List<Pokemon> pokemons) {
        return formatPokemonPrompt(pokemons, "Выберите двух покемонов для битвы:\nПервый покемон: ");
    }

    public String getSelectSecondPokemonPrompt(@NonNull final List<Pokemon> pokemons) {
        return formatPokemonPrompt(pokemons, "Второй покемон: ");
    }

    public String getSelectPokemonToUpdatePrompt(@NonNull final List<Pokemon> pokemons) {
        return formatPokemonPrompt(pokemons, "Выберите покемона: ");
    }

    public String getDeletePokemonPrompt(@NonNull final List<Pokemon> pokemons) {
        return formatPokemonPrompt(pokemons, "Удалить покемона номер: ");
    }

    public String getShowPokemons(@NonNull final List<Pokemon> pokemons) {
        val output = new StringBuilder();

        appendPokemons(output, pokemons);
        return output.toString();
    }

    public void appendPokemons(
        @NonNull final StringBuilder output,
        @NonNull final List<Pokemon> pokemons) {

        if (pokemons.isEmpty()) {
            output.append("Нет покемонов.\n");
            return;
        }

        IntStream.range(0, pokemons.size())
            .mapToObj(i -> "%d - %s%n".formatted(
                i + 1,
                pokemons.get(i)))
            .forEach(output::append);
    }
}
