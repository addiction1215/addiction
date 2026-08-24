package com.addiction.dailySmokingPush.service;

import com.addiction.dailySmokingPush.entity.DailySmokingPushSchedule;
import com.addiction.common.enums.DailySmokingFeedbackTime;
import com.addiction.dailySmokingPush.repository.DailySmokingPushScheduleJpaRepository;
import com.addiction.dailySmokingPush.service.request.DailySmokingPushScheduleUpdateServiceRequest;
import com.addiction.dailySmokingPush.service.response.DailySmokingPushScheduleResponse;
import com.addiction.global.exception.AddictionException;
import com.addiction.global.security.SecurityService;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DailySmokingPushScheduleService {

    private final DailySmokingPushScheduleJpaRepository scheduleRepository;
    private final SecurityService securityService;
    private final UserReadService userReadService;

    public void createDefaults(User user) {
        if (scheduleRepository.findByUserIdOrderBySlot(user.getId()).isEmpty()) {
            scheduleRepository.saveAll(DailySmokingPushSchedule.createDefaults(user));
        }
    }

    /**
     * 로그인한 사용자의 일일 흡연 피드백 발송 설정을 조회한다.
     *
     * <p>SecurityService에서 현재 사용자 ID를 가져온 뒤, 슬롯별 설정 엔티티를 조회하고
     * API 응답에 필요한 DTO로 변환해 반환한다. 조회만 수행하므로 readOnly 트랜잭션을 사용한다.</p>
     */
    @Transactional(readOnly = true)
    public List<DailySmokingPushScheduleResponse> getMySchedules() {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        return scheduleRepository.findByUserIdOrderBySlot(userId).stream()
                .map(DailySmokingPushScheduleResponse::createResponse)
                .toList();
    }

    /**
     * 로그인한 사용자의 네 개 시간대(MORNING, LUNCH, DINNER, NIGHT) 설정을 한 번에 수정한다.
     *
     * <p>기존 사용자에게 설정 행이 없는 경우 기본 설정을 먼저 생성하고, 요청에 네 슬롯이 각각 한 번씩
     * 포함됐는지 검증한 뒤 시간과 발송 여부를 갱신한다.</p>
     */
    public List<DailySmokingPushScheduleResponse> updateMySchedules(List<DailySmokingPushScheduleUpdateServiceRequest> requests) {
        User user = userReadService.findById(securityService.getCurrentLoginUserInfo().getUserId());
        createDefaults(user);

        if (requests.size() != 4 || requests.stream().map(DailySmokingPushScheduleUpdateServiceRequest::getSlot).distinct().count() != 4) {
            throw new AddictionException("MORNING, LUNCH, DINNER, NIGHT 설정을 각각 한 번씩 입력해야 합니다.");
        }

        Map<DailySmokingFeedbackTime, DailySmokingPushSchedule> schedules = scheduleRepository.findByUserIdOrderBySlot(user.getId()).stream()
                .collect(Collectors.toMap(DailySmokingPushSchedule::getSlot, Function.identity()));

        requests.forEach(request -> {
            DailySmokingPushSchedule schedule = schedules.get(request.getSlot());
            if (schedule == null) throw new AddictionException("유효하지 않은 알림 시간대입니다.");
            schedule.update(request.getSendTime(), request.isEnabled());
        });

        return scheduleRepository.findByUserIdOrderBySlot(user.getId()).stream()
                .map(DailySmokingPushScheduleResponse::createResponse)
                .toList();
    }
}
