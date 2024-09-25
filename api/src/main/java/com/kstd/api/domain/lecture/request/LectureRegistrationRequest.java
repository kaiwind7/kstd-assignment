package com.kstd.api.domain.lecture.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 강연 등록")
public class LectureRegistrationRequest {
    @Schema(description = "사용자ID")
    private Long userId;
    @Schema(description = "강연ID")
    private Long lectureId;
}
