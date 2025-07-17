package com.github.bladeehl.repositories;

import com.github.bladeehl.services.DatabaseHelper;
import com.github.bladeehl.model.Pokemon;
import com.github.bladeehl.model.Trainer;
import lombok.val;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PokemonRepository {
    public List<Pokemon> getPokemonsByTrainer(final Trainer trainer) {
        return DatabaseHelper.returnInTransaction(session -> {
            val managedTrainer = session.merge(trainer);
            session.refresh(managedTrainer);

            return managedTrainer.getPokemons();
        });
    }

    public void savePokemon(final Pokemon pokemon) {
        DatabaseHelper.doInTransaction(session -> session.persist(pokemon));
    }

    public void updatePokemon(final Pokemon pokemon) {
        DatabaseHelper.doInTransaction(session -> session.merge(pokemon));
    }

    public void deletePokemon(final Pokemon pokemon) {
        DatabaseHelper.doInTransaction(session ->
            session.remove(session.merge(pokemon)));
    }
}
