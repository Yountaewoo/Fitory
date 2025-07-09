package io.github.yountaewoo.user.dto;

import io.github.yountaewoo.Gender;

public record UserRequest(
        String name,
        double height,
        Gender gender
) {
}
