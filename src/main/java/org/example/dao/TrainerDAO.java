package org.example.dao;

import org.example.service.DatabaseHelper;
import org.example.models.Trainer;

import java.util.List;

public class TrainerDAO {
    public static List<Trainer> getAllTrainers() {
        return DatabaseHelper.returnInTransaction(session ->
            session.createQuery("from Trainer", Trainer.class).list());
    }

    public static void saveTrainer(Trainer trainer) {
        DatabaseHelper.doInTransaction(session -> session.persist(trainer));
    }

    public static Trainer getTrainerById(Long id) {
        return DatabaseHelper.returnInTransaction(session ->
            session.get(Trainer.class, id));
    }

    public static void updateTrainer(Trainer trainer) {
        DatabaseHelper.doInTransaction(session -> session.merge(trainer));
    }

    public static void deleteTrainer(Trainer trainer) {
        DatabaseHelper.doInTransaction(session -> {
            Trainer managed = session.merge(trainer);
            session.remove(managed);
        });
    }
}
