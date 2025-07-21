package com.github.bladeehl.io;

import com.github.bladeehl.model.Pokemon;
import com.github.bladeehl.model.Trainer;

import java.util.List;

public interface IOContext {
    int promptForInt(String prompt);
    String promptForString(String prompt);
    void printPokemons(List<Pokemon> pokemons);
    void printTrainers(List<Trainer> trainers);
    void println(String message);
    void printf(String format, Object... args);
}
