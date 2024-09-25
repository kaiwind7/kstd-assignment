package com.kstd.api.domain.lecture.dto;

import com.kstd.api.domain.lecture.entity.Lecture;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "강연정보")
public class LectureDTO {
    @Schema(description = "강연ID")
    private Long id;
    @Schema(description = "강연자")
    private String lecturer;
    @Schema(description = "강연장소")
    private String place;
    @Schema(description = "강연수용인원")
    private int capacity;
    @Schema(description = "강연시간")
    private LocalDateTime time;
    @Schema(description = "강연내용")
    private String content;

    public static LectureDTO fromEntity(Lecture lecture) {
        return LectureDTO.builder()
                .id(lecture.getId())
                .lecturer(lecture.getLecturer())
                .place(lecture.getPlace())
                .capacity(lecture.getCapacity())
                .time(lecture.getTime())
                .content(lecture.getContent())
                .build();
    }
}
