package com.github.bladeehl.services;

import com.github.bladeehl.exceptions.TrainerNotFoundException;
import com.github.bladeehl.model.Trainer;
import com.github.bladeehl.repositories.TrainerRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerService {
    final TrainerRepository trainerRepository;

    public Trainer createTrainer(@NonNull final String name) {
        return trainerRepository.save(
            Trainer.builder()
            .name(name)
            .build());
    }

    public Trainer getTrainerByIndex(@NonNull final int index) {
        val pageSize = 10;
        val pageNumber = (index - 1) / pageSize;
        val positionInPage = (index - 1) % pageSize;
        val pageable = PageRequest.of(
            pageNumber,
            pageSize,
            Sort.by("id"));
        val page = trainerRepository.findAll(pageable);

        if (page.getTotalElements() < index || index < 0) {
            log.error("Тренер с индексом {} не найден, всего тренеров: {}", index, page.getTotalElements());
            throw new TrainerNotFoundException("Некорректный индекс тренера: %d".formatted(index));
        }

        return page.getContent().get(positionInPage);
    }

    public Page<Trainer> getAll(@NonNull final Pageable pageable) {
        return trainerRepository.findAll(pageable);
    }
}
