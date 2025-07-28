package io.github.yountaewoo.bodyCompositionLog;

import io.github.yountaewoo.bodyCompositionLog.dto.BodyCompositionLogRequest;
import io.github.yountaewoo.bodyCompositionLog.dto.BodyCompositionLogResponse;
import io.github.yountaewoo.user.User;
import io.github.yountaewoo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Slf4j
@Service
public class BodyCompositionLogService {

    private final BodyCompositionLogRepository bodyCompositionLogRepository;
    private final UserRepository userRepository;

    private BodyCompositionLogResponse transferToBodyCompositionLogResponse(User user, BodyCompositionLog bodyCompositionLog) {
        return new BodyCompositionLogResponse(bodyCompositionLog.getId(), bodyCompositionLog.getUserId(), bodyCompositionLog.getWeight(),
                bodyCompositionLog.getBodyFatMass(), bodyCompositionLog.getSkeletalMuscleMass(), bodyCompositionLog.getRecordDate());
    }

    private void updateBodyFatMassIfNotNull(BodyCompositionLogRequest request, BodyCompositionLog bodyCompositionLog) {
        if (request.bodyFatMass() != null) {
            bodyCompositionLog.setBodyFatMass(request.bodyFatMass());
        }
    }

    private void updateWeightIfNotNull(BodyCompositionLogRequest request, BodyCompositionLog bodyCompositionLog) {
        if (request.weight() != null) {
            bodyCompositionLog.setWeight(request.weight());
        }
    }

    private void updateSkeletalMuscleMassIfNotNull(BodyCompositionLogRequest request, BodyCompositionLog bodyCompositionLog) {
        if (request.skeletalMuscleMass() != null) {
            bodyCompositionLog.setSkeletalMuscleMass(request.skeletalMuscleMass());
        }
    }

    @Transactional
    public BodyCompositionLogResponse findOrCreateTodayLog(String userId, BodyCompositionLogRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("해당하는 사용자가 없습니다."));
        BodyCompositionLog bodyCompositionLog = bodyCompositionLogRepository.findByUserIdAndRecordDate(user.getId(), LocalDate.now()).orElseGet(
                () -> bodyCompositionLogRepository.save(new BodyCompositionLog(user.getId(), request.weight(),
                        request.bodyFatMass(), request.skeletalMuscleMass(), LocalDate.now()))
        );
        return transferToBodyCompositionLogResponse(user, bodyCompositionLog);
    }

    @Transactional
    public BodyCompositionLogResponse update(String userId, BodyCompositionLogRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("해당하는 사용자가 없습니다."));
        BodyCompositionLog bodyCompositionLog = bodyCompositionLogRepository.findByUserIdAndRecordDate(user.getId(), LocalDate.now()).orElseThrow(
                () -> new NoSuchElementException("해당하는 기록이 없습니다."));
        updateBodyFatMassIfNotNull(request, bodyCompositionLog);
        updateSkeletalMuscleMassIfNotNull(request, bodyCompositionLog);
        updateWeightIfNotNull(request, bodyCompositionLog);
        return transferToBodyCompositionLogResponse(user, bodyCompositionLog);
    }
}
