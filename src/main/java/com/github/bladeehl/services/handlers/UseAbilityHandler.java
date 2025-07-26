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
public class UseAbilityHandler {
    @NonNull BattleService battleService;

    public void handle(@NonNull final StringBuilder output) {
        val gain = battleService.useAbility(battleService.getCurrentPlayablePokemon());

        output.append(BattleWebIO.getBattleAbility(battleService.getCurrentPlayablePokemon(), gain));
    }
}
