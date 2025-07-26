package com.github.bladeehl.io;

import com.github.bladeehl.model.Pokemon;
import com.github.bladeehl.model.Trainer;
import lombok.NonNull;

import java.util.List;

public interface IOContext {
    int promptForInt(@NonNull final String prompt);
    String promptForString(@NonNull final String prompt);
    void printPokemons(@NonNull final List<Pokemon> pokemons);
    void printTrainers(@NonNull final List<Trainer> trainers);
    void println(@NonNull final String message);
    void printf(
        @NonNull final String format,
        final Object... args);
}
