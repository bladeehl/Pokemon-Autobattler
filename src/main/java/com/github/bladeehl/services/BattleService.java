package com.github.bladeehl.services;

import com.github.bladeehl.model.FirePokemon;
import com.github.bladeehl.model.Pokemon;
import com.github.bladeehl.model.WaterPokemon;
import lombok.Getter;
import lombok.val;

@Getter
public class BattleService {
    private Pokemon firstPokemon;
    private Pokemon secondPokemon;
    private boolean isFirstPlayersTurn;

    public boolean canBattle(final int numberOfPokemons) {
        return numberOfPokemons >= 2;
    }

    public void startBattle(final Pokemon first, final Pokemon second) {
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
        val hpBefore = opponentPokemon.getHealth();
        playablePokemon.attack(opponentPokemon);
        return hpBefore - opponentPokemon.getHealth();
    }

    public void defend(final Pokemon playablePokemon) {
        playablePokemon.defend();
    }

    public int useAbility(final Pokemon playablePokemon) {
        val hpBefore = playablePokemon.getHealth();
        playablePokemon.ability();
        return playablePokemon.getHealth() - hpBefore;
    }

    public int specialAttack(final Pokemon playablePokemon, final Pokemon opponentPokemon) {
        if (playablePokemon instanceof FirePokemon firePokemon) {
            val hpBefore = opponentPokemon.getHealth();
            firePokemon.fireBall(opponentPokemon);
            return hpBefore - opponentPokemon.getHealth();
        }

        if (playablePokemon instanceof WaterPokemon waterPokemon) {
            val hpBefore = opponentPokemon.getHealth();
            waterPokemon.waveAttack(opponentPokemon);
            return hpBefore - opponentPokemon.getHealth();
        }

        return 0;
    }

    public void defensiveAbility(final Pokemon playablePokemon) {
        if (playablePokemon instanceof FirePokemon firePokemon) {
            firePokemon.fireThorns();
        }

        if (playablePokemon instanceof WaterPokemon waterPokemon) {
            waterPokemon.waterHide();
        }
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
