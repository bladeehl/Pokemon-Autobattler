package com.github.bladeehl.services;

import com.github.bladeehl.model.Pokemon;
import lombok.NonNull;

public interface ConsoleBattleWebService {
    void startBattle(
        @NonNull final Pokemon firstPokemon,
        @NonNull final Pokemon secondPokemon);

    String getCurrentOutput();

    String processInput(@NonNull final String input);
}
