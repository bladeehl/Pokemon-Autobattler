package com.github.bladeehl.api;

import com.github.bladeehl.exceptions.BattleEndedException;
import com.github.bladeehl.exceptions.BattleNotEndedException;
import com.github.bladeehl.exceptions.BattleNotStartedException;
import com.github.bladeehl.exceptions.EqualPokemonsException;
import com.github.bladeehl.model.Pokemon;
import com.github.bladeehl.model.FirePokemon;
import com.github.bladeehl.model.WaterPokemon;
import com.github.bladeehl.services.BattleService;
import com.github.bladeehl.services.BattleStatusFactory;
import com.github.bladeehl.services.PokemonService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/battles")
@RequiredArgsConstructor
public class BattleController {
    final BattleService battleService;
    final PokemonService pokemonService;
    final BattleStatusFactory battleStatusFactory;

    @PostMapping("/start")
    public ResponseEntity<BattleStatus> startBattle(
        final @NonNull @RequestParam Long firstPokemonId,
        final @NonNull @RequestParam Long secondPokemonId) {
        if (firstPokemonId.equals(secondPokemonId)) {
            throw new EqualPokemonsException("Покемоны должны быть разными");
        }

        val firstPokemon = pokemonService.getById(firstPokemonId);
        val secondPokemon = pokemonService.getById(secondPokemonId);

        battleService.startBattle(firstPokemon, secondPokemon);

        val status = battleStatusFactory.createStatusForStart();

        return ResponseEntity.ok(status);
    }

    @GetMapping("/status")
    public ResponseEntity<BattleStatus> getBattleStatus() {
        if (battleService.getFirstPokemon() == null
            || battleService.getSecondPokemon() == null) {
            throw new BattleNotStartedException("Бой не начат");
        }

        val status = battleStatusFactory.createStatusForGet();

        return ResponseEntity.ok(status);
    }

    @GetMapping("/stats")
    public ResponseEntity<BattleStats> getStats() {
        if (battleService.getFirstPokemon() == null || battleService.getSecondPokemon() == null) {
            throw new BattleNotStartedException("Бой не начат");
        }

        val firstPokemon = battleService.getFirstPokemon();
        val secondPokemon = battleService.getSecondPokemon();

        val firstPokemonStats = createPokemonStats(firstPokemon);
        val secondPokemonStats = createPokemonStats(secondPokemon);

        val stats = new BattleStats(firstPokemonStats, secondPokemonStats);

        return ResponseEntity.ok(stats);
    }

    @PostMapping("/attack")
    public ResponseEntity<BattleStatus> attack() {
        if (battleService.getFirstPokemon() == null || battleService.getSecondPokemon() == null) {
            throw new BattleNotStartedException("Бой не начат");
        }

        if (battleService.isBattleOver()) {
            throw new BattleEndedException("Бой уже закончен");
        }

        val playablePokemon = battleService.getCurrentPlayablePokemon();
        val opponentPokemon = battleService.getCurrentOpponentPokemon();
        val damage = battleService.attack(playablePokemon, opponentPokemon);

        battleService.nextTurn();
        val status = battleStatusFactory.createStatus(
            playablePokemon,
            playablePokemon.getName() + " нанёс " + damage + " урона " + opponentPokemon.getName()
        );

        return ResponseEntity.ok(status);
    }

    @PostMapping("/defend")
    public ResponseEntity<BattleStatus> defend() {
        if (battleService.getFirstPokemon() == null || battleService.getSecondPokemon() == null) {
            throw new BattleNotStartedException("Бой не начат");
        }

        if (battleService.isBattleOver()) {
            throw new BattleEndedException("Бой уже закончен");
        }

        val playablePokemon = battleService.getCurrentPlayablePokemon();

        battleService.defend(playablePokemon);
        battleService.nextTurn();
        val status = battleStatusFactory.createStatus(
            playablePokemon,
            playablePokemon.getName() + " применил защиту"
        );

        return ResponseEntity.ok(status);
    }

    @PostMapping("/ability")
    public ResponseEntity<BattleStatus> useAbility() {
        if (battleService.getFirstPokemon() == null || battleService.getSecondPokemon() == null) {
            throw new BattleNotStartedException("Бой не начат");
        }

        if (battleService.isBattleOver()) {
            throw new BattleEndedException("Бой уже закончен");
        }

        val playablePokemon = battleService.getCurrentPlayablePokemon();
        val result = battleService.useAbility(playablePokemon);

        battleService.nextTurn();
        val status = battleStatusFactory.createStatus(
            playablePokemon,
            playablePokemon.getName() + " использовал способность, результат: " + result
        );

        return ResponseEntity.ok(status);
    }

