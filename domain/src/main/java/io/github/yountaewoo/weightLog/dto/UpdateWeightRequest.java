package io.github.yountaewoo.weightLog.dto;

import java.time.LocalDate;

public record UpdateWeightRequest(
        LocalDate recordDate,
        double weight
) {
}
