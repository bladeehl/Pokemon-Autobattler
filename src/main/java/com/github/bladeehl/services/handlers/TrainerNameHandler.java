package com.github.bladeehl.services.handlers;

import com.github.bladeehl.io.TrainerWebIO;
import com.github.bladeehl.services.TrainerService;
import com.github.bladeehl.services.ConsoleSessionState;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class TrainerNameHandler {
    @NonNull TrainerService trainerService;
    @NonNull ConsoleSessionState sessionState;

    public void handle(
        @NonNull final String input,
        @NonNull final StringBuilder output) {

        val trainer = trainerService.createTrainer(input);

        sessionState.setTrainer(trainer);
        sessionState.setState("trainerActions");
        output.append("Тренер создан.");
        output.append(TrainerWebIO.getTrainerActions(trainer));
        sessionState.setInputType("trainerActionChoice");
    }
}
