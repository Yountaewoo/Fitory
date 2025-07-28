package userServiceTest;

import io.github.yountaewoo.Gender;
import io.github.yountaewoo.user.User;
import io.github.yountaewoo.user.UserRepository;
import io.github.yountaewoo.user.UserService;
import io.github.yountaewoo.user.dto.HeightRequest;
import io.github.yountaewoo.user.dto.UserRequest;
import io.github.yountaewoo.user.dto.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("존재하는 사용자가 있으면 save 호출 없이 그대로 반환한다")
    void givenExistingUser_whenGetOrCreateUser_thenReturnExisting() {
        // given
        String userId = "user1";
        UserRequest req = new UserRequest("김철수", 175.0, Gender.Man);
        User existing = new User(userId, req.name(), req.height(), req.gender());
        given(userRepository.findById(userId)).willReturn(Optional.of(existing));

        // when
        UserResponse resp = userService.getOrCreateUser(userId, req);

        // then
        assertThat(resp.userId()).isEqualTo(userId);
        assertThat(resp.userName()).isEqualTo(req.name());
        assertThat(resp.height()).isEqualTo(req.height());
        assertThat(resp.gender()).isEqualTo(req.gender());
        then(userRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("사용자가 없으면 save 호출 후 새로 생성된 엔티티를 반환한다")
    void givenNoUser_whenGetOrCreateUser_thenSaveAndReturnNew() {
        // given
        String userId = "user2";
        UserRequest req = new UserRequest("이영희", 162.5, Gender.Woman);
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        User saved = new User(userId, req.name(), req.height(), req.gender());
        given(userRepository.save(any(User.class))).willReturn(saved);

        // when
        UserResponse resp = userService.getOrCreateUser(userId, req);

        // then
        assertThat(resp.userId()).isEqualTo(userId);
        assertThat(resp.userName()).isEqualTo(req.name());
        assertThat(resp.height()).isEqualTo(req.height());
        assertThat(resp.gender()).isEqualTo(req.gender());
        then(userRepository).should().save(any(User.class));
    }

    @Test
    @DisplayName("존재하는 사용자를 탈퇴 처리하면 withdrawn 플래그가 true가 된다")
    void givenExistingUser_whenWithdrawMember_thenMarkWithdrawn() {
        // given
        String userId = "user3";
        User existing = new User(userId, "박영수", 180.0, Gender.Man);
        given(userRepository.findById(userId)).willReturn(Optional.of(existing));

        // when
        userService.withdrawMember(userId);

        // then
        assertThat(existing.isWithdrawn()).isTrue();
        then(userRepository).should().findById(userId);
    }

    @Test
    @DisplayName("존재하지 않는 사용자를 탈퇴 처리하면 NoSuchElementException 발생")
    void givenNoUser_whenWithdrawMember_thenThrowException() {
        // given
        String userId = "nonexistent";
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.withdrawMember(userId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 사용자가 없습니다.");
        then(userRepository).should().findById(userId);
    }

    // --- updateHeight 메서드 테스트 추가 ↓ ---

    @Test
    @DisplayName("존재하는 사용자의 키를 업데이트하면 height 필드가 변경된다")
    void givenExistingUser_whenUpdateHeight_thenHeightIsUpdated() {
        // given
        String userId = "user4";
        User existing = new User(userId, "최민수", 170.0, Gender.Man);
        given(userRepository.findById(userId)).willReturn(Optional.of(existing));

        HeightRequest req = new HeightRequest(182.5);

        // when
        userService.updateHeight(userId, req);

        // then
        assertThat(existing.getHeight()).isEqualTo(req.height());
        then(userRepository).should().findById(userId);
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 키 업데이트 시 NoSuchElementException 발생")
    void givenNoUser_whenUpdateHeight_thenThrowException() {
        // given
        String userId = "unknown";
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        HeightRequest req = new HeightRequest(180.0);

        // when & then
        assertThatThrownBy(() -> userService.updateHeight(userId, req))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 사용자가 없습니다.");
        then(userRepository).should().findById(userId);
    }
}
