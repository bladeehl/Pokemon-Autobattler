package com.github.bladeehl.services;

import com.github.bladeehl.model.Pokemon;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class BattleService{
    Pokemon firstPokemon;
    Pokemon secondPokemon;
    boolean isFirstPlayersTurn;

    public void startBattle(
        @NonNull final Pokemon first,
        @NonNull final Pokemon second) {

        this.firstPokemon = first;
        this.secondPokemon = second;
        this.isFirstPlayersTurn = true;
    }

    public boolean isBattleOver() {
        return firstPokemon.getHealth() <= 0
            || secondPokemon.getHealth() <= 0;
    }

    public Pokemon getCurrentPlayablePokemon() {
        return isFirstPlayersTurn
            ? firstPokemon
            : secondPokemon;
    }

    public Pokemon getCurrentOpponentPokemon() {
        return isFirstPlayersTurn
            ? secondPokemon
            : firstPokemon;
    }

    public void nextTurn() {
        isFirstPlayersTurn = !isFirstPlayersTurn;
    }

    public int attack(
        @NonNull final Pokemon playablePokemon,
        @NonNull final Pokemon opponentPokemon) {

        return playablePokemon.attack(opponentPokemon);
    }

    public void defend(@NonNull final Pokemon playablePokemon) {
        playablePokemon.defend();
    }

    public int useAbility(@NonNull final Pokemon playablePokemon) {
        return playablePokemon.ability();
    }

    public int specialAttack(
        @NonNull final Pokemon playablePokemon,
        @NonNull final Pokemon opponentPokemon) {

        return playablePokemon.specialAttack(opponentPokemon);
    }

    public void defensiveAbility(@NonNull final Pokemon playablePokemon) {
        playablePokemon.defensiveAbility();
    }

    public void evolve(@NonNull final Pokemon playablePokemon) {
        playablePokemon.evolve();
    }

    public Pokemon getWinner() {
        return firstPokemon.getHealth() > 0
            ? firstPokemon
            : secondPokemon;
    }
}
