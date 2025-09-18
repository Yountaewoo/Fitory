package io.github.yountaewoo.goalHistory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalHistoryRepository extends JpaRepository<GoalHistory, Long> {
}
