package io.github.yountaewoo.goal.dto;

import java.time.LocalDate;

public record GoalResponse(
        Long id,
        String userId,
        double targetBodyFatPercent,
        double targetMuscleMes,
        LocalDate startDate,
        LocalDate endDate
) {
}
