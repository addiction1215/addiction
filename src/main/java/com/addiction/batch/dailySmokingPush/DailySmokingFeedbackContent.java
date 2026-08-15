package com.addiction.batch.dailySmokingPush;

public record DailySmokingFeedbackContent(String statusMessage, String actionMessage) {
    public String toPushBody() {
        return statusMessage + "\n" + actionMessage;
    }
}
