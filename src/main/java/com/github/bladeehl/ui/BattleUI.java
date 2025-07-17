package com.github.bladeehl.ui;

import com.github.bladeehl.model.Trainer;
import com.github.bladeehl.services.PokemonService;
import com.github.bladeehl.services.BattleService;
import com.github.bladeehl.utils.InputUtils;
import com.github.bladeehl.utils.OutputUtils;
import com.github.bladeehl.exceptions.UnsupportedPokemonTypeException;
import lombok.extern.slf4j.Slf4j;
import lombok.NonNull;
import lombok.val;

@Slf4j
public class BattleUI {
    final BattleService battleService = new BattleService();
    final PokemonService pokemonService = new PokemonService();

    public void startBattle(final @NonNull Trainer trainer) {
        val pokemons = pokemonService.getPokemonsByTrainer(trainer);

        if (!trainer.canBattle()) {
            log.warn("Меньше двух покемонов");
            System.out.println("Нужно минимум 2 покемона для боя.");
            return;
        }

        System.out.println("Выберите двух покемонов для битвы:");
        OutputUtils.printPokemons(pokemons);

        val firstIndex = InputUtils.promptForInt("Первый покемон: ") - 1;
        val secondIndex = InputUtils.promptForInt("Второй покемон: ") - 1;

        if (firstIndex == secondIndex
            || firstIndex < 0
            || secondIndex < 0
            || firstIndex >= pokemons.size()
            || secondIndex >= pokemons.size()) {

            log.warn(
                "Некорректный выбор покемонов для битвы: first={}," + " second={}",
                firstIndex + 1,
                secondIndex + 1);

            System.out.println("Некорректный выбор покемонов. Бой отменён.");
            return;
        }

        val firstPokemon = pokemons.get(firstIndex);
        val secondPokemon = pokemons.get(secondIndex);

        System.out.printf("""
            
            ⚔️ Битва начинается!
            %s VS %s
            ⚔️⚔️⚔️⚔️⚔️⚔️⚔️⚔️⚔️⚔️⚔️
            %n%n""",
            firstPokemon.getName(),
            secondPokemon.getName());

        battleService.startBattle(firstPokemon, secondPokemon);

        while (!battleService.isBattleOver()) {
            val playablePokemon = battleService.getCurrentPlayablePokemon();
            val opponentPokemon = battleService.getCurrentOpponentPokemon();

            System.out.printf("""
                🎮 Ход: %s
                1. Атаковать
                2. Защититься
                3. Способность
                4. Спец. атака
                5. Защитная способность
                6. Эволюция
                """, playablePokemon.getName());

            val choice = InputUtils.promptForInt("Выбор: ");

            switch (choice) {
                case 1 -> {
                    val dmg = battleService.attack(playablePokemon, opponentPokemon);
                    System.out.printf(
                        "💥 %s атаковал %s на %d урона%n",
                        playablePokemon.getName(),
                        opponentPokemon.getName(),
                        dmg);
                }
                case 2 -> {
                    battleService.defend(playablePokemon);
                    System.out.printf("🛡️ %s активировал защиту%n",
                        playablePokemon.getName());
                }
                case 3 -> {
                    val gain = battleService.useAbility(playablePokemon);
                    System.out.printf("✨ %s использовал способность (+%d HP)%n",
                        playablePokemon.getName(),
                        gain);
                }
                case 4 -> {
                    try {
                        val dmg = battleService.specialAttack(playablePokemon, opponentPokemon);
                        System.out.printf("🔥 Спец. атака нанесла %d урона%n", dmg);
                    } catch (UnsupportedPokemonTypeException thrown) {
                        log.error("Ошибка спец. атаки: {}", thrown.getMessage());
                        System.out.println("Ошибка: " + thrown.getMessage());
                    }
                }
                case 5 -> {
                    try {
                        battleService.defensiveAbility(playablePokemon);
                        System.out.println("🛡️ Защитная способность активирована.");
                    } catch (UnsupportedPokemonTypeException thrown) {
                        log.error("Ошибка защитной способности: {}", thrown.getMessage());
                        System.out.println("Ошибка: " + thrown.getMessage());
                    }
                }
                case 6 -> {
                    battleService.evolve(playablePokemon);
                    System.out.println("🆙 Эволюция завершена!");
                }
                default -> System.out.println("⛔ Пропуск хода из-за неверного ввода.");
            }

            System.out.printf(
                "📊 %s (HP: %d) vs %s (HP: %d)%n%n",
                firstPokemon.getName(),
                firstPokemon.getHealth(),
                secondPokemon.getName(),
                secondPokemon.getHealth());

            battleService.nextTurn();
        }

        val winner = battleService.getWinner();

        System.out.printf("🏆 Победитель: %s!%n", winner.getName());
    }
}
