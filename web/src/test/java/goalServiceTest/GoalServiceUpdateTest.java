package goalServiceTest;

import io.github.yountaewoo.goal.Goal;
import io.github.yountaewoo.goal.GoalRepository;
import io.github.yountaewoo.goal.GoalService;
import io.github.yountaewoo.goal.dto.GoalRequest;
import io.github.yountaewoo.goal.dto.GoalResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceUpdateTest {

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private GoalService goalService;

    @Test
    @DisplayName("성공: 기존 목표를 조회해 값들을 업데이트하고 응답을 반환한다")
    void givenExistingGoal_whenUpdate_thenFieldsAreUpdatedAndResponseReturned() {
        // given
        String userId = "user-1";
        Goal existing = new Goal(userId, 20.0, 35.0, LocalDate.now().plusDays(30));
        // id, startDate 세팅 필요 시 리플렉션/세터/팩토리 사용. 여기선 startDate는 @PrePersist 전제라 생략.
        given(goalRepository.findByUserId(userId)).willReturn(Optional.of(existing));

        // 업데이트 요청
        LocalDate newEnd = LocalDate.now().plusDays(60);
        GoalRequest req = new GoalRequest(18.5, 36.2, newEnd);

        // when
        GoalResponse res = goalService.updateGoal(userId, req);

        // then: 엔티티가 변경되었는지
        assertThat(existing.getTargetBodyFatPercent()).isEqualTo(18.5);
        assertThat(existing.getTargetMuscleMes()).isEqualTo(36.2);
        assertThat(existing.getEndDate()).isEqualTo(newEnd);

        // then: 응답 값도 일치하는지
        assertThat(res.userId()).isEqualTo(userId);
        assertThat(res.targetBodyFatPercent()).isEqualTo(18.5);
        assertThat(res.targetMuscleMes()).isEqualTo(36.2);
        assertThat(res.endDate()).isEqualTo(newEnd);

        // JPA 관리 하에서는 save 호출 없어도 dirty checking으로 반영되므로 save 호출이 없는지 확인(선택)
        then(goalRepository).should(times(1)).findByUserId(userId);
        then(goalRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("실패: 해당 사용자의 목표가 없으면 NoSuchElementException 발생")
    void givenNoGoal_whenUpdate_thenThrow() {
        // given
        String userId = "user-404";
        given(goalRepository.findByUserId(userId)).willReturn(Optional.empty());
        GoalRequest req = new GoalRequest(18.0, 35.0, LocalDate.now().plusDays(10));

        // when & then
        assertThatThrownBy(() -> goalService.updateGoal(userId, req))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당하는 목표가 없습니다.");

        then(goalRepository).should(times(1)).findByUserId(userId);
        then(goalRepository).shouldHaveNoMoreInteractions();
    }
}

