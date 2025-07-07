package io.github.yountaewoo.goal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private Long userId;

    private double targetBodyFatPercent;

    private double targetMuscleMes;

    private LocalDate startDate;

    private LocalDate endDate;

    public Goal(Long userId, double targetBodyFatPercent, double targetMuscleMes, LocalDate startDate, LocalDate endDate) {
        this.userId = userId;
        this.targetBodyFatPercent = targetBodyFatPercent;
        this.targetMuscleMes = targetMuscleMes;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
