package com.github.bladeehl.services;

import com.github.bladeehl.exceptions.TrainerNotFoundException;
import com.github.bladeehl.model.Trainer;
import com.github.bladeehl.repositories.TrainerRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerService {
    final TrainerRepository trainerRepository;

    @Transactional
    public Trainer createTrainer(final @NonNull String name) {
        val trainer = Trainer.builder()
            .name(name)
            .build();

        trainerRepository.save(trainer);
        return trainer;
    }

    @Transactional(readOnly = true)
    public Trainer getTrainerByIndex(int index) {
        val trainers = trainerRepository.findAll();

        if (index < 1 || index > trainers.size()) {
            throw new TrainerNotFoundException("Некорректный индекс тренера: " + index);
        }

        return trainers.get(index - 1);
    }

    @Transactional(readOnly = true)
    public List<Trainer> getAll() {
        return trainerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Trainer getById(Long id) {
        return trainerRepository.findById(id)
            .orElseThrow(() -> new TrainerNotFoundException("Тренер с id " + id + " не найден"));
    }

    @Transactional
    public Trainer update(final @NonNull Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    @Transactional
    public void delete(Long id) {
        if (!trainerRepository.existsById(id)) {
            throw new TrainerNotFoundException("Невозможно удалить: тренер с id " + id + " не найден");
        }

        trainerRepository.deleteById(id);
    }
}
