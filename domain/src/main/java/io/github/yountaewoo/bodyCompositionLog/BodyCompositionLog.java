package io.github.yountaewoo.bodyCompositionLog;

import io.github.yountaewoo.bodyCompositionLog.dto.BodyCompositionLogRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class BodyCompositionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private double weight;

    private double bodyFatMass;

    private double skeletalMuscleMass;

    private LocalDate recordDate;

    public BodyCompositionLog(String userId, double weight, double bodyFatMass, double skeletalMuscleMass, LocalDate recordDate) {
        this.userId = userId;
        this.weight = weight;
        this.bodyFatMass = bodyFatMass;
        this.skeletalMuscleMass = skeletalMuscleMass;
        this.recordDate = recordDate;
    }

    public void updateBodyCompositionLog(BodyCompositionLogRequest request) {
        this.weight = request.weight();
        this.bodyFatMass = request.bodyFatMass();
        this.skeletalMuscleMass = request.skeletalMuscleMass();
    }
}
