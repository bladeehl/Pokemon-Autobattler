package com.github.bladeehl.services;

import com.github.bladeehl.model.Pokemon;
import com.github.bladeehl.model.Trainer;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;

@Component
@SessionScope
@Data
public class ConsoleSessionState implements Serializable {
    String state = "trainerMenu";
    String inputType = "trainerMenuChoice";
    Trainer trainer;
    Pokemon firstPokemon;
    Pokemon secondPokemon;
    String pokemonName;
    Integer pokemonType;
    Integer pokemonHP;
    Integer pokemonDamage;
    Integer fireRes;
    Integer firePwr;
    Integer waterRes;
    Integer waterPwr;
    Pokemon selectedPokemon;
}
