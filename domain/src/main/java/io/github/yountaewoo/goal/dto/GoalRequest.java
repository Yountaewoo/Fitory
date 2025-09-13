package io.github.yountaewoo.goal.dto;

import java.time.LocalDate;

public record GoalRequest(
        double targetBodyFatPercent,
        double targetMuscleMes,
        LocalDate endDate
) {
}
