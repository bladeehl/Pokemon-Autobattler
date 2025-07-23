package com.github.bladeehl.services.handlers;

import com.github.bladeehl.io.TrainerWebIO;
import com.github.bladeehl.services.TrainerService;
import com.github.bladeehl.services.ConsoleSessionState;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class SelectTrainerHandler {
    @NonNull TrainerService trainerService;
    @NonNull ConsoleSessionState sessionState;
    static int PAGE_SIZE = 10;

    public void handle(@NonNull final StringBuilder output) {
        val pageable = PageRequest.of(sessionState.getCurrentPage(), PAGE_SIZE, Sort.by("id"));
        val page = trainerService.getAll(pageable);

        if (page.getContent().isEmpty()) {
            output.append(TrainerWebIO.getNoTrainersMessage());
            sessionState.setState("trainerMenu");
            sessionState.setInputType("trainerMenuChoice");
            return;
        }

        output.append(String.format("Список тренеров (страница %d из %d):%n",
            sessionState.getCurrentPage() + 1, page.getTotalPages()));
        output.append(TrainerWebIO.getSelectTrainerPrompt(page.getContent()));
        output.append("Введите номер тренера, -1 для предыдущей страницы, 0 для следующей:");
        sessionState.setInputType("trainerIndex");
    }
}
