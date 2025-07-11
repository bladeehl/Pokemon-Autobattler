package com.github.bladeehl.services;

import com.github.bladeehl.model.Trainer;
import com.github.bladeehl.repositories.TrainerRepository;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.List;

@Slf4j
@UtilityClass
public class TrainerService {

    private final TrainerRepository trainerRepository = new TrainerRepository();

    public Trainer createTrainer(final String name) {
        val trainer = Trainer.builder().name(name).build();
        trainerRepository.saveTrainer(trainer);
        return trainer;
    }

    public List<Trainer> getAllTrainers() {
        return trainerRepository.getAllTrainers();
    }

    public Trainer getTrainerByIndex(List<Trainer> trainers, int index) {
        if (index < 1 || index > trainers.size()) {
            log.warn("Некорректный индекс тренера: {}", index);
            return null;
        }
        return trainers.get(index - 1);
    }
}
