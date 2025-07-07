package io.github.yountaewoo.exercise;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long muscleGroupId;

    private Long muscleSubGroupId;

    public Exercise(String name, Long muscleGroupId, Long muscleSubGroupId) {
        this.name = name;
        this.muscleGroupId = muscleGroupId;
        this.muscleSubGroupId = muscleSubGroupId;
    }
}
