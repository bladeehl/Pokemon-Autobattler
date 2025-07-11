package com.github.bladeehl.repositories;

import com.github.bladeehl.services.DatabaseHelper;
import com.github.bladeehl.model.Trainer;

import java.util.List;

public class TrainerRepository {

    public List<Trainer> getAllTrainers() {
        return DatabaseHelper.returnInTransaction(session ->
            session.createQuery("from Trainer", Trainer.class)
                .list());
    }

    public void saveTrainer(final Trainer trainer) {
        DatabaseHelper.doInTransaction(session -> session.persist(trainer));
    }

}
