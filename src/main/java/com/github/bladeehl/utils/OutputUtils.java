package com.github.bladeehl.utils;

import com.github.bladeehl.model.Trainer;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.IntStream;

@UtilityClass
public class OutputUtils {
    public void printTrainers(final List<Trainer> trainers) {
        IntStream.range(0, trainers.size())
            .forEach(i -> System.out.printf("%d - %s%n",
                i + 1,
                trainers.get(i).getName()));
    }

    public void printPokemons(final List<?> pokemons) {
        IntStream.range(0, pokemons.size())
            .forEach(i -> System.out.printf("%d - %s%n",
                i + 1,
                pokemons.get(i).toString()));
    }
}
