package com.github.bladeehl.utils;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.experimental.UtilityClass;

import java.util.Scanner;

@Slf4j
@UtilityClass
public class InputUtils {
    private final Scanner scanner = new Scanner(System.in);

    public int promptForInt(final @NonNull String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(scanner.next());
            } catch (NumberFormatException thrown) {
                log.warn("Ошибка ввода числа, попробуйте ещё раз", thrown);
                System.out.println("Ошибка: введите корректное целое число.");
            }
        }
    }

    public String promptForString(final @NonNull String message) {
        System.out.print(message);
        return safeStringInput();
    }

    private String safeStringInput() {
        try {
            return scanner.next();
        } catch (Exception thrown) {
            log.warn("Ошибка ввода строки", thrown);
            return "";
        }
    }
}
