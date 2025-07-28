package io.github.yountaewoo.bodyComposition;

import io.github.yountaewoo.bodyCompositionLog.BodyCompositionLogService;
import io.github.yountaewoo.bodyCompositionLog.dto.BodyCompositionLogRequest;
import io.github.yountaewoo.bodyCompositionLog.dto.BodyCompositionLogResponse;
import io.github.yountaewoo.loginUtils.LoginMemberId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
public class BodyCompositionLogRestController {

    private final BodyCompositionLogService bodyCompositionLogService;

    @PostMapping
    public BodyCompositionLogResponse findOrCreateTodayLog(@LoginMemberId String userId, @RequestBody BodyCompositionLogRequest request) {
        return bodyCompositionLogService.findOrCreateTodayLog(userId, request);
    }

    @PatchMapping
    public BodyCompositionLogResponse update(@LoginMemberId String userId, @RequestBody BodyCompositionLogRequest request) {
        return bodyCompositionLogService.update(userId, request);
    }
}
