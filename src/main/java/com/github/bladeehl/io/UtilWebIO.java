package com.github.bladeehl.io;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UtilWebIO {
    public String getInvalidChoiceMessage() {
        return "Неверный выбор. Попробуйте снова.";
    }

    public String getInvalidPokemonSelectionMessage() {
        return "Некорректный выбор покемонов. Бой отменён.";
    }

    public String getInvalidInputMessage() {
        return "Ошибка: введите корректное целое число.\n";
    }

    public String getErrorMessage(@NonNull final String message) {
        return "Ошибка: " + message;
    }

    public int parseInt(
        @NonNull final String input,
        @NonNull final StringBuilder output) {

        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException thrown) {
            output.append(getInvalidInputMessage());
            return -1;
        }
    }
}
