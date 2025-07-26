package com.github.bladeehl.services.handlers;

import com.github.bladeehl.services.BattleService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import static com.github.bladeehl.io.BattleWebIO.*;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class EvolveHandler {
    @NonNull
    BattleService battleService;

    public void handle(@NonNull final StringBuilder output) {
        battleService.evolve(battleService.getCurrentPlayablePokemon());
        output.append(BATTLE_EVOLVE);
    }
}
