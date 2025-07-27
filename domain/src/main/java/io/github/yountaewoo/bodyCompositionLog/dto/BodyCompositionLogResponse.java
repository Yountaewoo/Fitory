package io.github.yountaewoo.bodyCompositionLog.dto;

import java.time.LocalDate;

public record BodyCompositionLogResponse(
        Long id,
        String userId,
        double weight,
        double bodyFatMass,
        double skeletalMuscleMass,
        LocalDate recordDate
) {
}
