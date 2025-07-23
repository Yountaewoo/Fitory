package io.github.yountaewoo.weightLog;

import io.github.yountaewoo.loginUtils.LoginMemberId;
import io.github.yountaewoo.weightLog.dto.UpdateWeightRequest;
import io.github.yountaewoo.weightLog.dto.WeightLogRequest;
import io.github.yountaewoo.weightLog.dto.WeightLogResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class WeightLogRestController {

    private final WeightLogService weightLogService;

    @PostMapping
    public WeightLogResponse create(@LoginMemberId String id, @RequestBody WeightLogRequest weightLogRequest) {
        return weightLogService.create(id, weightLogRequest);
    }

    @PatchMapping
    public WeightLogResponse update(@LoginMemberId String id, @RequestBody UpdateWeightRequest updateWeightRequest) {
        return weightLogService.update(id, updateWeightRequest);
    }
}
