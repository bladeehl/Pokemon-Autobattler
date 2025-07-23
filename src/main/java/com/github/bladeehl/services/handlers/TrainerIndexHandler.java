package com.github.bladeehl.services.handlers;

import com.github.bladeehl.exceptions.TrainerNotFoundException;
import com.github.bladeehl.io.TrainerWebIO;
import com.github.bladeehl.io.UtilWebIO;
import com.github.bladeehl.services.TrainerService;
import com.github.bladeehl.services.ConsoleSessionState;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@Slf4j
public class TrainerIndexHandler {
    @NonNull TrainerService trainerService;
    @NonNull ConsoleSessionState sessionState;
    @NonNull SelectTrainerHandler selectTrainerHandler;
    static int PAGE_SIZE = 10;

    public void handle(
        @NonNull final String input,
        @NonNull final StringBuilder output) {

        val pageable = PageRequest.of(sessionState.getCurrentPage(), PAGE_SIZE, Sort.by("id"));
        val page = trainerService.getAll(pageable);
        val index = UtilWebIO.parseInt(input, output);

        if (index == -1) {
            if (sessionState.getCurrentPage() <= 0) {
                output.append("Это первая страница.\n");
                selectTrainerHandler.handle(output);
                return;
            }
            sessionState.setCurrentPage(sessionState.getCurrentPage() - 1);
            selectTrainerHandler.handle(output);
            return;
        }

        if (index == 0) {
            if (sessionState.getCurrentPage() >= page.getTotalPages() - 1) {
                output.append("Это последняя страница.\n");
                selectTrainerHandler.handle(output);
                return;
            }
            sessionState.setCurrentPage(sessionState.getCurrentPage() + 1);
            selectTrainerHandler.handle(output);
            return;
        }

        try {
            val globalIndex = (sessionState.getCurrentPage() * PAGE_SIZE) + index;
            val trainer = trainerService.getTrainerByIndex(globalIndex);

            sessionState.setTrainer(trainer);
            sessionState.setState("trainerActions");
            output.append(TrainerWebIO.getTrainerActions(trainer));
            sessionState.setInputType("trainerActionChoice");
        } catch (TrainerNotFoundException thrown) {
            log.warn("Некорректный индекс тренера: {}", input);
            output.append(UtilWebIO.getInvalidChoiceMessage());
            selectTrainerHandler.handle(output);
        }
    }
}
