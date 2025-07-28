package io.github.yountaewoo.user;

import io.github.yountaewoo.user.dto.HeightRequest;
import io.github.yountaewoo.user.dto.UserRequest;
import io.github.yountaewoo.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    private UserResponse transToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getHeight(),
                user.getGender());
    }

    @Transactional
    public UserResponse getOrCreateUser(String userId, UserRequest userRequest) {
        User user = userRepository.findById(userId)
                .orElseGet(() -> userRepository.save(
                        new User(userId, userRequest.name(), userRequest.height(), userRequest.gender())
                ));
        return transToUserResponse(user);
    }

    public void withdrawMember(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("해당하는 사용자가 없습니다.")
        );
        user.withdraw();
    }

    @Transactional
    public void updateHeight(String userId, HeightRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("해당하는 사용자가 없습니다.")
        );
        user.updateHeight(request.height());
    }
}
