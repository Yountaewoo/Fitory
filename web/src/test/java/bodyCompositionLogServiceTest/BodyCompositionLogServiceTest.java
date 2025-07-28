package bodyCompositionLogServiceTest;

import io.github.yountaewoo.bodyCompositionLog.BodyCompositionLog;
import io.github.yountaewoo.bodyCompositionLog.BodyCompositionLogRepository;
import io.github.yountaewoo.bodyCompositionLog.BodyCompositionLogService;
import io.github.yountaewoo.bodyCompositionLog.dto.BodyCompositionLogRequest;
import io.github.yountaewoo.bodyCompositionLog.dto.BodyCompositionLogResponse;
import io.github.yountaewoo.user.User;
import io.github.yountaewoo.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BodyCompositionLogServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BodyCompositionLogRepository bodyCompositionLogRepository;

    @InjectMocks
    private BodyCompositionLogService bodyCompositionLogService;

    @Captor
    private ArgumentCaptor<BodyCompositionLog> logCaptor;

    private final String USER_ID = "user-123";
    private final LocalDate TODAY = LocalDate.now();
    private User dummyUser;

    @BeforeEach
    void setUp() {
        dummyUser = new User(USER_ID, "테스트", 180.0, null);
    }

    // findOrCreateTodayLog Tests

    @Test
    void findOrCreateTodayLog_userNotFound_throwsException() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                bodyCompositionLogService.findOrCreateTodayLog(USER_ID,
                        new BodyCompositionLogRequest(70.0, 15.0, 30.0))
        )
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 사용자가 없습니다.");

        verify(userRepository).findById(USER_ID);
        verifyNoInteractions(bodyCompositionLogRepository);
    }

    @Test
    void findOrCreateTodayLog_logExists_returnsExisting() {
        BodyCompositionLog existingLog = new BodyCompositionLog(
                USER_ID, 65.5, 12.3, 28.1, TODAY);
        ReflectionTestUtils.setField(existingLog, "id", 42L);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(dummyUser));
        when(bodyCompositionLogRepository.findByUserIdAndRecordDate(USER_ID, TODAY))
                .thenReturn(Optional.of(existingLog));

        BodyCompositionLogResponse resp = bodyCompositionLogService.findOrCreateTodayLog(
                USER_ID,
                new BodyCompositionLogRequest(99.9, 99.9, 99.9)
        );

        assertThat(resp.id()).isEqualTo(42L);
        assertThat(resp.userId()).isEqualTo(USER_ID);
        assertThat(resp.weight()).isEqualTo(65.5);
        assertThat(resp.bodyFatMass()).isEqualTo(12.3);
        assertThat(resp.skeletalMuscleMass()).isEqualTo(28.1);
        assertThat(resp.recordDate()).isEqualTo(TODAY);

        verify(bodyCompositionLogRepository, never()).save(any());
    }

    @Test
    void findOrCreateTodayLog_logNotExists_createsAndReturnsNew() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(dummyUser));
        when(bodyCompositionLogRepository.findByUserIdAndRecordDate(USER_ID, TODAY))
                .thenReturn(Optional.empty());

        BodyCompositionLog savedLog = new BodyCompositionLog(
                USER_ID, 70.0, 15.0, 30.0, TODAY);
        ReflectionTestUtils.setField(savedLog, "id", 99L);

        when(bodyCompositionLogRepository.save(any(BodyCompositionLog.class)))
                .thenReturn(savedLog);

        BodyCompositionLogRequest req = new BodyCompositionLogRequest(70.0, 15.0, 30.0);
        BodyCompositionLogResponse resp = bodyCompositionLogService.findOrCreateTodayLog(USER_ID, req);

        verify(bodyCompositionLogRepository).save(logCaptor.capture());
        BodyCompositionLog toSave = logCaptor.getValue();
        assertThat(toSave.getUserId()).isEqualTo(USER_ID);
        assertThat(toSave.getWeight()).isEqualTo(70.0);
        assertThat(toSave.getBodyFatMass()).isEqualTo(15.0);
        assertThat(toSave.getSkeletalMuscleMass()).isEqualTo(30.0);
        assertThat(toSave.getRecordDate()).isEqualTo(TODAY);

        assertThat(resp.id()).isEqualTo(99L);
        assertThat(resp.userId()).isEqualTo(USER_ID);
        assertThat(resp.weight()).isEqualTo(70.0);
        assertThat(resp.bodyFatMass()).isEqualTo(15.0);
        assertThat(resp.skeletalMuscleMass()).isEqualTo(30.0);
        assertThat(resp.recordDate()).isEqualTo(TODAY);
    }

    // update Tests

    @Test
    void update_userNotFound_throwsException() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        BodyCompositionLogRequest req = new BodyCompositionLogRequest(60.0, 20.0, 30.0);
        assertThatThrownBy(() -> bodyCompositionLogService.update(USER_ID, req))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 사용자가 없습니다.");

        verify(userRepository).findById(USER_ID);
        verifyNoMoreInteractions(bodyCompositionLogRepository);
    }

    @Test
    void update_logNotFound_throwsException() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(dummyUser));
        when(bodyCompositionLogRepository.findByUserIdAndRecordDate(USER_ID, TODAY))
                .thenReturn(Optional.empty());

        BodyCompositionLogRequest req = new BodyCompositionLogRequest(60.0, 20.0, 30.0);
        assertThatThrownBy(() -> bodyCompositionLogService.update(USER_ID, req))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 기록이 없습니다.");

        verify(userRepository).findById(USER_ID);
        verify(bodyCompositionLogRepository).findByUserIdAndRecordDate(USER_ID, TODAY);
    }

    @Test
    void update_partialFields_updatesOnlyNonNull() {
        BodyCompositionLog existingLog = new BodyCompositionLog(USER_ID, 65.0, 12.0, 28.0, TODAY);
        ReflectionTestUtils.setField(existingLog, "id", 5L);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(dummyUser));
        when(bodyCompositionLogRepository.findByUserIdAndRecordDate(USER_ID, TODAY))
                .thenReturn(Optional.of(existingLog));

        BodyCompositionLogRequest req = new BodyCompositionLogRequest(70.5, null, 32.1);
        BodyCompositionLogResponse resp = bodyCompositionLogService.update(USER_ID, req);

        assertThat(resp.id()).isEqualTo(5L);
        assertThat(resp.userId()).isEqualTo(USER_ID);
        assertThat(resp.weight()).isEqualTo(70.5);
        assertThat(resp.bodyFatMass()).isEqualTo(12.0);
        assertThat(resp.skeletalMuscleMass()).isEqualTo(32.1);
        assertThat(resp.recordDate()).isEqualTo(TODAY);
    }

    @Test
    void update_allFields_updatedCorrectly() {
        BodyCompositionLog existingLog = new BodyCompositionLog(USER_ID, 65.0, 12.0, 28.0, TODAY);
        ReflectionTestUtils.setField(existingLog, "id", 8L);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(dummyUser));
        when(bodyCompositionLogRepository.findByUserIdAndRecordDate(USER_ID, TODAY))
                .thenReturn(Optional.of(existingLog));

        BodyCompositionLogRequest req = new BodyCompositionLogRequest(75.0, 18.5, 34.2);
        BodyCompositionLogResponse resp = bodyCompositionLogService.update(USER_ID, req);

        assertThat(resp.id()).isEqualTo(8L);
        assertThat(resp.userId()).isEqualTo(USER_ID);
        assertThat(resp.weight()).isEqualTo(75.0);
        assertThat(resp.bodyFatMass()).isEqualTo(18.5);
        assertThat(resp.skeletalMuscleMass()).isEqualTo(34.2);
        assertThat(resp.recordDate()).isEqualTo(TODAY);
    }
}
