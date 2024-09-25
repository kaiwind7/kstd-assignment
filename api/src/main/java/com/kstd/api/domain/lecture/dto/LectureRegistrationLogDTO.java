package com.kstd.api.domain.lecture.dto;

import com.kstd.api.common.enums.Status;
import com.kstd.api.domain.lecture.entity.LectureRegistrationLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "강연등록로그")
public class LectureRegistrationLogDTO {
    @Schema(description = "사용자ID")
    private Long userId;
    @Schema(description = "강연ID")
    private Long lectureId;
    @Schema(description = "등록요청상태")
    private Status status;
    @Schema(description = "메시지")
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
