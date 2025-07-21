package com.github.bladeehl.ui;

import com.github.bladeehl.io.IOContext;
import com.github.bladeehl.model.Trainer;
import com.github.bladeehl.services.PokemonService;
import com.github.bladeehl.services.BattleService;
import com.github.bladeehl.exceptions.UnsupportedPokemonTypeException;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.NonNull;
import lombok.val;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class BattleUI {
    BattleService battleService;
    PokemonService pokemonService;
    IOContext io;

    public void startBattle(@NonNull final Trainer trainer) {
        val pokemons = pokemonService.getByTrainer(trainer);

        if (!trainer.canBattle()) {
            log.warn("Меньше двух покемонов");
            io.println("Нужно минимум 2 покемона для боя.");
            return;
        }

        io.println("Выберите двух покемонов для битвы:");
        io.printPokemons(pokemons);

        val firstIndex = io.promptForInt("Первый покемон: ") - 1;
        val secondIndex = io.promptForInt("Второй покемон: ") - 1;

        if (firstIndex == secondIndex
            || firstIndex < 0
            || secondIndex < 0
            || firstIndex >= pokemons.size()
            || secondIndex >= pokemons.size()) {

            log.warn(
                "Некорректный выбор покемонов для битвы: first={}, second={}",
                firstIndex + 1,
                secondIndex + 1);

            io.println("Некорректный выбор покемонов. Бой отменён.");
            return;
        }

        val firstPokemon = pokemons.get(firstIndex);
        val secondPokemon = pokemons.get(secondIndex);

        io.printf("""
            
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

            io.printf("""
                🎮 Ход: %s
                1. Атаковать
                2. Защититься
                3. Способность
                4. Спец. атака
                5. Защитная способность
                6. Эволюция
                """, playablePokemon.getName());

            val choice = io.promptForInt("Выбор: ");

            switch (choice) {
                case 1 -> {
                    val dmg = battleService.attack(playablePokemon, opponentPokemon);
                    io.printf(
                        "💥 %s атаковал %s на %d урона%n",
                        playablePokemon.getName(),
                        opponentPokemon.getName(),
                        dmg);
                }
                case 2 -> {
                    battleService.defend(playablePokemon);
                    io.printf("🛡️ %s активировал защиту%n",
                        playablePokemon.getName());
                }
                case 3 -> {
                    val gain = battleService.useAbility(playablePokemon);
                    io.printf("✨ %s использовал способность (+%d HP)%n",
                        playablePokemon.getName(),
                        gain);
                }
                case 4 -> {
                    try {
                        val dmg = battleService.specialAttack(playablePokemon, opponentPokemon);
                        io.printf("🔥 Спец. атака нанесла %d урона%n", dmg);
                    } catch (UnsupportedPokemonTypeException thrown) {
                        log.error("Ошибка спец. атаки", thrown);
                        io.println("Ошибка: " + thrown.getMessage());
                    }
                }
                case 5 -> {
                    try {
                        battleService.defensiveAbility(playablePokemon);
                        io.println("🛡️ Защитная способность активирована.");
                    } catch (UnsupportedPokemonTypeException thrown) {
                        log.error("Ошибка защитной способности", thrown);
                        io.println("Ошибка: " + thrown.getMessage());
                    }
                }
                case 6 -> {
                    battleService.evolve(playablePokemon);
                    io.println("🆙 Эволюция завершена!");
                }
                default -> io.println("⛔ Пропуск хода из-за неверного ввода.");
            }

            io.printf(
                "📊 %s (HP: %d) vs %s (HP: %d)%n%n",
                firstPokemon.getName(),
                firstPokemon.getHealth(),
                secondPokemon.getName(),
                secondPokemon.getHealth());

            battleService.nextTurn();
        }

        val winner = battleService.getWinner();

        io.printf("🏆 Победитель: %s!%n", winner.getName());
    }
}
