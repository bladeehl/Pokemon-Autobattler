package com.github.bladeehl.api;

import com.github.bladeehl.exceptions.UnsupportedPokemonTypeException;
import com.github.bladeehl.model.*;
import com.github.bladeehl.services.PokemonService;
import com.github.bladeehl.services.TrainerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pokemons")
@RequiredArgsConstructor
public class PokemonController {
    final PokemonService pokemonService;
    final TrainerService trainerService;

    @GetMapping("/{id}")
    public ResponseEntity<Pokemon> getById(
            final @NonNull @PathVariable Long id) {
        val pokemon = pokemonService.getById(id);

        return ResponseEntity.ok(pokemon);
    }

    @GetMapping("/by-trainer/{trainerId}")
    public ResponseEntity<List<Pokemon>> getByTrainer(
            final @NonNull @PathVariable Long trainerId) {
        val trainer = trainerService.getById(trainerId);
        val pokemons = pokemonService.getByTrainer(trainer);

        return ResponseEntity.ok(pokemons);
    }

    @PostMapping("/fire")
    public ResponseEntity<Pokemon> createFirePokemon(
        final @NonNull @RequestParam Long trainerId,
        final @NonNull @RequestParam String name,
        final @RequestParam int health,
        final @RequestParam int damage,
        final @RequestParam int fireResistance,
        final @RequestParam int firePower) {

        val trainer = trainerService.getById(trainerId);
        val pokemon = pokemonService.saveFirePokemon(
            trainer,
            name,
            health,
            damage,
            fireResistance,
            firePower);

        return ResponseEntity.ok(pokemon);
    }

    @PostMapping("/water")
    public ResponseEntity<Pokemon> createWaterPokemon(
        final @NonNull @RequestParam Long trainerId,
        final @NonNull @RequestParam String name,
        final @RequestParam int health,
        final @RequestParam int damage,
        final @RequestParam int waterResistance,
        final @RequestParam int waterPower) {

        val trainer = trainerService.getById(trainerId);
        val pokemon = pokemonService.saveWaterPokemon(
            trainer,
            name,
            health,
            damage,
            waterResistance,
            waterPower);

        return ResponseEntity.ok(pokemon);
    }

    @PutMapping("/fire/{id}")
    public ResponseEntity<Pokemon> updateFirePokemon(
            final @NonNull @PathVariable Long id,
            final @NonNull @RequestParam String name,
            final @RequestParam int health,
            final @RequestParam int damage,
            final @RequestParam int fireResistance,
            final @RequestParam int firePower) {
        val pokemon = pokemonService.getById(id);

        if (!(pokemon instanceof FirePokemon)) {
            throw new UnsupportedPokemonTypeException("Покемон не является FirePokemon");
        }

        val firePokemon = (FirePokemon) pokemon;

        firePokemon.setName(name);
        firePokemon.setHealth(health);
        firePokemon.setDamage(damage);
        firePokemon.setFireResistance(Math.max(0, fireResistance));
        firePokemon.setFirePower(Math.max(0, firePower));
        pokemonService.updatePokemon(firePokemon);

        return ResponseEntity.ok(firePokemon);
    }

    @PutMapping("/water/{id}")
    public ResponseEntity<Pokemon> updateWaterPokemon(
            final @NonNull @PathVariable Long id,
            final @NonNull @RequestParam String name,
            final @RequestParam int health,
            final @RequestParam int damage,
            final @RequestParam int waterResistance,
            final @RequestParam int waterPower) {
        val pokemon = pokemonService.getById(id);

        if (!(pokemon instanceof WaterPokemon)) {
            throw new UnsupportedPokemonTypeException("Покемон не является WaterPokemon");
        }

        val waterPokemon = (WaterPokemon) pokemon;

        waterPokemon.setName(name);
        waterPokemon.setHealth(health);
        waterPokemon.setDamage(damage);
        waterPokemon.setWaterResistance(Math.max(0, waterResistance));
        waterPokemon.setWaterPower(Math.max(0, waterPower));
        pokemonService.updatePokemon(waterPokemon);

        return ResponseEntity.ok(waterPokemon);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            final @NonNull @PathVariable Long id) {
        pokemonService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
