package com.kstd.api.domain.lecture.dto;

import com.kstd.api.domain.lecture.entity.Lecture;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PopularLectureDTO {
    private LectureDTO lecture;
    private Long registrationCount;

    private PopularLectureDTO(Lecture lecture, Long registrationCount) {
        this.lecture = LectureDTO.fromEntity(lecture);
        this.registrationCount = registrationCount;
    }
}
