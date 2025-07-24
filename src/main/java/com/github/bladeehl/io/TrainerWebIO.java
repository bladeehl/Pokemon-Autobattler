package com.github.bladeehl.io;

import com.github.bladeehl.model.Trainer;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.List;
import java.util.stream.IntStream;

@UtilityClass
@FieldDefaults(makeFinal = true)

public class TrainerWebIO {
    public String TRAINER_MENU_PROMT = """
            --- Тренерское меню ---
            1. Создать тренера
            2. Выбрать тренера
            0. Выход

            Ваш выбор: """;

    public String CREATE_TRAINER_PROMPT = "Введите имя: ";
    public String NO_TRAINERS_MESSAGE = "Тренеров нет.";

    public String getSelectTrainerPrompt(@NonNull final List<Trainer> trainers) {
        return formatTrainerPrompt(trainers, "Выберите номер: ");
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

    private String formatTrainerPrompt(
        @NonNull final List<Trainer> trainers,
        @NonNull final String prompt) {

        val output = new StringBuilder();
        appendTrainers(output, trainers);
        output.append(prompt);

        return output.toString();
    }

    public void appendTrainers(
        @NonNull final StringBuilder output,
        @NonNull final List<Trainer> trainers) {

        if (trainers.isEmpty()) {
            output.append(NO_TRAINERS_MESSAGE).append(System.lineSeparator());

            return;
        }

        IntStream.range(0, trainers.size())
            .mapToObj(i -> "%d - %s%n".formatted(i + 1,
                trainers
                    .get(i)
                    .getName()))
            .forEach(output::append);
    }
}
