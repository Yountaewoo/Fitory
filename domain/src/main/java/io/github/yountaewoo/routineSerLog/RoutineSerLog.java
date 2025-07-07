package io.github.yountaewoo.routineSerLog;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoutineSerLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long routineLogId;

    private int setNumber;

    private int reps;

    private int restTime;

    private int weight;

    public RoutineSerLog(Long routineLogId, int setNumber, int reps, int restTime, int weight) {
        this.routineLogId = routineLogId;
        this.setNumber = setNumber;
        this.reps = reps;
        this.restTime = restTime;
        this.weight = weight;
    }
}
