package com.addiction.batch.dailySmokingPush;

import com.addiction.common.enums.DailySmokingFeedbackGrade;
import com.addiction.common.enums.DailySmokingFeedbackTime;

import java.util.List;
import java.util.Map;

public record DailySmokingFeedbackMessagePool(
        Map<DailySmokingFeedbackGrade, List<String>> statusMessages,
        Map<DailySmokingFeedbackGrade, Map<DailySmokingFeedbackTime, List<String>>> actionMessages
) {
}
