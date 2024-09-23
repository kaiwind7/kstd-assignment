package com.kstd.api.front.controller;

import com.kstd.api.common.response.ApiResponse;
import com.kstd.api.common.response.ErrorResponse;
import com.kstd.api.domain.lecture.dto.LectureDTO;
import com.kstd.api.domain.lecture.dto.PopularLectureDTO;
import com.kstd.api.domain.lecture.request.LectureRegistrationRequest;
import com.kstd.api.domain.user.dto.UserLectureRegistrationsDTO;
import com.kstd.api.front.service.LectureRegistrationQueueService;
import com.kstd.api.front.service.LectureUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "사용자 강의")
public class LectureUserController {
    private final LectureUserService frontService;
    private final LectureRegistrationQueueService lectureRegistrationQueueService;

    @GetMapping("/lectures/scheduled")
    @Operation(summary = "강연 목록 조회", description = "신청 가능한 시점(1주일 전)부터 강연 시작 시간 1일 후까지 노출한다.")
    public ApiResponse<List<LectureDTO>> getLectureList() {
        return ApiResponse.success(frontService.findLectureByScheduled());
    }

    @PostMapping("/registrations/user")
    @Operation(summary = "강연 신청", description = "강연 신청(사번 입력, 같은 강연 중복 신청 제한)을 큐에 적재한다.")
    public ApiResponse registerForLecture(@RequestBody LectureRegistrationRequest lectureRegistrationRequest) throws InterruptedException {
        lectureRegistrationQueueService.addRegistrationToQueue(lectureRegistrationRequest);
        return ApiResponse.success();
    }

    @GetMapping("/registrations/user/{userNo}")
    @Operation(summary = "강의 신청 내역 조회", description = "강의 신청 내역 조회(사번 입력)한다", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserLectureRegistrationsDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 사번의 사용자 정보를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ApiResponse<UserLectureRegistrationsDTO> getUserLectureRegistrations(@Parameter(description = "사용자사번") @PathVariable Long userNo) {
        return ApiResponse.success(frontService.findLectureRegistrationsByUserNo(userNo));
    }

    @PutMapping("/{lectureId}/registrations/{registrationId}/cancel")
    @Operation(summary = "신청 강연 취소", description = "신청한 강연을 취소한다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "신청 강연 취소 성공", content = @Content(schema = @Schema(implementation = UserLectureRegistrationsDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "강연 정보가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "신청 정보를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    public ApiResponse updateRegistrationStatus(@Parameter(description = "강의ID") @PathVariable Long lectureId,
                                                @Parameter(description = "강의등록ID") @PathVariable Long registrationId) {
        frontService.cancelRegistration(lectureId, registrationId);
        return ApiResponse.success();
    }

    @GetMapping("/lectures/popular")
    @Operation(summary = "실시간 인기 강연", description = "실시간 인기 강연을 조회한다.(3일간 가장 신청이 많은 강연순으로 노출)", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = PopularLectureDTO.class))),
    })
    public ApiResponse<List<PopularLectureDTO>> getPopularLectures() {
        return ApiResponse.success(frontService.findPopularLectures());
    }
}
