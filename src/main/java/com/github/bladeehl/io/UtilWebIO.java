package com.github.bladeehl.io;

import com.github.bladeehl.model.BattleAction;
import com.github.bladeehl.model.InputType;
import com.github.bladeehl.model.State;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.util.OptionalInt;

@UtilityClass
@FieldDefaults(makeFinal = true)
public class UtilWebIO {
    public String INVALID_CHOICE = "Неверный dыбор. Попробуйте снова.";
    public String INVALID_POKEMON_SELECTION = "Некорректный выбор покемонов. Бой отменён.";

    public String getErrorMessage(@NonNull final String message) {
        return "Ошибка: " + message;
    }

    public OptionalInt parseInt(
        @NonNull final String input,
        @NonNull final StringBuilder output) {
        try {
            return OptionalInt.of(Integer.parseInt(input.trim()));
        } catch (NumberFormatException thrown) {
            return OptionalInt.empty();
        }
    }

    public static BattleAction parseBattleAction(@NonNull final String input) {
        try {
            return BattleAction.fromValue(Integer.parseInt(input.trim()));
        } catch (IllegalArgumentException thrown) {
            throw new IllegalArgumentException("Некорректное действие: " + input, thrown);
        }
    }

    public static State parseState(String stateStr) {
        return parseEnum(stateStr, State.class, "Неизвестное состояние");
    }

    public static InputType parseInputType(String inputTypeStr) {
        return parseEnum(inputTypeStr, InputType.class, "Неизвестный тип ввода");
    }

    public static <T extends Enum<T>> T parseEnum(
        @NonNull final String str,
        @NonNull final Class<T> enumType,
        @NonNull final String errorMessagePrefix) {
        try {
            return Enum.valueOf(enumType,
                str.replaceAll("([a-z])([A-Z])", "$1_$2")
                    .replaceAll("([A-Z])([A-Z][a-z])", "$1_$2")
                    .toUpperCase());
        } catch (IllegalArgumentException thrown) {
            throw new IllegalArgumentException(errorMessagePrefix + ": " + str);
        }
    }

}
