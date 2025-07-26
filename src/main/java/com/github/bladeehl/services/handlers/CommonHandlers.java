package com.github.bladeehl.services.handlers;

import com.github.bladeehl.services.ConsoleSessionState;
import com.github.bladeehl.io.TrainerWebIO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import static com.github.bladeehl.io.TrainerWebIO.*;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class CommonHandlers {
    public void returnToTrainerActions(
        @NonNull final StringBuilder output,
        @NonNull final ConsoleSessionState sessionState) {

        sessionState.setState("trainerActions");
        output.append(TrainerWebIO.getTrainerActions(sessionState.getTrainer()));
        sessionState.setInputType("trainerActionChoice");
    }

    public void returnToTrainerMenu(
        @NonNull final StringBuilder output,
        @NonNull final ConsoleSessionState sessionState) {

        sessionState.setState("trainerMenu");
        output.append(TRAINER_MENU_PROMT);
        sessionState.setInputType("trainerMenuChoice");
    }
}
