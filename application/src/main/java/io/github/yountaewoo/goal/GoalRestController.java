package io.github.yountaewoo.goal;

import io.github.yountaewoo.goal.dto.GoalRequest;
import io.github.yountaewoo.goal.dto.GoalResponse;
import io.github.yountaewoo.loginUtils.LoginMemberId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class GoalRestController {

    private final GoalService goalService;

    @PostMapping
    public GoalResponse createGoal(@RequestBody GoalRequest goalRequest, @LoginMemberId String userId) {
        return goalService.createGoal(goalRequest, userId);
    }
}
