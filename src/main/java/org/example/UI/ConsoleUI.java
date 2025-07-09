package org.example.UI;

import lombok.val;
import org.example.dao.PokemonDAO;
import org.example.dao.TrainerDAO;
import org.example.models.*;
import org.example.service.BattleService;

import java.util.Scanner;

public class ConsoleUI {

    private final Scanner scanner = new Scanner(System.in);
    private Trainer currentTrainer = null;

    public void run() {
        while (true) {
            showTrainerMenu();
            if (currentTrainer != null) {
                showTrainerActions();
            }
        }
    }

    private void showTrainerMenu() {
        currentTrainer = null;
        while (currentTrainer == null) {
            System.out.println("\n--- Тренерское меню ---");
            System.out.println("1. Создать тренера");
            System.out.println("2. Выбрать тренера");
            System.out.println("0. Выход");
            System.out.print("Ваш выбор: ");
            val userChoice = safeIntInput();

            switch (userChoice) {
                case 1 -> {
                    System.out.print("Введите имя: ");
                    val trainerName = scanner.next();
                    val newTrainer = new Trainer();
                    newTrainer.setName(trainerName);
                    TrainerDAO.saveTrainer(newTrainer);
                    currentTrainer = newTrainer;
                    System.out.println("Тренер создан.");
                }
                case 2 -> {
                    val allTrainers = TrainerDAO.getAllTrainers();
                    if (allTrainers.isEmpty()) {
                        System.out.println("Тренеров нет.");
                        return;
                    }

                    for (int index = 0; index < allTrainers.size(); index++) {
                        System.out.printf("%d - %s\n", index + 1, allTrainers.get(index).getName());
                    }

                    System.out.print("Выберите номер: ");
                    val selectedIndex = safeIntInput();
                    if (selectedIndex > 0 && selectedIndex <= allTrainers.size()) {
                        currentTrainer = allTrainers.get(selectedIndex - 1);
                    } else {
                        System.out.println("Некорректный выбор.");
                    }
                }
                case 0 -> {
                    System.out.println("Выход...");
                    System.exit(0);
                }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private void showTrainerActions() {
        var isBackRequested = false;
        while (!isBackRequested) {
            System.out.printf("\n--- Меню %s ---\n", currentTrainer.getName());
            System.out.println("1. Начать бой");
            System.out.println("2. Создать покемона");
            System.out.println("3. Изменить покемона");
            System.out.println("4. Удалить покемона");
            System.out.println("5. Показать покемонов");
            System.out.println("0. Назад");
            System.out.print("Выбор: ");
            val userChoice = safeIntInput();

            switch (userChoice) {
                case 1 -> new BattleService().startBattle(currentTrainer);
                case 2 -> createPokemon();
                case 3 -> updatePokemon();
                case 4 -> deletePokemon();
                case 5 -> showPokemons();
                case 0 -> isBackRequested = true;
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private void createPokemon() {
        System.out.println("1. Огненный\n2. Водяной");
        val pokemonType = safeIntInput();
        System.out.print("Имя: ");
        val pokemonName = scanner.next();
        val pokemonHealth = promptFor("Здоровье: ");
        val pokemonDamage = promptFor("Урон: ");

        switch (pokemonType) {
            case 1 -> {
                val fireResistance = promptFor("Огненная защита: ");
                val firePower = promptFor("Огненная сила: ");
                val firePokemon = new FirePokemon(pokemonName, pokemonHealth, pokemonDamage, fireResistance, firePower);
                firePokemon.setTrainer(currentTrainer);
                PokemonDAO.savePokemon(firePokemon);
            }
            case 2 -> {
                val waterResistance = promptFor("Водная защита: ");
                val waterPower = promptFor("Водная сила: ");
                val waterPokemon = new WaterPokemon(pokemonName, pokemonHealth, pokemonDamage, waterResistance, waterPower);
                waterPokemon.setTrainer(currentTrainer);
                PokemonDAO.savePokemon(waterPokemon);
            }
            default -> System.out.println("Неверный выбор.");
        }
    }

    private void showPokemons() {
        val pokemons = PokemonDAO.getPokemonsByTrainer(currentTrainer);
        if (pokemons.isEmpty()) {
            System.out.println("Нет покемонов.");
        } else {
            pokemons.forEach(System.out::println);
        }
    }

    private void updatePokemon() {
        val pokemons = PokemonDAO.getPokemonsByTrainer(currentTrainer);
        if (pokemons.isEmpty()) {
            System.out.println("Нет покемонов.");
            return;
        }

        for (int index = 0; index < pokemons.size(); index++) {
            System.out.printf("%d - %s\n", index + 1, pokemons.get(index));
        }

        System.out.print("Выберите покемона: ");
        val selectedIndex = safeIntInput();
        if (selectedIndex < 1 || selectedIndex > pokemons.size()) return;

        val selectedPokemon = pokemons.get(selectedIndex - 1);
        System.out.println("1. Имя\n2. HP\n3. Урон");
        val fieldChoice = safeIntInput();

        switch (fieldChoice) {
            case 1 -> {
                System.out.print("Новое имя: ");
                selectedPokemon.setName(scanner.next());
            }
            case 2 -> selectedPokemon.setHealth(promptFor("Новое здоровье: "));
            case 3 -> selectedPokemon.setDamage(promptFor("Новый урон: "));
        }

        PokemonDAO.updatePokemon(selectedPokemon);
    }

    private void deletePokemon() {
        val pokemons = PokemonDAO.getPokemonsByTrainer(currentTrainer);
        if (pokemons.isEmpty()) return;

        for (int index = 0; index < pokemons.size(); index++) {
            System.out.printf("%d - %s\n", index + 1, pokemons.get(index));
        }

        System.out.print("Удалить покемона номер: ");
        val selectedIndex = safeIntInput();
        if (selectedIndex > 0 && selectedIndex <= pokemons.size()) {
            PokemonDAO.deletePokemon(pokemons.get(selectedIndex - 1));
        }
    }

    private int promptFor(final String message) {
        System.out.print(message);
        return safeIntInput();
    }

    private int safeIntInput() {
        try {
            return Integer.parseInt(scanner.next());
        } catch (Exception e) {
            return -1;
        }
    }
}
