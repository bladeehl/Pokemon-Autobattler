package com.github.bladeehl.services;

import com.github.bladeehl.model.FirePokemon;
import com.github.bladeehl.model.Pokemon;
import com.github.bladeehl.model.WaterPokemon;
import com.github.bladeehl.exceptions.UnsupportedPokemonTypeException;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
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

    public int attack(final @NonNull Pokemon playablePokemon, final @NonNull Pokemon opponentPokemon) {
        return playablePokemon.attack(opponentPokemon);
    }

    public void defend(final @NonNull Pokemon playablePokemon) {
        playablePokemon.defend();
    }

    public int useAbility(final @NonNull Pokemon playablePokemon) {
        return playablePokemon.ability();
    }

    public int specialAttack(final @NonNull Pokemon playablePokemon, final @NonNull Pokemon opponentPokemon) {
        if (playablePokemon instanceof FirePokemon firePokemon) {
            return firePokemon.fireBall(opponentPokemon);
        }

        if (playablePokemon instanceof WaterPokemon waterPokemon) {
            return waterPokemon.waveAttack(opponentPokemon);
        }

        throw new UnsupportedPokemonTypeException("Неподдерживаемый тип покемона для спец. атаки");
    }

    public void defensiveAbility(final @NonNull Pokemon playablePokemon) {
        if (playablePokemon instanceof FirePokemon firePokemon) {
            firePokemon.fireThorns();
            return;
        }

        if (playablePokemon instanceof WaterPokemon waterPokemon) {
            waterPokemon.waterHide();
            return;
        }

        throw new UnsupportedPokemonTypeException("Неподдерживаемый тип покемона для защитной способности");
    }

    public void evolve(final @NonNull Pokemon playablePokemon) {
        playablePokemon.evolve();
    }

    public Pokemon getWinner() {
        return firstPokemon.getHealth() > 0
            ? firstPokemon
            : secondPokemon;
    }
}
