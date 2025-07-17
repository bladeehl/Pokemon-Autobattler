package com.github.bladeehl.services;

import com.github.bladeehl.model.*;
import com.github.bladeehl.repositories.PokemonRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PokemonService {
    private final PokemonRepository pokemonRepository;

    public void saveFirePokemon(
        final @NonNull Trainer trainer,
        final @NonNull String name,
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
        final @NonNull Trainer trainer,
        final @NonNull String name,
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
