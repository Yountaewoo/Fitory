package io.github.yountaewoo.routineLog;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoutineLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long exerciseRoutineId;

    private LocalDate workOutDate;

    private String memo;

    public RoutineLog(Long exerciseRoutineId, LocalDate workOutDate, String memo) {
        this.exerciseRoutineId = exerciseRoutineId;
        this.workOutDate = workOutDate;
        this.memo = memo;
    }
}
