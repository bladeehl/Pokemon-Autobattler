package com.github.bladeehl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BattleAction {
    ATTACK(1),
    DEFEND(2),
    USE_ABILITY(3),
    SPECIAL_ATTACK(4),
    DEFENSIVE_ABILITY(5),
    EVOLVE(6);

    final int value;

    public static BattleAction fromValue(int value) {
        for (BattleAction action : values()) {
            if (action.value == value) {
                return action;
            }
        }

        throw new IllegalArgumentException("Некорректное действие: " + value);
    }
}
