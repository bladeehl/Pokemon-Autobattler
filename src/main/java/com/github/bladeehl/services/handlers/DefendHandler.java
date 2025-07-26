package com.github.bladeehl.services.handlers;

import com.github.bladeehl.io.BattleWebIO;

import com.github.bladeehl.services.BattleService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class DefendHandler {
    @NonNull BattleService battleService;

    public void handle(@NonNull final StringBuilder output) {
        battleService.defend(battleService.getCurrentPlayablePokemon());
        output.append(BattleWebIO.getBattleDefend(battleService.getCurrentPlayablePokemon()));
    }
}
