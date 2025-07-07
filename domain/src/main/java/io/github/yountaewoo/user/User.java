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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double height;

    private String loginId;

    private String password;

    private Gender gender;

    public User(String name, double height, String loginId, String password, Gender gender) {
        this.name = name;
        this.height = height;
        this.loginId = loginId;
        this.password = password;
        this.gender = gender;
    }
}
