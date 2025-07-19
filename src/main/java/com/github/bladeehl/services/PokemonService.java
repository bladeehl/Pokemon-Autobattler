package com.github.bladeehl.services;

import com.github.bladeehl.exceptions.PokemonNotFoundException;
import com.github.bladeehl.model.*;
import com.github.bladeehl.repositories.PokemonRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PokemonService {
    final PokemonRepository pokemonRepository;

    @Transactional
    public FirePokemon saveFirePokemon(
        final @NonNull Trainer trainer,
        final @NonNull String name,
        final int health,
        final int damage,
        final int fireResistance,
        final int firePower) {

        val pokemon = FirePokemon.builder()
            .name(name)
            .health(health)
            .damage(damage)
            .fireResistance(Math.max(0, fireResistance))
            .firePower(Math.max(0, firePower))
            .trainer(trainer)
            .build();

        return pokemonRepository.save(pokemon);
    }

    @Transactional
    public WaterPokemon saveWaterPokemon(
        final @NonNull Trainer trainer,
        final @NonNull String name,
        final int health,
        final int damage,
        final int waterResistance,
        final int waterPower) {

        val pokemon = WaterPokemon.builder()
            .name(name)
            .health(health)
            .damage(damage)
            .waterResistance(Math.max(0, waterResistance))
            .waterPower(Math.max(0, waterPower))
            .trainer(trainer)
            .build();

        return pokemonRepository.save(pokemon);
    }

    @Transactional
    public void updatePokemon(final @NonNull Pokemon pokemon) {
        pokemonRepository.save(pokemon);
    }

    @Transactional
    public void deletePokemon(final @NonNull Pokemon pokemon) {
        pokemonRepository.delete(pokemon);
    }

    @Transactional(readOnly = true)
    public Pokemon getById(Long id) {
        return pokemonRepository.findById(id)
            .orElseThrow(() -> new PokemonNotFoundException("Покемон с id " + id + " не найден"));
    }

    @Transactional(readOnly = true)
    public List<Pokemon> getByTrainer(final @NonNull Trainer trainer) {
        return pokemonRepository.findByTrainer(trainer);
    }

    @Transactional
    public void delete(Long id) {
        if (!pokemonRepository.existsById(id)) {
            throw new PokemonNotFoundException("Покемон с id " + id + " не найден для удаления");
        }

        pokemonRepository.deleteById(id);
    }
}
