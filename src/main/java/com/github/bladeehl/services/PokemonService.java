package com.github.bladeehl.services;

import com.github.bladeehl.model.*;
import com.github.bladeehl.repositories.PokemonRepository;
import lombok.NonNull;

import java.util.List;

public class PokemonService {
    private final PokemonRepository pokemonRepository = new PokemonRepository();

    public void saveFirePokemon(
        final Trainer trainer,
        final String name,
        final int health,
        final int damage,
        final int fireResistance,
        final int firePower) {

        pokemonRepository.savePokemon(
            FirePokemon.builder()
                .name(name)
                .health(health)
                .damage(damage)
                .fireResistance(Math.max(0, fireResistance))
                .firePower(Math.max(0, firePower))
                .trainer(trainer)
                .build());
    }

    public void saveWaterPokemon(
        final Trainer trainer,
        final String name,
        final int health,
        final int damage,
        final int waterResistance,
        final int waterPower) {

        pokemonRepository.savePokemon(
            WaterPokemon.builder()
                .name(name)
                .health(health)
                .damage(damage)
                .waterResistance(Math.max(0, waterResistance))
                .waterPower(Math.max(0, waterPower))
                .trainer(trainer)
                .build());
    }


    public List<Pokemon> getPokemonsByTrainer(final @NonNull Trainer trainer) {
        return pokemonRepository.getPokemonsByTrainer(trainer);
    }

    public void updatePokemon(final @NonNull Pokemon pokemon) {
        pokemonRepository.updatePokemon(pokemon);
    }

    public void deletePokemon(final @NonNull Pokemon pokemon) {
        pokemonRepository.deletePokemon(pokemon);
    }
}
