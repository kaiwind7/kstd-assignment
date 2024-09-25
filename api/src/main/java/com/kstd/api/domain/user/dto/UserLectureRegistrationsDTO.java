package com.kstd.api.domain.user.dto;

import com.kstd.api.domain.lecture.dto.LectureDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 신청 간의")
public class UserLectureRegistrationsDTO {
    @Schema(description = "사용자 정보")
    private UserDTO user;
    @Schema(description = "신청 강의 목록")
    private List<LectureDTO> lecture;
}
