package userServiceTest;

import io.github.yountaewoo.Gender;
import io.github.yountaewoo.user.User;
import io.github.yountaewoo.user.UserRepository;
import io.github.yountaewoo.user.UserService;
import io.github.yountaewoo.user.dto.UserRequest;
import io.github.yountaewoo.user.dto.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}

