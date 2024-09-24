package com.kstd.api.domain.lecture.dto;

import com.kstd.api.domain.lecture.entity.LectureRegistrationLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LectureRegistrationLogDTO {
    private Long userId;
    private Long lectureId;
    private String status;
    private String message;

    public static LectureRegistrationLogDTO fromEntity(LectureRegistrationLog log) {
        return LectureRegistrationLogDTO.builder()
                .userId(log.getUserId())
                .lectureId(log.getUserId())
                .status(log.getStatus())
                .message(log.getMessage())
                .build();
    }
}
