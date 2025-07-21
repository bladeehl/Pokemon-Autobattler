package com.github.bladeehl.ui;

import com.github.bladeehl.exceptions.TrainerNotFoundException;
import com.github.bladeehl.io.IOContext;
import com.github.bladeehl.model.Trainer;
import com.github.bladeehl.services.PokemonService;
import com.github.bladeehl.services.TrainerService;
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
public class ConsoleUI {
    TrainerService trainerService;
    PokemonService pokemonService;
    BattleUI battleUI;
    IOContext io;

    public void run() {
        while (true) {
            val trainer = showTrainerMenu();

            if (trainer == null) {
                io.println("Выход");
                break;
            }

            showTrainerActions(trainer);
        }
    }

    private Trainer showTrainerMenu() {
        while (true) {
            io.println("""
                
                --- Тренерское меню ---
                1. Создать тренера
                2. Выбрать тренера
                0. Выход
                """);

            val userChoice = io.promptForInt("Ваш выбор: ");

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
                    io.println("Неверный выбор. Попробуйте снова.");
                }
            }
        }
    }

    private Trainer handleCreateTrainer() {
        val trainerName = io.promptForString("Введите имя: ");
        val newTrainer = trainerService.createTrainer(trainerName);

        io.println("Тренер создан.");

        return newTrainer;
    }

    private Trainer handleSelectTrainer() {
        val allTrainers = trainerService.getAll();

        if (allTrainers.isEmpty()) {
            io.println("Тренеров нет.");
            return null;
        }

        io.printTrainers(allTrainers);

        while (true) {
            val selectedIndex = io.promptForInt("Выберите номер: ");

            try {
                return trainerService.getTrainerByIndex(selectedIndex);
            } catch (TrainerNotFoundException thrown) {
                log.warn("Некорректный индекс: {}", selectedIndex);
                io.println("Некорректный выбор. Попробуйте снова.");
            }
        }
    }

    private void showTrainerActions(@NonNull final Trainer trainer) {
        while (true) {
            io.printf("""
            
            --- Меню %s ---
            1. Начать бой
            2. Создать покемона
            3. Изменить покемона
            4. Удалить покемона
            5. Показать покемонов
            0. Назад
            """,
            trainer.getName());

            val userChoice = io.promptForInt("Выбор: ");

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
                    io.println("Неверный выбор. Попробуйте снова.");
                }
            }
        }
    }

    private void createPokemon(@NonNull final Trainer trainer) {
        val type = io.promptForInt("Выберите тип покемона (1 - Огненный, 2 - Водяной): ");
        val name = io.promptForString("Имя: ");
        val hp = io.promptForInt("Здоровье: ");
        val damage = io.promptForInt("Урон: ");

        switch (type) {
            case 1 -> {
                val fireRes = io.promptForInt("Огненная защита: ");
                val firePwr = io.promptForInt("Огненная сила: ");

                pokemonService.saveFirePokemon(
                    trainer,
                    name,
                    hp,
                    damage,
                    fireRes,
                    firePwr);
            }
            case 2 -> {
                val waterRes = io.promptForInt("Водная защита: ");
                val waterPwr = io.promptForInt("Водная сила: ");

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

    private void showPokemons(@NonNull final Trainer trainer) {
        val pokemons = pokemonService.getByTrainer(trainer);

        io.printPokemons(pokemons);
    }

    private void updatePokemon(@NonNull final Trainer trainer) {
        val pokemons = pokemonService.getByTrainer(trainer);

        io.printPokemons(pokemons);

        val selectedIndex = io.promptForInt("Выберите покемона: ");

        if (selectedIndex < 1 || selectedIndex > pokemons.size()) {
            log.warn("Некорректный выбор покемона");
            return;
        }

        val selectedPokemon = pokemons.get(selectedIndex - 1);

        io.println("""
        1. Имя
        2. HP
        3. Урон
        """);

        val fieldChoice = io.promptForInt("Что изменить: ");

        switch (fieldChoice) {
            case 1 -> {
                val newName = io.promptForString("Новое имя: ");
                selectedPokemon.setName(newName);
            }
            case 2 -> selectedPokemon.setHealth(io.promptForInt("Новое здоровье: "));
            case 3 -> selectedPokemon.setDamage(io.promptForInt("Новый урон: "));
            default -> io.println("Неверный выбор.");
        }

        pokemonService.updatePokemon(selectedPokemon);
    }

    private void deletePokemon(@NonNull final Trainer trainer) {
        val pokemons = pokemonService.getByTrainer(trainer);

        io.printPokemons(pokemons);

        val selectedIndex = io.promptForInt("Удалить покемона номер: ");
        if (selectedIndex > 0
            && selectedIndex <= pokemons.size()) {
            pokemonService.deletePokemon(pokemons.get(selectedIndex - 1));
        } else {
            log.warn("Некорректный выбор при удалении покемона: {}", selectedIndex);
            io.println("Некорректный выбор.");
        }
    }
}
