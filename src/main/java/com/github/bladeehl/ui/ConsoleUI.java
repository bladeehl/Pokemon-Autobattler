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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class ConsoleUI {
    @NonNull TrainerService trainerService;
    @NonNull PokemonService pokemonService;
    @NonNull BattleUI battleUI;
    @NonNull IOContext ioContext;

    public void run() {
        while (true) {
            val trainer = showTrainerMenu();

            if (trainer == null) {
                ioContext.println("Выход");
                break;
            }

            showTrainerActions(trainer);
        }
    }

    private Trainer showTrainerMenu() {
        while (true) {
            ioContext.println("""
                
                --- Тренерское меню ---
                1. Создать тренера
                2. Выбрать тренера
                0. Выход
                """);

            val userChoice = ioContext.promptForInt("Ваш выбор: ");

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
                    ioContext.println("Неверный выбор. Попробуйте снова.");
                }
            }
        }
    }

    private Trainer handleCreateTrainer() {
        val trainerName = ioContext.promptForString("Введите имя: ");
        val newTrainer = trainerService.createTrainer(trainerName);

        ioContext.println("Тренер создан.");

        return newTrainer;
    }

    private Trainer handleSelectTrainer() {
        val pageSize = 10;
        var currentPage = 0;

        while (true) {
            val pageable = PageRequest.of(currentPage, pageSize, Sort.by("id"));
            val page = trainerService.getAll(pageable);

            if (page.getContent().isEmpty()) {
                ioContext.println("Тренеров нет.");
                return null;
            }

            ioContext.printf("Список тренеров (страница %d из %d):%n", currentPage + 1, page.getTotalPages());
            ioContext.printTrainers(page.getContent());

            ioContext.println("Введите номер тренера, -1 для предыдущей страницы, 0 для следующей:");
            val input = ioContext.promptForInt("Выберите номер: ");

            if (input == -1) {
                if (currentPage <= 0) {
                    ioContext.println("Это первая страница.");
                    continue;
                }

                currentPage--;
                continue;
            }

            if (input == 0) {
                if (currentPage >= page.getTotalPages() - 1) {
                    ioContext.println("Это последняя страница.");
                    continue;
                }

                currentPage++;
                continue;
            }

            try {
                val globalIndex = (currentPage * pageSize) + input;

                return trainerService.getTrainerByIndex(globalIndex);
            } catch (TrainerNotFoundException thrown) {
                log.warn("Некорректный индекс: {}", input);
                ioContext.println("Некорректный выбор. Попробуйте снова.");
            }
        }
    }


    private void showTrainerActions(@NonNull final Trainer trainer) {
        while (true) {
            ioContext.printf("""
            
            --- Меню %s ---
            1. Начать бой
            2. Создать покемона
            3. Изменить покемона
            4. Удалить покемона
            5. Показать покемонов
            0. Назад
            """,
            trainer.getName());

            val userChoice = ioContext.promptForInt("Выбор: ");

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
                    ioContext.println("Неверный выбор. Попробуйте снова.");
                }
            }
        }
    }

    private void createPokemon(@NonNull final Trainer trainer) {
        val type = ioContext.promptForInt("Выберите тип покемона (1 - Огненный, 2 - Водяной): ");
        val name = ioContext.promptForString("Имя: ");
        val hp = ioContext.promptForInt("Здоровье: ");
        val damage = ioContext.promptForInt("Урон: ");

        switch (type) {
            case 1 -> {
                val fireRes = ioContext.promptForInt("Огненная защита: ");
                val firePwr = ioContext.promptForInt("Огненная сила: ");

                pokemonService.saveFirePokemon(
                    trainer,
                    name,
                    hp,
                    damage,
                    fireRes,
                    firePwr);
            }
            case 2 -> {
                val waterRes = ioContext.promptForInt("Водная защита: ");
                val waterPwr = ioContext.promptForInt("Водная сила: ");

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

        ioContext.printPokemons(pokemons);
    }

    private void updatePokemon(@NonNull final Trainer trainer) {
        val pokemons = pokemonService.getByTrainer(trainer);

        ioContext.printPokemons(pokemons);

        val selectedIndex = ioContext.promptForInt("Выберите покемона: ");

        if (selectedIndex < 1 || selectedIndex > pokemons.size()) {
            log.warn("Некорректный выбор покемона");
            return;
        }

        val selectedPokemon = pokemons.get(selectedIndex - 1);

        ioContext.println("""
        1. Имя
        2. HP
        3. Урон
        """);

        val fieldChoice = ioContext.promptForInt("Что изменить: ");

        switch (fieldChoice) {
            case 1 -> {
                val newName = ioContext.promptForString("Новое имя: ");

                selectedPokemon.setName(newName);
            }
            case 2 -> selectedPokemon.setHealth(ioContext.promptForInt("Новое здоровье: "));
            case 3 -> selectedPokemon.setDamage(ioContext.promptForInt("Новый урон: "));
            default -> ioContext.println("Неверный выбор.");
        }

        pokemonService.updatePokemon(selectedPokemon);
    }

    private void deletePokemon(@NonNull final Trainer trainer) {
        val pokemons = pokemonService.getByTrainer(trainer);

        ioContext.printPokemons(pokemons);

        val selectedIndex = ioContext.promptForInt("Удалить покемона номер: ");

        if (selectedIndex > 0
            && selectedIndex <= pokemons.size()) {
            pokemonService.deletePokemon(pokemons.get(selectedIndex - 1));
        } else {
            log.warn("Некорректный выбор при удалении покемона: {}", selectedIndex);
            ioContext.println("Некорректный выбор.");
        }
    }
}
