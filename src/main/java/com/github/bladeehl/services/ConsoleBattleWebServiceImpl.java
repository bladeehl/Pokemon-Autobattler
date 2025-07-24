package com.github.bladeehl.services;

import com.github.bladeehl.io.BattleWebIO;
import com.github.bladeehl.io.UtilWebIO;
import com.github.bladeehl.model.Pokemon;
import com.github.bladeehl.services.handlers.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
class ConsoleBattleWebServiceImpl implements ConsoleBattleWebService {
    @NonNull BattleService battleService;
    @NonNull ConsoleSessionState sessionState;
    @NonNull AttackHandler attackHandler;
    @NonNull DefendHandler defendHandler;
    @NonNull UseAbilityHandler useAbilityHandler;
    @NonNull SpecialAttackHandler specialAttackHandler;
    @NonNull DefensiveAbilityHandler defensiveAbilityHandler;
    @NonNull EvolveHandler evolveHandler;
    @NonNull CommonHandlers commonHandlers;

    public void startBattle(
        @NonNull final Pokemon firstPokemon,
        @NonNull final Pokemon secondPokemon) {

        battleService.startBattle(firstPokemon, secondPokemon);
    }

    public String getCurrentOutput() {
        val output = new StringBuilder();
        val firstPokemon = sessionState.getFirstPokemon();
        val secondPokemon = sessionState.getSecondPokemon();

        output.append(BattleWebIO.getBattleStart(firstPokemon, secondPokemon));
        output.append(BattleWebIO.getBattleTurn(battleService.getCurrentPlayablePokemon()));

        return output.toString();
    }

    public String processInput(@NonNull final String input) {
        val output = new StringBuilder();

        try {
            val choice = UtilWebIO.parseBattleAction(input);

            switch (choice) {
                case ATTACK -> attackHandler.handle(output);
                case DEFEND -> defendHandler.handle(output);
                case USE_ABILITY -> useAbilityHandler.handle(output);
                case SPECIAL_ATTACK -> specialAttackHandler.handle(output);
                case DEFENSIVE_ABILITY -> defensiveAbilityHandler.handle(output);
                case EVOLVE -> evolveHandler.handle(output);
                default -> output.append("Пропуск хода");
            }

            if (battleService.isBattleOver()) {
                val winner = battleService.getWinner();

                output.append(BattleWebIO.getBattleWinner(winner));
                commonHandlers.returnToTrainerActions(output, sessionState);

                return output.toString();
            }

            battleService.nextTurn();
            val firstPokemon = sessionState.getFirstPokemon();
            val secondPokemon = sessionState.getSecondPokemon();

            output.append(BattleWebIO.getBattleStatus(firstPokemon, secondPokemon));
            output.append(BattleWebIO.getBattleTurn(battleService.getCurrentPlayablePokemon()));
            sessionState.setInputType("battleChoice");
        } catch (Exception thrown) {
            log.error("Ошибка в бою: {}", thrown.getMessage());
            output.append(UtilWebIO.getErrorMessage(thrown.getMessage()));
        }

        return output.toString();
    }
}
