package com.kstd.api.domain.lecture.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LectureRegistrationRequest {
    private Long userId;
    private Long lectureId;
}
