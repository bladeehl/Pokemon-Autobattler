package com.github.bladeehl.ui;

import com.github.bladeehl.model.Trainer;
import com.github.bladeehl.services.PokemonService;
import com.github.bladeehl.utils.InputUtils;
import com.github.bladeehl.services.TrainerService;
import com.github.bladeehl.utils.OutputUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class ConsoleUI {
    Boolean isRunning = true;

    public void run() {
        while (isRunning) {
            val trainer = showTrainerMenu();
            if (trainer != null) {
                showTrainerActions(trainer);
            }
        }
        System.out.println("Выход");
        System.exit(0);
    }

    private Trainer showTrainerMenu() {
        while (true) {
            System.out.println("""
                
                --- Тренерское меню ---
                1. Создать тренера
                2. Выбрать тренера
                0. Выход
                """);

            val userChoice = InputUtils.promptForInt("Ваш выбор: ");

            switch (userChoice) {
                case 1 -> {
                    return handleCreateTrainer();
                }
                case 2 -> {
                    Trainer selectedTrainer = handleSelectTrainer();
                    if (selectedTrainer != null){
                        return selectedTrainer;
                    }
                }
                case 0 -> {
                    exit();
                    return null;
                }
                default -> {
                    log.warn("Пользователь ввёл некорректный выбор в меню тренеров: {}", userChoice);
                    System.out.println("Неверный выбор. Попробуйте снова.");
                }
            }
        }
    }

    private Trainer handleCreateTrainer() {
        val trainerName = InputUtils.promptForString("Введите имя: ");
        val newTrainer = TrainerService.createTrainer(trainerName);

        System.out.println("Тренер создан.");
        return newTrainer;
    }

    private Trainer handleSelectTrainer() {
        val allTrainers = TrainerService.getAllTrainers();

        if (allTrainers.isEmpty()) {
            System.out.println("Тренеров нет.");
            return null;
        }

        OutputUtils.printTrainers(allTrainers);

        val selectedIndex = InputUtils.promptForInt("Выберите номер: ");
        val trainer = TrainerService.getTrainerByIndex(allTrainers, selectedIndex);

        if (trainer == null) {
            log.warn("Пользователь ввёл некорректный индекс тренера: {}", selectedIndex);
            System.out.println("Некорректный выбор.");
        }

        return trainer;
    }

    private void exit() {
        isRunning = false;
    }

    private void showTrainerActions(final Trainer trainer) {
        while (true) {
            System.out.printf("""
            
            --- Меню %s ---
            1. Начать бой
            2. Создать покемона
            3. Изменить покемона
            4. Удалить покемона
            5. Показать покемонов
            0. Назад
            """, trainer.getName());

            val userChoice = InputUtils.promptForInt("Выбор: ");

            switch (userChoice) {
                case 1 -> new BattleUI().startBattle(trainer);
                case 2 -> createPokemon(trainer);
                case 3 -> updatePokemon(trainer);
                case 4 -> deletePokemon(trainer);
                case 5 -> showPokemons(trainer);
                case 0 -> {
                    return;
                }
                default -> {
                    log.warn("Пользователь ввёл некорректный вариант в меню тренера: {}", userChoice);
                    System.out.println("Неверный выбор. Попробуйте снова.");
                }
            }
        }
    }

    private void createPokemon(final Trainer trainer) {
        val type = InputUtils.promptForInt("Выберите тип покемона (1 - Огненный, 2 - Водяной): ");
        val name = InputUtils.promptForString("Имя: ");
        val hp = InputUtils.promptForInt("Здоровье: ");
        val damage = InputUtils.promptForInt("Урон: ");

        switch (type) {
            case 1 -> {
                val fireRes = InputUtils.promptForInt("Огненная защита: ");
                val firePwr = InputUtils.promptForInt("Огненная сила: ");
                PokemonService.saveFirePokemon(
                    trainer,
                    name,
                    hp,
                    damage,
                    fireRes,
                    firePwr);
            }
            case 2 -> {
                val waterRes = InputUtils.promptForInt("Водная защита: ");
                val waterPwr = InputUtils.promptForInt("Водная сила: ");
                PokemonService.saveWaterPokemon(
                    trainer,
                    name,
                    hp,
                    damage,
                    waterRes,
                    waterPwr);
            }
            default -> {
                log.warn("Пользователь ввёл неверный вариант");
                System.out.println("Неверный выбор.");
            }
        }
    }

    private void showPokemons(final Trainer trainer) {
        val pokemons = PokemonService.getPokemonsByTrainer(trainer);

        if (pokemons.isEmpty()) {
            System.out.println("Нет покемонов.");
            return;
        }

        OutputUtils.printPokemons(pokemons);
    }


    private void updatePokemon(final Trainer trainer) {
        val pokemons = PokemonService.getPokemonsByTrainer(trainer);
        if (pokemons.isEmpty()) {
            System.out.println("Нет покемонов.");
            return;
        }

        OutputUtils.printPokemons(pokemons);

        val selectedIndex = InputUtils.promptForInt("Выберите покемона: ");
        if (selectedIndex < 1 || selectedIndex > pokemons.size()) {
            log.warn("Некорректный выбор покемона");
            return;
        }

        val selectedPokemon = pokemons.get(selectedIndex - 1);

        System.out.println("""
        1. Имя
        2. HP
        3. Урон
        """);

        val fieldChoice = InputUtils.promptForInt("Что изменить: ");

        switch (fieldChoice) {
            case 1 -> {
                val newName = InputUtils.promptForString("Новое имя: ");
                selectedPokemon.setName(newName);
            }
            case 2 -> selectedPokemon.setHealth(InputUtils.promptForInt("Новое здоровье: "));
            case 3 -> selectedPokemon.setDamage(InputUtils.promptForInt("Новый урон: "));
            default -> System.out.println("Неверный выбор.");
        }

        PokemonService.updatePokemon(selectedPokemon);
    }

    private void deletePokemon(final Trainer trainer) {
        val pokemons = PokemonService.getPokemonsByTrainer(trainer);
        if (pokemons.isEmpty()){
            System.out.println("Список покемонов пуст");
            return;
        }

        OutputUtils.printPokemons(pokemons);

        val selectedIndex = InputUtils.promptForInt("Удалить покемона номер: ");
        if (selectedIndex > 0 && selectedIndex <= pokemons.size()) {
            PokemonService.deletePokemon(pokemons.get(selectedIndex - 1));
        } else {
            log.warn("Некорректный выбор при удалении покемона: {}", selectedIndex);
            System.out.println("Некорректный выбор.");
        }

    }
}
