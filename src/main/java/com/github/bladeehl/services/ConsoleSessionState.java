package com.github.bladeehl.services;

import com.github.bladeehl.model.Pokemon;
import com.github.bladeehl.model.Trainer;
import lombok.Data;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;

@Component
@SessionScope
@Data
public class ConsoleSessionState implements Serializable {
    @NonNull String state = "trainerMenu";
    @NonNull String inputType = "trainerMenuChoice";
    @Nullable Trainer trainer;
    @Nullable Pokemon firstPokemon;
    @Nullable Pokemon secondPokemon;
    @Nullable String pokemonName;
    @Nullable Integer pokemonType;
    @Nullable Integer pokemonHP;
    @Nullable Integer pokemonDamage;
    @Nullable Integer fireRes;
    @Nullable Integer firePwr;
    @Nullable Integer waterRes;
    @Nullable Integer waterPwr;
    @Nullable Pokemon selectedPokemon;
    int currentPage = 0;
}
