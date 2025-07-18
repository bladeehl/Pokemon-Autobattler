package com.github.bladeehl.utils;

import com.github.bladeehl.io.IOContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InputUtils {
    final IOContext io;

    public int promptForInt(final String message) {
        while (true) {
            io.out().print(message);
            try {

                return Integer.parseInt(io.in().next());
            } catch (NumberFormatException e) {
                log.warn("Ошибка ввода числа, попробуйте ещё раз", e);
                io.out().println("Ошибка: введите корректное целое число.");
            }
        }
    }

    public String promptForString(final String message) {
        io.out().print(message);
        return safeStringInput();
    }

    private String safeStringInput() {
        try {
            return io.in().next();
        } catch (Exception e) {
            log.warn("Ошибка ввода строки", e);
            return "";
        }
    }
}
