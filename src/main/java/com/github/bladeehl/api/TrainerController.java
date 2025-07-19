package com.github.bladeehl.api;

import com.github.bladeehl.model.Trainer;
import com.github.bladeehl.services.TrainerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class TrainerController {
    final TrainerService trainerService;

    @GetMapping
    public ResponseEntity<List<Trainer>> getAll() {
        return ResponseEntity.ok(trainerService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trainer> getById(
            final @NonNull @PathVariable Long id) {

        return ResponseEntity.ok(trainerService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Trainer> create(
        final @NonNull @RequestParam String name) {

        return ResponseEntity.ok(trainerService.createTrainer(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trainer> updateTrainer(
            final @NonNull @PathVariable Long id,
            final @NonNull @RequestParam String name) {
        val trainer = trainerService.getById(id);

        trainer.setName(name);
        trainerService.update(trainer);

        return ResponseEntity.ok(trainer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            final @NonNull @PathVariable Long id) {
        trainerService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
