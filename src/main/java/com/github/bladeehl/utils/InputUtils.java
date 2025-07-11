package com.github.bladeehl.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class InputUtils {
    private static final Scanner scanner = new Scanner(System.in);

    public static int promptForInt(String message) {
        System.out.print(message);
        return safeIntInput();
    }

    private static int safeIntInput() {
        try {
            return Integer.parseInt(scanner.next());
        } catch (NumberFormatException e) {
            log.warn("Ошибка ввода числа", e);
            return -1;
        }
    }

    public static String promptForString(String message) {
        System.out.print(message);
        return safeStringInput();
    }

    private static String safeStringInput() {
        try {
            return scanner.next();
        } catch (Exception e) {
            log.warn("Ошибка ввода строки", e);
            return "";
        }
    }
}
