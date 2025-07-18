package com.github.bladeehl.ui;

import com.github.bladeehl.exceptions.TrainerNotFoundException;
import com.github.bladeehl.io.IOContext;
import com.github.bladeehl.model.Trainer;
import com.github.bladeehl.services.PokemonService;
import com.github.bladeehl.utils.InputUtils;
import com.github.bladeehl.services.TrainerService;
import com.github.bladeehl.utils.OutputUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.NonNull;
import lombok.val;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConsoleUI {
    final TrainerService trainerService;
    final PokemonService pokemonService;
    final BattleUI battleUI;
    final IOContext io;
    final InputUtils inputUtils;
    final OutputUtils outputUtils;

    public void run() {
        while (true) {
            val trainer = showTrainerMenu();

            if (trainer == null) {
                io.out().println("Выход");
                break;
            }

            showTrainerActions(trainer);
        }
    }

    private Trainer showTrainerMenu() {
        while (true) {
            io.out().println("""
                
                --- Тренерское меню ---
                1. Создать тренера
                2. Выбрать тренера
                0. Выход
                """);

            val userChoice = inputUtils.promptForInt("Ваш выбор: ");

            switch (userChoice) {
                case 1 -> {
                    return handleCreateTrainer();
                }
                case 2 -> {
                    val selectedTrainer = handleSelectTrainer();

                    if (selectedTrainer != null) {
                        return selectedTrainer;
                    }
                }
                case 0 -> {
                    return null;
                }
                default -> {
                    log.warn("Пользователь ввёл некорректный выбор в меню тренеров: {}", userChoice);
                    io.out().println("Неверный выбор. Попробуйте снова.");
                }
            }
        }
    }

    private Trainer handleCreateTrainer() {
        val trainerName = inputUtils.promptForString("Введите имя: ");
        val newTrainer = trainerService.createTrainer(trainerName);

        io.out().println("Тренер создан.");

        return newTrainer;
    }

    private Trainer handleSelectTrainer() {
        val allTrainers = trainerService.getAllTrainers();

        if (allTrainers.isEmpty()) {
            io.out().println("Тренеров нет.");
            return null;
        }

        outputUtils.printTrainers(allTrainers);

        while (true) {
            val selectedIndex = inputUtils.promptForInt("Выберите номер: ");

            try {
                return trainerService.getTrainerByIndex(selectedIndex);
            } catch (TrainerNotFoundException thrown) {
                log.warn("Некорректный индекс: {}", selectedIndex);
                io.out().println("Некорректный выбор. Попробуйте снова.");
            }
        }
    }

    private void showTrainerActions(final @NonNull Trainer trainer) {
        while (true) {
            io.out().printf("""
            
            --- Меню %s ---
            1. Начать бой
            2. Создать покемона
            3. Изменить покемона
            4. Удалить покемона
            5. Показать покемонов
            0. Назад
            """,
            trainer.getName());

            val userChoice = inputUtils.promptForInt("Выбор: ");

            switch (userChoice) {
                case 1 -> battleUI.startBattle(trainer);
                case 2 -> createPokemon(trainer);
                case 3 -> updatePokemon(trainer);
                case 4 -> deletePokemon(trainer);
                case 5 -> showPokemons(trainer);
                case 0 -> {
                    return;
                }
                default -> {
                    log.warn("Пользователь ввёл некорректный вариант в меню тренера: {}", userChoice);
                    io.out().println("Неверный выбор. Попробуйте снова.");
                }
            }
        }
    }

    private void createPokemon(final @NonNull Trainer trainer) {
        val type = inputUtils.promptForInt("Выберите тип покемона (1 - Огненный, 2 - Водяной): ");
        val name = inputUtils.promptForString("Имя: ");
        val hp = inputUtils.promptForInt("Здоровье: ");
        val damage = inputUtils.promptForInt("Урон: ");

        switch (type) {
            case 1 -> {
                val fireRes = inputUtils.promptForInt("Огненная защита: ");
                val firePwr = inputUtils.promptForInt("Огненная сила: ");

                pokemonService.saveFirePokemon(
                    trainer,
                    name,
                    hp,
                    damage,
                    fireRes,
                    firePwr);
            }
            case 2 -> {
                val waterRes = inputUtils.promptForInt("Водная защита: ");
                val waterPwr = inputUtils.promptForInt("Водная сила: ");

                pokemonService.saveWaterPokemon(
                    trainer,
                    name,
                    hp,
                    damage,
                    waterRes,
                    waterPwr);
            }
            default -> {
                log.warn("Пользователь ввёл неверный вариант");
            }
        }
    }

    private void showPokemons(final @NonNull Trainer trainer) {
        val pokemons = pokemonService.getPokemonsByTrainer(trainer);

        outputUtils.printPokemons(pokemons);
    }

    private void updatePokemon(final @NonNull Trainer trainer) {
        val pokemons = pokemonService.getPokemonsByTrainer(trainer);

        outputUtils.printPokemons(pokemons);

        val selectedIndex = inputUtils.promptForInt("Выберите покемона: ");

        if (selectedIndex < 1 || selectedIndex > pokemons.size()) {
            log.warn("Некорректный выбор покемона");
            return;
        }

        val selectedPokemon = pokemons.get(selectedIndex - 1);

        io.out().println("""
        1. Имя
        2. HP
        3. Урон
        """);

        val fieldChoice = inputUtils.promptForInt("Что изменить: ");

        switch (fieldChoice) {
            case 1 -> {
                val newName = inputUtils.promptForString("Новое имя: ");
                selectedPokemon.setName(newName);
            }
            case 2 -> selectedPokemon.setHealth(inputUtils.promptForInt("Новое здоровье: "));
            case 3 -> selectedPokemon.setDamage(inputUtils.promptForInt("Новый урон: "));
            default -> io.out().println("Неверный выбор.");
        }

        pokemonService.updatePokemon(selectedPokemon);
    }

    private void deletePokemon(final @NonNull Trainer trainer) {
        val pokemons = pokemonService.getPokemonsByTrainer(trainer);

        outputUtils.printPokemons(pokemons);

        val selectedIndex = inputUtils.promptForInt("Удалить покемона номер: ");
        if (selectedIndex > 0 && selectedIndex <= pokemons.size()) {
            pokemonService.deletePokemon(pokemons.get(selectedIndex - 1));
        } else {
            log.warn("Некорректный выбор при удалении покемона: {}", selectedIndex);
            io.out().println("Некорректный выбор.");
        }
    }
}
