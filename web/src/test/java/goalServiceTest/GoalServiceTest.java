package goalServiceTest;

import io.github.yountaewoo.goal.Goal;
import io.github.yountaewoo.goal.GoalRepository;
import io.github.yountaewoo.goal.GoalService;
import io.github.yountaewoo.goal.dto.GoalRequest;
import io.github.yountaewoo.goal.dto.GoalResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private GoalService goalService;

    @Test
    @DisplayName("사용자 목표가 없으면 새로 저장하고 GoalResponse로 반환한다")
    void createGoal_success() {
        // given
        String userId = "user-1";
        GoalRequest req = new GoalRequest(12.5, 35.2, LocalDate.of(2025, 12, 31));
        given(goalRepository.existsByUserId(userId)).willReturn(false);

        // save 스텁: 저장되는 엔티티에 id/startDate를 채워 반환
        willAnswer(invocation -> {
            Goal g = invocation.getArgument(0, Goal.class);
            if (g.getStartDate() == null) {
            }
            Goal saved = new Goal(g.getUserId(), g.getTargetBodyFatPercent(), g.getTargetMuscleMes(), g.getEndDate());
            return g; // 단순히 동일 객체 반환
        }).given(goalRepository).save(any(Goal.class));

        // when
        GoalResponse res = goalService.createGoal(req, userId);

        // then
        ArgumentCaptor<Goal> captor = ArgumentCaptor.forClass(Goal.class);
        verify(goalRepository).save(captor.capture());

        Goal saved = captor.getValue();
        assertThat(saved.getUserId()).isEqualTo(userId);
        assertThat(saved.getTargetBodyFatPercent()).isEqualTo(12.5);
        assertThat(saved.getTargetMuscleMes()).isEqualTo(35.2);
        assertThat(saved.getEndDate()).isEqualTo(LocalDate.of(2025, 12, 31));

        assertThat(res.userId()).isEqualTo(userId);
        assertThat(res.targetBodyFatPercent()).isEqualTo(12.5);
        assertThat(res.targetMuscleMes()).isEqualTo(35.2);
        assertThat(res.endDate()).isEqualTo(LocalDate.of(2025, 12, 31));
    }

    @Test
    @DisplayName("이미 사용자 목표가 존재하면 예외를 던진다")
    void createGoal_duplicate() {
        String userId = "user-1";
        GoalRequest req = new GoalRequest(10.0, 34.0, LocalDate.of(2025, 11, 30));
        given(goalRepository.existsByUserId(userId)).willReturn(true);

        assertThatThrownBy(() -> goalService.createGoal(req, userId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("목표가 이미 존재합니다.");

        verify(goalRepository, never()).save(any());
    }
}
