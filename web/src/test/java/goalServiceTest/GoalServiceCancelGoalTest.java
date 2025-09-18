package goalServiceTest;

import io.github.yountaewoo.GoalStatus;
import io.github.yountaewoo.goal.Goal;
import io.github.yountaewoo.goal.GoalRepository;
import io.github.yountaewoo.goal.GoalService;
import io.github.yountaewoo.goalHistory.GoalHistory;
import io.github.yountaewoo.goalHistory.GoalHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceCancelGoalTest {

    @Mock private GoalRepository goalRepository;
    @Mock private GoalHistoryRepository goalHistoryRepository;

    @InjectMocks private GoalService goalService;

    @Nested
    @DisplayName("cancelGoal(userId)")
    class CancelGoal {

        @Test
        @DisplayName("정상: Goal → GoalHistory(CANCELLED) 저장 후 Goal 삭제")
        void success_cancel_and_archive() {
            // given
            String userId = "user-1";
            double bodyFat = 12.3;
            double muscle = 34.5;
            LocalDate start = LocalDate.of(2025, 7, 1);
            LocalDate plannedEnd = LocalDate.of(2025, 9, 30);

            // Goal 엔티티는 간단히 Mockito mock으로 스텁
            Goal goal = mock(Goal.class);
            given(goal.getUserId()).willReturn(userId);
            given(goal.getTargetBodyFatPercent()).willReturn(bodyFat);
            given(goal.getTargetMuscleMes()).willReturn(muscle);
            given(goal.getStartDate()).willReturn(start);
            given(goal.getEndDate()).willReturn(plannedEnd);

            given(goalRepository.findByUserId(userId)).willReturn(Optional.of(goal));
            // save()가 DB에서 반환하는 엔티티를 흉내: 넘긴 그대로 돌려주도록
            given(goalHistoryRepository.save(any(GoalHistory.class)))
                    .willAnswer(inv -> inv.getArgument(0));

            ArgumentCaptor<GoalHistory> captor = ArgumentCaptor.forClass(GoalHistory.class);

            // when
            goalService.cancelGoal(userId);

            // then
            verify(goalHistoryRepository).save(captor.capture());
            GoalHistory saved = captor.getValue();

            assertThat(saved.getUserId()).isEqualTo(userId);
            assertThat(saved.getTargetBodyFatPercent()).isEqualTo(bodyFat);
            assertThat(saved.getTargetMuscleMes()).isEqualTo(muscle);
            assertThat(saved.getStartDate()).isEqualTo(start);
            assertThat(saved.getPlannedEndDate()).isEqualTo(plannedEnd);
            assertThat(saved.getGoalStatus()).isEqualTo(GoalStatus.CANCELLED);

            // 단위테스트(모킹)에서는 @PrePersist가 동작하지 않으므로 archivedAt은 null일 수 있음
            // JPA 통합 테스트에서 별도 검증 (아래 2) 참고)
            assertThat(saved.getArchivedAt()).isNull();

            verify(goalRepository).delete(goal);
        }

        @Test
        @DisplayName("예외: 해당 userId의 Goal이 없으면 NoSuchElementException")
        void fail_when_goal_not_found() {
            // given
            String userId = "no-goal";
            given(goalRepository.findByUserId(userId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> goalService.cancelGoal(userId))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessageContaining("해당하는 목표가 없습니다.");

            verify(goalHistoryRepository, never()).save(any());
            verify(goalRepository, never()).delete(any());
        }
    }
}
