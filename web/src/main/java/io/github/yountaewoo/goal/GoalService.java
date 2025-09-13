package io.github.yountaewoo.goal;

import io.github.yountaewoo.goal.dto.GoalRequest;
import io.github.yountaewoo.goal.dto.GoalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoalService {

    private final GoalRepository goalRepository;

    private GoalResponse transferToGoalResponse(Goal goal) {
        return new GoalResponse(goal.getId(),
                goal.getUserId(),
                goal.getTargetBodyFatPercent(),
                goal.getTargetMuscleMes(),
                goal.getStartDate(),
                goal.getEndDate());
    }

    @Transactional
    public GoalResponse createGoal(GoalRequest goalRequest, String userId) {
        if (goalRepository.existsByUserId(userId)) {
            throw new IllegalStateException("목표가 이미 존재합니다.");
        }
        Goal goal = goalRepository.save(new Goal(userId,
                goalRequest.targetBodyFatPercent(),
                goalRequest.targetMuscleMes(),
                goalRequest.endDate()));
        return transferToGoalResponse(goal);
    }
}
