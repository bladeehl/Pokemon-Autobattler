package com.github.bladeehl.services;

import com.github.bladeehl.model.Trainer;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TrainerService {
    Trainer createTrainer(@NonNull final String name);

    Trainer getTrainerByIndex(final int index);

    Page<Trainer> getAll(@NonNull final Pageable pageable);
}
