package com.github.bladeehl.services.handlers;

import com.github.bladeehl.io.TrainerWebIO;
import com.github.bladeehl.io.UtilWebIO;
import com.github.bladeehl.services.ConsoleSessionState;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Component;

import static com.github.bladeehl.io.TrainerWebIO.*;
import static com.github.bladeehl.io.UtilWebIO.*;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class TrainerMenuChoiceHandler {
    @NonNull ConsoleSessionState sessionState;
    @NonNull SelectTrainerHandler selectTrainerHandler;

    public void handle(
        @NonNull final String input,
        @NonNull final StringBuilder output) {

        val choice = UtilWebIO.parseInt(input, output).orElse(-1);

        switch (choice) {
            case 1 -> {
                sessionState.setState("createTrainer");
                sessionState.setInputType("trainerName");
                output.append(CREATE_TRAINER_PROMPT);
            }
            case 2 -> {
                sessionState.setState("selectTrainer");
                selectTrainerHandler.handle(output);
            }
            case 0 -> {
                output.append("Выход");
                sessionState.setState(null);
            }
            default -> output.append(INVALID_CHOICE);
        }
    }
}
