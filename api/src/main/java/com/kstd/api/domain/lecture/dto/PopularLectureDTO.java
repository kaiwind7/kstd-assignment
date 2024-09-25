package com.kstd.api.domain.lecture.dto;

import com.kstd.api.domain.lecture.entity.Lecture;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "인기강연")
public class PopularLectureDTO {
    @Schema(description = "강연정보")
    private LectureDTO lecture;
    @Schema(description = "강연등록자수")
    private Long registrationCount;

    private PopularLectureDTO(Lecture lecture, Long registrationCount) {
        this.lecture = LectureDTO.fromEntity(lecture);
        this.registrationCount = registrationCount;
    }
}
