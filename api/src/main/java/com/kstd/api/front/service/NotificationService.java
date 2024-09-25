package com.kstd.api.front.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Async
    public void notifyUser(Long userId, String message) {
        // TODO 이메일 or 메신저 등 구현
    }
}
