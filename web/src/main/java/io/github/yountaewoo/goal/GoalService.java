package io.github.yountaewoo.goal;

import io.github.yountaewoo.GoalStatus;
import io.github.yountaewoo.goal.dto.GoalRequest;
import io.github.yountaewoo.goal.dto.GoalResponse;
import io.github.yountaewoo.goalHistory.GoalHistory;
import io.github.yountaewoo.goalHistory.GoalHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final GoalHistoryRepository goalHistoryRepository;

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

    @Transactional
    public void cancelGoal(String userId) {
        Goal goal = goalRepository.findByUserId(userId).orElseThrow(
                () -> new NoSuchElementException("해당하는 목표가 없습니다."));
        GoalHistory goalHistory = goalHistoryRepository.save(new GoalHistory(goal.getUserId(),
                goal.getTargetBodyFatPercent(),
                goal.getTargetMuscleMes(),
                goal.getStartDate(),
                goal.getEndDate(),
                GoalStatus.CANCELLED));
        goalRepository.delete(goal);
    }

    @Transactional
    public GoalResponse updateGoal(String userId, GoalRequest goalRequest) {
        Goal goal = goalRepository.findByUserId(userId).orElseThrow(
                () -> new NoSuchElementException("해당하는 목표가 없습니다."));
        goal.updateGoal(goalRequest);
        return transferToGoalResponse(goal);
    }

    @Transactional
    public void completedGoal(String userId) {
        Goal goal = goalRepository.findByUserId(userId).orElseThrow(
                () -> new NoSuchElementException("해당하는 목표가 없습니다."));
        GoalHistory goalHistory = goalHistoryRepository.save(new GoalHistory(goal.getUserId(),
                goal.getTargetBodyFatPercent(),
                goal.getTargetMuscleMes(),
                goal.getStartDate(),
                goal.getEndDate(),
                GoalStatus.COMPLETED));
        goalRepository.delete(goal);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void archiveExpiredGoals() {
        LocalDate today = LocalDate.now();
        List<Goal> goals = goalRepository.findByEndDateBefore(today);
        if (goals.isEmpty()) {
            log.info("[GoalScheduler] {} 만료된 목표 없음", today);
            return;
        }
        for (Goal goal : goals) {
            GoalHistory goalHistory = goalHistoryRepository.save(new GoalHistory(goal.getUserId(),
                    goal.getTargetBodyFatPercent(),
                    goal.getTargetMuscleMes(),
                    goal.getStartDate(),
                    goal.getEndDate(),
                    GoalStatus.EXPIRED));
            goalRepository.delete(goal);
        }
        log.info("[GoalScheduler] {} 만료된 목표 {}건 아카이브 완료", today, goals.size());
    }
}
