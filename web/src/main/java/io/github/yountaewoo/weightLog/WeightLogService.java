package io.github.yountaewoo.weightLog;

import io.github.yountaewoo.user.User;
import io.github.yountaewoo.user.UserRepository;
import io.github.yountaewoo.weightLog.dto.WeightLogRequest;
import io.github.yountaewoo.weightLog.dto.WeightLogResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Slf4j
@Service
public class WeightLogService {

    private final WeightLogRepository weightLogRepository;
    private final UserRepository userRepository;

    private WeightLogResponse transferToWeightLogResponse(WeightLog weightLog, User user, WeightLogRequest weightLogRequest) {
        return new WeightLogResponse(user.getId(), weightLogRequest.weight(), weightLog.getRecordDate());
    }

    @Transactional
    public WeightLogResponse create(String id, WeightLogRequest weightLogRequest) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("해당하는 사용자가 없습니다."));
        WeightLog weightLog = weightLogRepository.save(new WeightLog(user.getId(), weightLogRequest.weight(), LocalDate.now()));
        return transferToWeightLogResponse(weightLog, user, weightLogRequest);
    }
}
