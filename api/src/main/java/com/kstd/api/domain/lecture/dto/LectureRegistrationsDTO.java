package com.kstd.api.domain.lecture.dto;

import com.kstd.api.domain.user.dto.UserDTO;
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
@Schema(description = "강연 신청자 목록")
public class LectureRegistrationsDTO {
    @Schema(description = "강연 정보")
    private LectureDTO lecture;
    @Schema(description = "강연 신청자 목록")
    private List<UserDTO> users;
}