    @PostMapping("/special-attack")
    public ResponseEntity<BattleStatus> specialAttack() {
        if (battleService.getFirstPokemon() == null
            || battleService.getSecondPokemon() == null) {
            throw new BattleNotStartedException("Бой не начат");
        }

        if (battleService.isBattleOver()) {
            throw new BattleEndedException("Бой уже закончен");
        }

        val playablePokemon = battleService.getCurrentPlayablePokemon();
        val opponentPokemon = battleService.getCurrentOpponentPokemon();
        val damage = battleService.specialAttack(playablePokemon, opponentPokemon);

        battleService.nextTurn();
        val status = battleStatusFactory.createStatus(
            playablePokemon,
            playablePokemon.getName() + " нанёс " + damage + " урона специальной атакой " + opponentPokemon.getName()
        );

        return ResponseEntity.ok(status);
    }

    @PostMapping("/defensive-ability")
    public ResponseEntity<BattleStatus> defensiveAbility() {
        if (battleService.getFirstPokemon() == null || battleService.getSecondPokemon() == null) {
            throw new BattleNotStartedException("Бой не начат");
        }

        if (battleService.isBattleOver()) {
            throw new BattleEndedException("Бой уже закончен");
        }

        val playablePokemon = battleService.getCurrentPlayablePokemon();

        battleService.defensiveAbility(playablePokemon);
        battleService.nextTurn();
        val status = battleStatusFactory.createStatus(
            playablePokemon,
            playablePokemon.getName() + " применил защитную способность"
        );

        return ResponseEntity.ok(status);
    }

    @PostMapping("/evolve")
    public ResponseEntity<BattleStatus> evolve() {
        if (battleService.getFirstPokemon() == null
            || battleService.getSecondPokemon() == null) {
            throw new BattleNotStartedException("Бой не начат");
        }

        if (battleService.isBattleOver()) {
            throw new BattleEndedException("Бой уже закончен");
        }

        val playablePokemon = battleService.getCurrentPlayablePokemon();

        battleService.evolve(playablePokemon);
        battleService.nextTurn();
        val status = battleStatusFactory.createStatus(
            playablePokemon,
            playablePokemon.getName() + " эволюционировал"
        );

        return ResponseEntity.ok(status);
    }

    @GetMapping("/winner")
    public ResponseEntity<Pokemon> getWinner() {
        if (battleService.getFirstPokemon() == null
            || battleService.getSecondPokemon() == null) {
            throw new BattleNotStartedException("Бой не начат");
        }

        if (!battleService.isBattleOver()) {
            throw new BattleNotEndedException("Бой ещё не закончен");
        }

        val winner = battleService.getWinner();

        return ResponseEntity.ok(winner);
    }

    public record BattleStatus(
        String currentPokemonName,
        boolean isFirstPlayersTurn,
        boolean isBattleOver,
        String winnerName,
        String actionResult
    ) {}

    record BattleStats(
        PokemonStats firstPokemon,
        PokemonStats secondPokemon
    ) {}

    record PokemonStats(
        Long id,
        String name,
        int health,
        int damage,
        String type,
        Integer fireResistance,
        Integer firePower,
        Integer waterResistance,
        Integer waterPower
    ) {}

    private PokemonStats createPokemonStats(final @NonNull Pokemon pokemon) {
        val id = pokemon.getId();
        val name = pokemon.getName();
        val health = pokemon.getHealth();
        val damage = pokemon.getDamage();
        val type = pokemon instanceof FirePokemon
            ? "Fire"
            : "Water";
        val fireResistance = pokemon instanceof FirePokemon
            ? ((FirePokemon) pokemon).getFireResistance()
            : null;
        val firePower = pokemon instanceof FirePokemon
            ? ((FirePokemon) pokemon).getFirePower()
            : null;
        val waterResistance = pokemon instanceof WaterPokemon
            ? ((WaterPokemon) pokemon).getWaterResistance()
            : null;
        val waterPower = pokemon instanceof WaterPokemon
            ? ((WaterPokemon) pokemon).getWaterPower()
            : null;

        return new PokemonStats(
            id,
            name,
            health,
            damage,
            type,
            fireResistance,
            firePower,
            waterResistance,
            waterPower
        );
    }
}
