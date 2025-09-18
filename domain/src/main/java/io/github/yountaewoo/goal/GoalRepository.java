package io.github.yountaewoo.goal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    boolean existsByUserId(String userId);

    Optional<Goal> findByUserId(String userId);

    List<Goal> findByEndDateBefore(LocalDate date);
}
