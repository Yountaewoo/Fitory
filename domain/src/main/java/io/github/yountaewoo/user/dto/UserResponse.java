package io.github.yountaewoo.user.dto;

import io.github.yountaewoo.Gender;

public record UserResponse(
        String userId,
        String userName,
        double height,
        Gender gender
) {
}
