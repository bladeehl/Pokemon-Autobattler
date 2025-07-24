package com.github.bladeehl.services;

import com.github.bladeehl.model.FirePokemon;
import com.github.bladeehl.model.Pokemon;
import com.github.bladeehl.model.Trainer;
import com.github.bladeehl.model.WaterPokemon;
import lombok.NonNull;

import java.util.List;

public interface PokemonService {
    FirePokemon saveFirePokemon(
        @NonNull final Trainer trainer,
        @NonNull final String name,
        final int health,
        final int damage,
        final int fireResistance,
        final int firePower);

    WaterPokemon saveWaterPokemon(
        @NonNull final Trainer trainer,
        @NonNull final String name,
        final int health,
        final int damage,
        final int waterResistance,
        final int waterPower);

    void updatePokemon(@NonNull final Pokemon pokemon);

    void deletePokemon(@NonNull final Pokemon pokemon);

    List<Pokemon> getByTrainer(@NonNull final Trainer trainer);
}
