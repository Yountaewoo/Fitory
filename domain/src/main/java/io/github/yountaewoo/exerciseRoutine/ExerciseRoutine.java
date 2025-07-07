package io.github.yountaewoo.exerciseRoutine;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ExerciseRoutine {

    @Id
    @GeneratedValue
    private Long id;

    private Long exerciseId;

    private Long routineId;

    private int set;

    private int reps;

    private int restTime;

    public ExerciseRoutine(Long exerciseId, Long routineId, int set, int reps, int restTime) {
        this.exerciseId = exerciseId;
        this.routineId = routineId;
        this.set = set;
        this.reps = reps;
        this.restTime = restTime;
    }
}
