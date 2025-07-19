package com.github.bladeehl.repositories;

import com.github.bladeehl.model.Pokemon;
import com.github.bladeehl.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
    List<Pokemon> findByTrainer(Trainer trainer);
}
