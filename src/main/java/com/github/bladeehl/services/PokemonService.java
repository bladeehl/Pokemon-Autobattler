package com.github.bladeehl.services;

import com.github.bladeehl.model.*;
import com.github.bladeehl.repositories.PokemonRepository;
import lombok.val;

import java.util.List;

public class PokemonService {

    private static final PokemonRepository pokemonRepository = new PokemonRepository();

    public static void saveFirePokemon(
        final Trainer trainer,
        final String name,
        final int health,
        final int damage,
        final int fireResistance,
        final int firePower
    ) {
        pokemonRepository.savePokemon(
            FirePokemon.builder()
                .name(name)
                .health(health)
                .damage(damage)
                .fireResistance(Math.max(0, fireResistance))
                .firePower(Math.max(0, firePower))
                .trainer(trainer)
                .build()
        );
    }

    public static void saveWaterPokemon(
        final Trainer trainer,
        final String name,
        final int health,
        final int damage,
        final int waterResistance,
        final int waterPower
    ) {
        pokemonRepository.savePokemon(
            WaterPokemon.builder()
                .name(name)
                .health(health)
                .damage(damage)
                .waterResistance(Math.max(0, waterResistance))
                .waterPower(Math.max(0, waterPower))
                .trainer(trainer)
                .build()
        );
    }


    public static List<Pokemon> getPokemonsByTrainer(final Trainer trainer) {
        return pokemonRepository.getPokemonsByTrainer(trainer);
    }

    public static void updatePokemon(final Pokemon pokemon) {
        pokemonRepository.updatePokemon(pokemon);
    }

    public static void deletePokemon(final Pokemon pokemon) {
        pokemonRepository.deletePokemon(pokemon);
    }
}
