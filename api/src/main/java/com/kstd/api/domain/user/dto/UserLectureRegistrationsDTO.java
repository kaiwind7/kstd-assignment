package com.kstd.api.domain.user.dto;

import com.kstd.api.domain.lecture.dto.LectureDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLectureRegistrationsDTO {
    private UserDTO user;
    private List<LectureDTO> lecture;
}
