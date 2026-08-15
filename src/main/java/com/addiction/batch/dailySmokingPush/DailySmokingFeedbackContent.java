package com.addiction.batch.dailySmokingPush;

public record DailySmokingFeedbackContent(String statusMessage, String actionMessage) {
    public static DailySmokingFeedbackContent onboarding(String actionMessage) {
        return new DailySmokingFeedbackContent(null, actionMessage);
    }

    public String toPushBody() {
        return statusMessage == null || statusMessage.isBlank()
                ? actionMessage
                : statusMessage + "\n" + actionMessage;
    }
}
