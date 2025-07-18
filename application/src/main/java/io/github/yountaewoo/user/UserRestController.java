package io.github.yountaewoo.user;

import io.github.yountaewoo.loginUtils.LoginMemberId;
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
    public UserResponse createOrGet(@LoginMemberId String id, @RequestBody UserRequest userRequest) {
        return userService.getOrCreateUser(id, userRequest);
    }

    @PatchMapping
    public void withdrawMember(@LoginMemberId String id) {
        userService.withdrawMember(id);
    }
}
