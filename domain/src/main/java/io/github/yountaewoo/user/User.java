package io.github.yountaewoo.user;

import io.github.yountaewoo.Gender;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User {

    @Id
    private String id;

    private String name;

    private double height;

    private Gender gender;

    private boolean withdrawn;

    public User(String id, String name, double height, Gender gender) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.gender = gender;
        this.withdrawn = false;
    }

    public void withdraw() {
        this.withdrawn = true;
    }
}
