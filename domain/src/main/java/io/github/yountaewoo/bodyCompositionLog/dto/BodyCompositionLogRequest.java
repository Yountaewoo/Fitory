package io.github.yountaewoo.bodyCompositionLog.dto;

public record BodyCompositionLogRequest(
        double weight,
        double bodyFatMass,
        double skeletalMuscleMass
) {
}
