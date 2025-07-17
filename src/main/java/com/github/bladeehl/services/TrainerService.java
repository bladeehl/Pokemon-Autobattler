package com.github.bladeehl.services;

import com.github.bladeehl.exceptions.TrainerNotFoundException;
import com.github.bladeehl.model.Trainer;
import com.github.bladeehl.repositories.TrainerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class TrainerService {
    private final TrainerRepository trainerRepository;

    public Trainer createTrainer(final String name) {
        val trainer = Trainer.builder()
            .name(name)
            .build();
        trainerRepository.saveTrainer(trainer);

        return trainer;
    }

    public List<Trainer> getAllTrainers() {
        return trainerRepository.getAllTrainers();
    }

    public Trainer getTrainerByIndex(int index) {
        val trainers = trainerRepository.getAllTrainers();

        if (index < 1 || index > trainers.size()) {
            throw new TrainerNotFoundException("Некорректный индекс тренера: " + index);
        }

        return trainers.get(index - 1);
    }
}
