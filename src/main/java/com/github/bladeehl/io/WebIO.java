package com.github.bladeehl.io;

import com.github.bladeehl.model.Pokemon;
import com.github.bladeehl.model.Trainer;
import lombok.NonNull;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebIO {
    public String getTrainerMenu() {
        return """
            --- Тренерское меню ---
            1. Создать тренера
            2. Выбрать тренера
            0. Выход

            Ваш выбор: """;
    }

    public String getCreateTrainerPrompt() {
        return "Введите имя: ";
    }

    public String getSelectTrainerPrompt(@NonNull final List<Trainer> trainers) {
        val output = new StringBuilder();

        appendTrainers(output, trainers);
        output.append("Выберите номер: ");
        return output.toString();
    }

    public String getNoTrainersMessage() {
        return "Тренеров нет.";
    }

    public String getTrainerActions(@NonNull final Trainer trainer) {
        return """
            --- Меню %s ---
            1. Начать бой
            2. Создать покемона
            3. Изменить покемона
            4. Удалить покемона
            5. Показать покемонов
            0. Назад

            Выбор: """
            .formatted(trainer.getName());
    }


    public String getCreatePokemonTypePrompt() {
        return "Выберите тип покемона (1 - Огненный, 2 - Водяной): ";
    }

    public String getCreatePokemonNamePrompt() {
        return "Имя: ";
    }

    public String getCreatePokemonHPPrompt() {
        return "Здоровье: ";
    }

    public String getCreatePokemonDamagePrompt() {
        return "Урон: ";
    }

    public String getCreateFirePokemonResPrompt() {
        return "Огненная защита: ";
    }

    public String getCreateFirePokemonPwrPrompt() {
        return "Огненная сила: ";
    }

    public String getCreateWaterPokemonResPrompt() {
        return "Водная защита: ";
    }

    public String getCreateWaterPokemonPwrPrompt() {
        return "Водная сила: ";
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

    public String getBattleStart(
        @NonNull final Pokemon firstPokemon,
        @NonNull final Pokemon secondPokemon) {

        return """
            ⚔️ Битва начинается!
            %s VS %s
            ⚔️⚔️⚔️⚔️⚔️⚔️⚔️⚔️⚔️⚔️⚔️
            
            """
            .formatted(firstPokemon.getName(),
                secondPokemon.getName());
    }

    public String getBattleTurn(@NonNull final Pokemon currentPokemon) {
        return """
            🎮 Ход: %s
            1. Атаковать
            2. Защититься
            3. Способность
            4. Спец. атака
            5. Защитная способность
            6. Эволюция
            Выбор: """
            .formatted(currentPokemon.getName());
    }

    public String getBattleAttack(
        @NonNull final Pokemon attacker,
        @NonNull final Pokemon target,
        int damage) {

        return "💥 %s атаковал %s на %d урона%n"
            .formatted(attacker.getName(),
                target.getName(),
                damage);
    }


    public String getBattleDefend(@NonNull final Pokemon pokemon) {
        return "🛡️ %s активировал защиту%n".formatted(pokemon.getName());
    }

    public String getBattleAbility(
        @NonNull final Pokemon pokemon,
        int gain) {

        return "✨ %s использовал способность (+%d HP)%n".formatted(pokemon.getName(),
                gain);
    }


    public String getBattleSpecialAttack(int damage) {
        return "🔥 Спец. атака нанесла %d урона%n".formatted(damage);
    }

    public String getBattleDefensiveAbility() {
        return "🛡️ Защитная способность активирована.";
    }

    public String getBattleEvolve() {
        return "🆙 Эволюция завершена!";
    }

    public String getBattleStatus(
        @NonNull final Pokemon firstPokemon,
        @NonNull final Pokemon secondPokemon) {

        return "📊 %s (HP: %d) vs %s (HP: %d)%n%n".formatted(
            firstPokemon.getName(),
            firstPokemon.getHealth(),
            secondPokemon.getName(),
            secondPokemon.getHealth());
    }

    public String getBattleWinner(@NonNull final Pokemon winner) {
        return "Победитель: %s!%n".formatted(winner.getName());
    }

    public String getNoPokemonForBattleMessage() {
        return "Нужно минимум 2 покемона для боя.";
    }

    public String getSelectPokemonToUpdatePrompt(@NonNull final List<Pokemon> pokemons) {
        val output = new StringBuilder();

        appendPokemons(output, pokemons);
        output.append("Выберите покемона: ");
        return output.toString();
    }

    public String getUpdatePokemonFieldPrompt() {
        return """
            1. Имя
            2. HP
            3. Урон

            Что изменить: """;
    }

    public String getUpdatePokemonNamePrompt() {
        return "Новое имя: ";
    }

    public String getUpdatePokemonHPPrompt() {
        return "Новое здоровье: ";
    }

    public String getUpdatePokemonDamagePrompt() {
        return "Новый урон: ";
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

    public String getInvalidChoiceMessage() {
        return "Неверный выбор. Попробуйте снова.";
    }

    public String getInvalidPokemonSelectionMessage() {
        return "Некорректный выбор покемонов. Бой отменён.";
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

    public String getInvalidInputMessage() {
        return "Ошибка: введите корректное целое число.\n";
    }

    public String getErrorMessage(@NonNull final String message) {
        return "Ошибка: " + message + "\n";
    }

    public int parseInt(
        @NonNull final String input,
        @NonNull final StringBuilder output) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException thrown) {
            output.append(getInvalidInputMessage());
            return -1;
        }
    }

    private void appendPokemons(
        @NonNull final StringBuilder output,
        @NonNull final List<Pokemon> pokemons) {

        if (pokemons.isEmpty()) {
            output.append("Нет покемонов.\n");
        } else {
            pokemons.stream()
                .map(pokemon -> "%d - %s%n".formatted(pokemons.indexOf(pokemon) + 1,
                    pokemon.toString()))
                .forEach(output::append);
        }
    }

    private void appendTrainers(
        @NonNull final StringBuilder output,
        @NonNull final List<Trainer> trainers) {

        trainers.stream()
            .map(trainer -> "%d - %s%n".formatted(trainers.indexOf(trainer) + 1,
                trainer.getName()))
            .forEach(output::append);
    }
}
