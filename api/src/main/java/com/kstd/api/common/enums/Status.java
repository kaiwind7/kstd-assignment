package com.kstd.api.common.enums;

import lombok.Getter;

@Getter
public enum Status {
    QUEUED,
    PROCESSING,
    CONFIRMED,
    CANCELED,
    FAILED,
    SUCCESS,
    DUPLICATE_IN_QUEUE
}
