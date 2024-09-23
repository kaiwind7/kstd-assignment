package com.kstd.api.domain.lecture.dto;

import com.kstd.api.domain.lecture.entity.Lecture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LectureDTO {
    private Long id;
    private String lecturer;
    private String place;
    private int capacity;
    private LocalDateTime time;
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
