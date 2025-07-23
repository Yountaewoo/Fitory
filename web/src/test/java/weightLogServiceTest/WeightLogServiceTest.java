package weightLogServiceTest;

import io.github.yountaewoo.user.User;
import io.github.yountaewoo.user.UserRepository;
import io.github.yountaewoo.weightLog.WeightLog;
import io.github.yountaewoo.weightLog.WeightLogRepository;
import io.github.yountaewoo.weightLog.WeightLogService;
import io.github.yountaewoo.weightLog.dto.WeightLogRequest;
import io.github.yountaewoo.weightLog.dto.WeightLogResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeightLogServiceTest {

    @InjectMocks
    private WeightLogService weightLogService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WeightLogRepository weightLogRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_success() {
        // given
        String userId = "user123";
        double weight = 72.5;
        User user = new User(userId, "Taewoo", 169.3, null);
        WeightLog savedLog = new WeightLog(userId, weight, LocalDate.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(weightLogRepository.save(any(WeightLog.class))).thenReturn(savedLog);

        // when
        WeightLogResponse response = weightLogService.create(userId, new WeightLogRequest(weight));

        // then
        assertThat(response).isNotNull();
        assertThat(response.userId()).isEqualTo(userId);
        assertThat(response.weight()).isEqualTo(weight);
        assertThat(response.recordDate()).isEqualTo(LocalDate.now());

        verify(userRepository).findById(userId);
        verify(weightLogRepository).save(argThat(log ->
                log.getUserId().equals(userId) &&
                        log.getWeight() == weight &&
                        log.getRecordDate().equals(LocalDate.now())
        ));
    }

    @Test
    void create_userNotFound_throws() {
        // given
        String userId = "unknown";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() ->
                weightLogService.create(userId, new WeightLogRequest(60.0))
        ).isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 사용자가 없습니다.");

        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(weightLogRepository);
    }
}
