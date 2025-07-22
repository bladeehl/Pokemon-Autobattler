package com.github.bladeehl.repositories;

import com.github.bladeehl.model.ConsoleHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ConsoleHistoryRepository extends JpaRepository<ConsoleHistory, Long> {
    List<ConsoleHistory> findAllByOrderByIdAsc();

    List<ConsoleHistory> findByIdGreaterThanOrderByIdAsc(Long id);
}
