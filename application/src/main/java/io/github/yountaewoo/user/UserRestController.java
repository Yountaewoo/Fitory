package io.github.yountaewoo.user;

import io.github.yountaewoo.loginUtils.LoginMemberId;
import io.github.yountaewoo.user.dto.HeightRequest;
import io.github.yountaewoo.user.dto.UserRequest;
import io.github.yountaewoo.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
public class UserRestController {

    private final UserService userService;

    @PostMapping
    public UserResponse createOrGet(@LoginMemberId String userId, @RequestBody UserRequest userRequest) {
        return userService.getOrCreateUser(userId, userRequest);
    }

    @PatchMapping
    public void withdrawMember(@LoginMemberId String userId) {
        userService.withdrawMember(userId);
    }

    @PatchMapping
    public void updateHeight(@LoginMemberId String userId, @RequestBody HeightRequest request) {
        userService.updateHeight(userId, request);
    }
}
