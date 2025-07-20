package io.github.yountaewoo.weightLog;

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
public class WeightLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private double weight;

    private LocalDate recordDate;

    public WeightLog(String userId, double weight, LocalDate recordDate) {
        this.userId = userId;
        this.weight = weight;
        this.recordDate = recordDate;
    }
}
