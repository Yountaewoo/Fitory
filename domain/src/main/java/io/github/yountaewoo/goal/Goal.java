package io.github.yountaewoo.goal;

import io.github.yountaewoo.goal.dto.GoalRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private double targetBodyFatPercent;

    private double targetMuscleMes;

    private LocalDate startDate;

    private LocalDate endDate;

    @PrePersist
    void onCreate() {
        if (this.startDate == null) {
            this.startDate = LocalDate.now();
        }
    }

    public Goal(String userId, double targetBodyFatPercent, double targetMuscleMes, LocalDate endDate) {
        this.userId = userId;
        this.targetBodyFatPercent = targetBodyFatPercent;
        this.targetMuscleMes = targetMuscleMes;
        this.endDate = endDate;
    }

    public void updateGoal(GoalRequest goalRequest) {
        this.targetBodyFatPercent = goalRequest.targetBodyFatPercent();
        this.targetMuscleMes = goalRequest.targetMuscleMes();
        this.endDate = goalRequest.endDate();
    }
}
