package com.github.bladeehl.io;

import com.github.bladeehl.model.Pokemon;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.List;

@UtilityClass
public class PokemonWebIO {
    public String getCreatePokemonPrompt() {
        return "Введите данные покемона (тип,имя,HP,урон,резист,сила):";
    }

    public String getSelectPokemonForBattlePrompt(@NonNull final List<Pokemon> pokemons) {
        val output = new StringBuilder();

        output.append("Выберите двух покемонов для битвы:\n");
        appendPokemons(output, pokemons);
        output.append("Первый покемон: ");
        return output.toString();
    }

    public String getSelectSecondPokemonPrompt(@NonNull final List<Pokemon> pokemons) {
        val output = new StringBuilder();

        appendPokemons(output, pokemons);
        output.append("Второй покемон: ");
        return output.toString();
    }

    public String getSelectPokemonToUpdatePrompt(@NonNull final List<Pokemon> pokemons) {
        val output = new StringBuilder();

        appendPokemons(output, pokemons);
        output.append("Выберите покемона: ");
        return output.toString();
    }

    public String getDeletePokemonPrompt(@NonNull final List<Pokemon> pokemons) {
        val output = new StringBuilder();

        appendPokemons(output, pokemons);
        output.append("Удалить покемона номер: ");
        return output.toString();
    }

    public String getShowPokemons(@NonNull final List<Pokemon> pokemons) {
        val output = new StringBuilder();

        appendPokemons(output, pokemons);
        return output.toString();
    }

    public String getNoPokemonForBattleMessage() {
        return "Нужно минимум 2 покемона для боя.";
    }

    public String getPokemonCreatedMessage() {
        return "Покемон создан.\n";
    }

    public String getPokemonUpdatedMessage() {
        return "Покемон обновлён.\n";
    }

    public String getPokemonDeletedMessage() {
        return "Покемон удалён.\n";
    }

    public void appendPokemons(
        @NonNull final StringBuilder output,
        @NonNull final List<Pokemon> pokemons) {
        if (pokemons.isEmpty()) {
            output.append("Нет покемонов.\n");
            return;
        }
        pokemons.stream()
            .map(pokemon -> "%d - %s%n".formatted(pokemons.indexOf(pokemon) + 1,
                pokemon.toString()))
            .forEach(output::append);
    }
}
