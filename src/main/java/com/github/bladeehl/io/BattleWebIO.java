package com.github.bladeehl.io;

import com.github.bladeehl.model.Pokemon;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BattleWebIO {
    public String getBattleStart(
            @NonNull final Pokemon firstPokemon,
            @NonNull final Pokemon secondPokemon) {
        return """
                ⚔️ Битва начинается!
                %s VS %s
                ⚔️⚔️⚔️⚔️⚔️⚔️⚔️⚔️⚔️⚔️⚔️
                
                """
                .formatted(firstPokemon.getName(),
                        secondPokemon.getName());
    }

    public String getBattleTurn(@NonNull final Pokemon currentPokemon) {
        return """
                🎮 Ход: %s
                1. Атаковать
                2. Защититься
                3. Способность
                4. Спец. атака
                5. Защитная способность
                6. Эволюция
                Выбор: """
                .formatted(currentPokemon.getName());
    }

    public String getBattleAttack(
            @NonNull final Pokemon attacker,
            @NonNull final Pokemon target,
            int damage) {
        return "💥 %s атаковал %s на %d урона%n"
                .formatted(attacker.getName(),
                        target.getName(),
                        damage);
    }

    public String getBattleDefend(@NonNull final Pokemon pokemon) {
        return "🛡️ %s активировал защиту%n".formatted(pokemon.getName());
    }

    public String getBattleAbility(
            @NonNull final Pokemon pokemon,
            final int gain) {
        return "✨ %s использовал способность (+%d HP)%n".formatted(pokemon.getName(),
                gain);
    }

    public String getBattleSpecialAttack(int damage) {
        return "🔥 Спец. атака нанесла %d урона%n".formatted(damage);
    }

    public String getBattleDefensiveAbility() {
        return "🛡️ Защитная способность активирована.";
    }

    public String getBattleEvolve() {
        return "🆙 Эволюция завершена!";
    }

    public String getBattleStatus(
            @NonNull final Pokemon firstPokemon,
            @NonNull final Pokemon secondPokemon) {
        return "📊 %s (HP: %d) vs %s (HP: %d)%n%n".formatted(
                firstPokemon.getName(),
                firstPokemon.getHealth(),
                secondPokemon.getName(),
                secondPokemon.getHealth());
    }

    public String getBattleWinner(@NonNull final Pokemon winner) {
        return "Победитель: %s!%n".formatted(winner.getName());
    }
}
