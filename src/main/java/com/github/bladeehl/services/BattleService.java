package com.github.bladeehl.services;

import com.github.bladeehl.model.FirePokemon;
import com.github.bladeehl.model.Pokemon;
import com.github.bladeehl.model.WaterPokemon;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j

public class BattleService {
    Pokemon firstPokemon;
    Pokemon secondPokemon;
    boolean isFirstPlayersTurn;

    public void startBattle(final @NonNull Pokemon first, final @NonNull Pokemon second) {
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

    public int attack(final Pokemon playablePokemon, final Pokemon opponentPokemon) {
        return playablePokemon.attack(opponentPokemon);
    }

    public void defend(final Pokemon playablePokemon) {
        playablePokemon.defend();
    }

    public int useAbility(final Pokemon playablePokemon) {
        return playablePokemon.ability();
    }

    public int specialAttack(final Pokemon playablePokemon, final Pokemon opponentPokemon) {
        if (playablePokemon instanceof FirePokemon firePokemon) {
            return firePokemon.fireBall(opponentPokemon);
        }

        if (playablePokemon instanceof WaterPokemon waterPokemon) {
            return waterPokemon.waveAttack(opponentPokemon);
        }

        log.error("Попытка использовать спец. атаку с неподдерживаемым типом покемона");
        throw new IllegalArgumentException("Неподдерживаемый тип покемона для спец. атаки");
    }

    public void defensiveAbility(final Pokemon playablePokemon) {
        if (playablePokemon instanceof FirePokemon firePokemon) {
            firePokemon.fireThorns();
        }

        if (playablePokemon instanceof WaterPokemon waterPokemon) {
            waterPokemon.waterHide();
        }

        log.error("Попытка использовать защитную способность с неподдерживаемым типом покемона");
        throw new IllegalArgumentException("Неподдерживаемый тип покемона для защитной способноси");

    }

    public void evolve(final Pokemon playablePokemon) {
        playablePokemon.evolve();
    }

    public Pokemon getWinner() {
        return firstPokemon.getHealth() > 0
            ? firstPokemon
            : secondPokemon;
    }
}
