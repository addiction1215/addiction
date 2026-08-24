package com.addiction.pushOutbox.entity;

public enum PushOutboxStatus {
    PENDING,
    PROCESSING,
    SENT,
    RETRY,
    FAILED
}
