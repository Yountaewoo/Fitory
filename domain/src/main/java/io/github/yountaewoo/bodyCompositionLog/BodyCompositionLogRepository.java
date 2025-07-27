package io.github.yountaewoo.bodyCompositionLog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface BodyCompositionLogRepository extends JpaRepository<BodyCompositionLog, Long> {

    Optional<BodyCompositionLog> findByUserIdAndRecordDate(String userId, LocalDate recordDate);
}
