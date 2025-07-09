package org.example.service;

import lombok.val;
import org.example.dao.PokemonDAO;
import org.example.models.*;

import java.util.Scanner;

public class BattleService {

    private final Scanner scanner = new Scanner(System.in);

    public void startBattle(final Trainer trainer) {
        val pokemons = PokemonDAO.getPokemonsByTrainer(trainer);

        if (pokemons.size() < 2) {
            System.out.println("Нужно минимум 2 покемона для боя.");
            return;
        }

        System.out.println("Выберите двух покемонов для битвы:");
        for (int index = 0; index < pokemons.size(); index++) {
            val pokemon = pokemons.get(index);
            System.out.printf("%d - %s (%s, HP: %d)\n", index + 1, pokemon.getName(),
                pokemon.getClass().getSimpleName(), pokemon.getHealth());
        }

        System.out.print("Первый покемон: ");
        val firstPokemonIndex = safeIntInput() - 1;
        System.out.print("Второй покемон: ");
        val secondPokemonIndex = safeIntInput() - 1;

        if (firstPokemonIndex == secondPokemonIndex || firstPokemonIndex < 0 || secondPokemonIndex < 0
            || firstPokemonIndex >= pokemons.size() || secondPokemonIndex >= pokemons.size()) {
            System.out.println("Неверный выбор.");
            return;
        }

        val firstPokemon = pokemons.get(firstPokemonIndex);
        val secondPokemon = pokemons.get(secondPokemonIndex);

        System.out.printf("\n⚔️ Битва: %s VS %s ⚔️\n", firstPokemon.getName(), secondPokemon.getName());

        var turnCounter = 0;
        while (firstPokemon.getHealth() > 0 && secondPokemon.getHealth() > 0) {
            val attacker = (turnCounter % 2 == 0) ? firstPokemon : secondPokemon;
            val defender = (turnCounter % 2 == 0) ? secondPokemon : firstPokemon;

            System.out.printf("\n🎮 Ход: %s\n", attacker.getName());
            System.out.println("1. Атаковать");
            System.out.println("2. Защититься");
            System.out.println("3. Способность");
            System.out.println("4. Спец. атака");
            System.out.println("5. Защитная способность");
            System.out.println("6. Эволюция");
            System.out.print("Выбор: ");
            val userChoice = scanner.next();

            switch (userChoice) {
                case "1" -> {
                    val defenderHealthBeforeAttack = defender.getHealth();
                    attacker.attack(defender);
                    System.out.printf("💥 %s атаковал %s на %d урона\n",
                        attacker.getName(), defender.getName(),
                        defenderHealthBeforeAttack - defender.getHealth());
                }
                case "2" -> {
                    attacker.defend();
                    System.out.printf("🛡️ %s активировал защиту\n", attacker.getName());
                }
                case "3" -> {
                    val attackerHealthBeforeAbility = attacker.getHealth();
                    attacker.ability();
                    val healthGained = attacker.getHealth() - attackerHealthBeforeAbility;
                    System.out.printf("✨ %s использовал способность (+%d HP)\n", attacker.getName(), healthGained);
                }
                case "4" -> {
                    if (attacker instanceof FirePokemon firePokemon) {
                        val defenderHealthBeforeSpecial = defender.getHealth();
                        firePokemon.fireBall(defender);
                        System.out.printf("🔥 Fire Ball! Нанесено %d урона\n",
                            defenderHealthBeforeSpecial - defender.getHealth());
                    } else if (attacker instanceof WaterPokemon waterPokemon) {
                        val defenderHealthBeforeSpecial = defender.getHealth();
                        waterPokemon.waveAttack(defender);
                        System.out.printf("🌊 Wave Attack! Нанесено %d урона\n",
                            defenderHealthBeforeSpecial - defender.getHealth());
                    } else {
                        System.out.println("Нет специальной атаки.");
                    }
                }
                case "5" -> {
                    if (attacker instanceof FirePokemon firePokemon) {
                        firePokemon.fireThorns();
                        System.out.println("🔥 Fire Thorns активирован.");
                    } else if (attacker instanceof WaterPokemon waterPokemon) {
                        waterPokemon.waterHide();
                        System.out.println("🌊 Water Hide активирован.");
                    } else {
                        System.out.println("Нет защитной способности.");
                    }
                }
                case "6" -> {
                    attacker.evolve();
                    System.out.println("🆙 Эволюция завершена!");
                }
                default -> System.out.println("⛔ Пропуск хода из-за неверного ввода.");
            }

            System.out.printf("📊 %s (HP: %d) vs %s (HP: %d)\n",
                firstPokemon.getName(), firstPokemon.getHealth(),
                secondPokemon.getName(), secondPokemon.getHealth());

            turnCounter++;
        }

        val winner = (firstPokemon.getHealth() > 0) ? firstPokemon : secondPokemon;
        System.out.printf("🏆 Победитель: %s!\n", winner.getName());
    }

    private int safeIntInput() {
        try {
            return Integer.parseInt(scanner.next());
        } catch (NumberFormatException thrown) {
            return -1;
        }
    }
}
