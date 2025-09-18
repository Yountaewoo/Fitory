package goalServiceTest;

import io.github.yountaewoo.GoalStatus;
import io.github.yountaewoo.goal.Goal;
import io.github.yountaewoo.goal.GoalRepository;
import io.github.yountaewoo.goal.GoalService;
import io.github.yountaewoo.goalHistory.GoalHistory;
import io.github.yountaewoo.goalHistory.GoalHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
        import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceCompletedGoalTest {

    @Mock
    GoalRepository goalRepository;

    @Mock
    GoalHistoryRepository goalHistoryRepository;

    @InjectMocks
    GoalService goalService;

    @Test
    @DisplayName("Goal을 완료하면 히스토리 저장 후 Goal 삭제")
    void completeGoal_success() {
        // given
        Goal goal = new Goal("user1", 15.0, 34.0, LocalDate.now().plusDays(10));
        given(goalRepository.findByUserId("user1")).willReturn(Optional.of(goal));

        // when
        goalService.completedGoal("user1");

        // then
        then(goalHistoryRepository).should()
                .save(argThat(h -> h.getGoalStatus() == GoalStatus.COMPLETED));
        then(goalRepository).should().delete(goal);
    }

    @Test
    @DisplayName("존재하지 않는 Goal 완료 시 예외 발생")
    void completeGoal_notFound() {
        given(goalRepository.findByUserId("userX")).willReturn(Optional.empty());

        assertThatThrownBy(() -> goalService.completedGoal("userX"))
                .isInstanceOf(NoSuchElementException.class);
    }
}


