package com.github.bladeehl.io;

import com.github.bladeehl.model.Trainer;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.List;

@UtilityClass
public class TrainerWebIO {
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

    public void appendTrainers(
            @NonNull final StringBuilder output,
            @NonNull final List<Trainer> trainers) {

        if (trainers.isEmpty()) {
            output.append("Тренеров нет.\n");
            return;
        }
        trainers.stream()
                .map(trainer -> "%d - %s%n".formatted(trainers.indexOf(trainer) + 1,
                        trainer.getName()))
                .forEach(output::append);
    }
}
