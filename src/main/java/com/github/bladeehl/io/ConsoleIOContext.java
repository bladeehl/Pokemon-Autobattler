package com.github.bladeehl.io;

import com.github.bladeehl.model.Pokemon;
import com.github.bladeehl.model.Trainer;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

@Component
@FieldDefaults(makeFinal = true)
@Slf4j
public class ConsoleIOContext implements IOContext {
    Scanner in;
    PrintStream out;

    public ConsoleIOContext() {
        this.in = new Scanner(System.in);
        this.out = new PrintStream(System.out, true);
    }

    @Override
    public int promptForInt(@NonNull final String prompt) {
        while (true) {
            out.print(prompt);
            try {
                return Integer.parseInt(in.next());
            } catch (NumberFormatException thrown) {
                log.warn("Ошибка ввода числа, попробуйте ещё раз", thrown);
                out.println("Ошибка: введите корректное целое число.");
            }
        }
    }

    @Override
    public String promptForString(@NonNull final String prompt) {
        out.print(prompt);
        return safeStringInput();
    }

    private String safeStringInput() {
        try {
            in.nextLine();
            return in.nextLine();
        } catch (Exception thrown) {
            log.warn("Ошибка ввода строки", thrown);
            return "";
        }
    }

    @Override
    public void printPokemons(@NonNull final List<Pokemon> pokemons) {
        if (pokemons.isEmpty()) {
            out.println("Нет покемонов.");
            return;
        }

        IntStream.range(0, pokemons.size())
            .forEach(i -> out.printf(
                "%d - %s%n",
                i + 1,
                pokemons.get(i).toString()));
    }

    @Override
    public void printTrainers(@NonNull final List<Trainer> trainers) {
        IntStream.range(0, trainers.size())
            .forEach(i -> out.printf(
                "%d - %s%n",
                i + 1,
                trainers.get(i).getName()));
    }

    @Override
    public void println(@NonNull final String message) {
        out.println(message);
    }

    @Override
    public void printf(@NonNull final String format,
                       @NonNull final Object... args) {
        out.printf(format, args);
    }
}
