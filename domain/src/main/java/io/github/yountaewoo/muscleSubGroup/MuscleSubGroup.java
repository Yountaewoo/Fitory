package io.github.yountaewoo.muscleSubGroup;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MuscleSubGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long muscleGroupId;

    private String name;

    public MuscleSubGroup(Long muscleGroupId, String name) {
        this.muscleGroupId = muscleGroupId;
        this.name = name;
    }
}
