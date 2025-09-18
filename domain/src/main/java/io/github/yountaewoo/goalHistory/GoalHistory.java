package io.github.yountaewoo.goalHistory;

import io.github.yountaewoo.GoalStatus;
import jakarta.persistence.*;
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

    private LocalDate archivedAt;

    private LocalDate plannedEndDate;

    @Enumerated(EnumType.STRING)
    private GoalStatus goalStatus;

    @PrePersist
    void onCreate() {
        if (this.archivedAt == null) {
            this.archivedAt = LocalDate.now();
        }
    }

    public GoalHistory(String userId, double targetBodyFatPercent, double targetMuscleMes, LocalDate startDate, LocalDate plannedEndDate, GoalStatus goalStatus) {
        this.userId = userId;
        this.targetBodyFatPercent = targetBodyFatPercent;
        this.targetMuscleMes = targetMuscleMes;
        this.startDate = startDate;
        this.plannedEndDate = plannedEndDate;
        this.goalStatus = goalStatus;
    }
}
