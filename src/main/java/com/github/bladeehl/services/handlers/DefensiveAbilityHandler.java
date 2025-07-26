package com.github.bladeehl.services.handlers;

import com.github.bladeehl.exceptions.UnsupportedPokemonTypeException;
import com.github.bladeehl.io.UtilWebIO;
import com.github.bladeehl.services.BattleService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import static com.github.bladeehl.io.BattleWebIO.*;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class DefensiveAbilityHandler {
    @NonNull
    BattleService battleService;

    public void handle(@NonNull final StringBuilder output) {
        try {
            battleService.defensiveAbility(battleService.getCurrentPlayablePokemon());
            output.append(BATTLE_DEFENSIVE_ABILITY);
        } catch (UnsupportedPokemonTypeException thrown) {
            output.append(UtilWebIO.getErrorMessage(thrown.getMessage()));
        }
    }
}
