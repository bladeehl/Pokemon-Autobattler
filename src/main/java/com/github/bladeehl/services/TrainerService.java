package com.github.bladeehl.services;

import com.github.bladeehl.model.Trainer;
import com.github.bladeehl.repositories.TrainerRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.List;

@Slf4j
public class TrainerService {
    private static final TrainerRepository trainerRepository = new TrainerRepository();
    public static Trainer createTrainer(final String name) {
        val trainer = Trainer.builder()
            .name(name)
            .build();
        trainerRepository.saveTrainer(trainer);

        return trainer;
    }

    public static List<Trainer> getAllTrainers() {
        return trainerRepository.getAllTrainers();
    }

    public static Trainer getTrainerByIndex(int index) {
        val trainers = trainerRepository.getAllTrainers();
        if (index < 1 || index > trainers.size()) {
            throw new IllegalArgumentException("Некорректный индекс тренера: " + index);
        }

        return trainers.get(index - 1);
    }
}
