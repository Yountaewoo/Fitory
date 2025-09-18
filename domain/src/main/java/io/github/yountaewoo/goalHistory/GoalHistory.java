package io.github.yountaewoo.goalHistory;

import io.github.yountaewoo.GoalStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Entity
public class GoalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private double targetBodyFatPercent;

    private double targetMuscleMes;

    private LocalDate startDate;

    private LocalDate endDate;

    private GoalStatus goalStatus;

    public GoalHistory(String userId, double targetBodyFatPercent, double targetMuscleMes, LocalDate startDate, LocalDate endDate, GoalStatus goalStatus) {
        this.userId = userId;
        this.targetBodyFatPercent = targetBodyFatPercent;
        this.targetMuscleMes = targetMuscleMes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.goalStatus = goalStatus;
    }
}
