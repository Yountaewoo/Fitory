package io.github.yountaewoo.routine;

import io.github.yountaewoo.DayOfWeek;
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
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String name;

    private DayOfWeek dayOfWeek;

    public Routine(Long userId, String name, DayOfWeek dayOfWeek) {
        this.userId = userId;
        this.name = name;
        this.dayOfWeek = dayOfWeek;
    }
}
