package com.github.bladeehl.services.handlers;

import com.github.bladeehl.exceptions.UnsupportedPokemonTypeException;
import com.github.bladeehl.io.BattleWebIO;
import com.github.bladeehl.io.UtilWebIO;
import com.github.bladeehl.services.BattleService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class DefensiveAbilityHandler {
    @NonNull BattleService battleService;

    public void handle(@NonNull final StringBuilder output) {
        try {
            battleService.defensiveAbility(battleService.getCurrentPlayablePokemon());
            output.append(BattleWebIO.getBattleDefensiveAbility());
        } catch (UnsupportedPokemonTypeException thrown) {
            output.append(UtilWebIO.getErrorMessage(thrown.getMessage()));
        }
    }
}
