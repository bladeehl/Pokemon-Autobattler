package com.github.bladeehl.services;

import com.github.bladeehl.api.BattleController.BattleStatus;
import com.github.bladeehl.model.Pokemon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BattleStatusFactory {
    final BattleService battleService;

    public BattleStatus createStatus(Pokemon playablePokemon, String actionResult) {
        return new BattleStatus(
            battleService.getCurrentPlayablePokemon().getName(),
            battleService.isFirstPlayersTurn(),
            battleService.isBattleOver(),
            battleService.isBattleOver() ? battleService.getWinner().getName() : null,
            actionResult
        );
    }

    public BattleStatus createStatusForStart() {
        return new BattleStatus(
            battleService.getCurrentPlayablePokemon().getName(),
            battleService.isFirstPlayersTurn(),
            battleService.isBattleOver(),
            null,
            null
        );
    }

    public BattleStatus createStatusForGet() {
        return new BattleStatus(
            battleService.getCurrentPlayablePokemon().getName(),
            battleService.isFirstPlayersTurn(),
            battleService.isBattleOver(),
            battleService.isBattleOver() ? battleService.getWinner().getName() : null,
            null
        );
    }
}
