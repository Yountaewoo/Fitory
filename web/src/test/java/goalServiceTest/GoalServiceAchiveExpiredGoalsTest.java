package goalServiceTest;

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
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceAchiveExpiredGoalsTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private GoalHistoryRepository goalHistoryRepository;

    @InjectMocks
    private GoalService goalService;

    @Test
    @DisplayName("만료된 목표가 없으면 저장/삭제가 호출되지 않는다")
    void givenNoExpiredGoals_whenArchive_thenDoNothing() {
        // given
        given(goalRepository.findByEndDateBefore(any(LocalDate.class)))
                .willReturn(Collections.emptyList());

        // when
        goalService.archiveExpiredGoals();

        // then
        then(goalHistoryRepository).shouldHaveNoInteractions();
        then(goalRepository).should(never()).delete(any(Goal.class));
    }

    @Test
    @DisplayName("만료된 목표가 있으면 GoalHistory로 저장되고 Goal에서 삭제된다")
    void givenExpiredGoals_whenArchive_thenMoveToHistoryAndDelete() {
        // given
        Goal expiredGoal = new Goal("user1", 15.0, 35.0, LocalDate.now().minusDays(10));

        given(goalRepository.findByEndDateBefore(any(LocalDate.class)))
                .willReturn(List.of(expiredGoal));

        given(goalHistoryRepository.save(any(GoalHistory.class)))
                .willAnswer(invocation -> invocation.getArgument(0)); // 저장값 그대로 반환

        // when
        goalService.archiveExpiredGoals();

        // then
        then(goalHistoryRepository).should().save(any(GoalHistory.class));
        then(goalRepository).should().delete(expiredGoal);
    }
}
