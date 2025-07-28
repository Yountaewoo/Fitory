package io.github.yountaewoo.bodyCompositionLog.dto;

public record BodyCompositionLogRequest(
        Double weight,
        Double bodyFatMass,
        Double skeletalMuscleMass
) {
}
