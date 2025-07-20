package io.github.yountaewoo.weightLog.dto;

import io.github.yountaewoo.user.User;

import java.time.LocalDate;

public record WeightLogResponse(
        String userId,
        double weight,
        LocalDate recordDate
) {
}
