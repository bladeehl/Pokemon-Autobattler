package com.github.bladeehl.services.handlers;

import com.github.bladeehl.io.BattleWebIO;
import com.github.bladeehl.services.BattleService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class AttackHandler {
    @NonNull BattleService battleService;

    public void handle(@NonNull final StringBuilder output) {
        val dmg = battleService.attack(
            battleService.getCurrentPlayablePokemon(),
            battleService.getCurrentOpponentPokemon());

        output.append(BattleWebIO.getBattleAttack(
            battleService.getCurrentPlayablePokemon(),
            battleService.getCurrentOpponentPokemon(),
            dmg));
    }
}
