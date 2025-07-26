package com.github.bladeehl.services;

import com.github.bladeehl.model.Pokemon;
import lombok.NonNull;

public interface BattleService {
    void startBattle(
        @NonNull Pokemon first,
        @NonNull Pokemon second);

    boolean isBattleOver();

    Pokemon getCurrentPlayablePokemon();

    Pokemon getCurrentOpponentPokemon();

    void nextTurn();

    int attack(
        @NonNull Pokemon playablePokemon,
        @NonNull Pokemon opponentPokemon);

    void defend(@NonNull Pokemon playablePokemon);

    int useAbility(@NonNull Pokemon playablePokemon);

    int specialAttack(
        @NonNull Pokemon playablePokemon,
        @NonNull Pokemon opponentPokemon);

    void defensiveAbility(@NonNull Pokemon playablePokemon);

    void evolve(@NonNull Pokemon playablePokemon);

    Pokemon getWinner();
}
