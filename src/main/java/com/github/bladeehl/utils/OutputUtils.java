package com.github.bladeehl.utils;

import com.github.bladeehl.io.IOContext;
import com.github.bladeehl.model.Trainer;
import com.github.bladeehl.model.Pokemon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class OutputUtils {
    final IOContext io;

    public void printTrainers(final List<Trainer> trainers) {
        IntStream.range(0, trainers.size())
            .forEach(i -> io.out().printf(
                "%d - %s%n",
                i + 1,
                trainers.get(i).getName()));
    }

    public void printPokemons(final List<Pokemon> pokemons) {
        if (pokemons.isEmpty()) {
            io.out().println("Нет покемонов.");
            return;
        }

        IntStream.range(0, pokemons.size())
            .forEach(i -> io.out().printf(
                "%d - %s%n",
                i + 1,
                pokemons.get(i)
                    .toString()));
    }
}
