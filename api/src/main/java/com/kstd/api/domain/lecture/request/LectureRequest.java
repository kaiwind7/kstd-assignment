package com.kstd.api.domain.lecture.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LectureRequest {
    private String lecturer;
    private String place;
    private int capacity;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") // 요청 본문의 형식에 맞게 설정
    private LocalDateTime time;
    private String content;
}
