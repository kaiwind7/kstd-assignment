package com.kstd.api.domain.lecture.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "강연저장요청")
public class LectureRequest {
    @Schema(description = "강연자")
    @NotBlank(message = "강연자는 필수 항목입니다.")
    private String lecturer;

    @Schema(description = "강연장소")
    @NotBlank(message = "강연 장소는 필수 항목입니다.")
    private String place;

    @Schema(description = "참여 가능 인원")
    @Positive(message = "참여 가능 인원은 0보다 큰 값이어야 합니다.")
    private int capacity;

    @Schema(description = "강연시간")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Future(message = "강연 시간은 현재 시간 이후여야 합니다.")
    private LocalDateTime time;

    @Schema(description = "강연내용")
    @Size(max = 500, message = "강연 내용은 최대 500자까지 입력 가능합니다.")
    private String content;
}
