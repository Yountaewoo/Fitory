package io.github.yountaewoo.weightLog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WeightLogRepository extends JpaRepository<WeightLog, Long> {

    Optional<WeightLog> findByUserIdAndRecordDate(String userId, LocalDate recordDate);
}
