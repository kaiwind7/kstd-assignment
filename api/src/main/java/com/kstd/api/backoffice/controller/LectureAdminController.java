package com.kstd.api.backoffice.controller;

import com.kstd.api.backoffice.service.LectureAdminService;
import com.kstd.api.common.response.ApiResponse;
import com.kstd.api.domain.lecture.dto.LectureDTO;
import com.kstd.api.domain.lecture.dto.LectureRegistrationsDTO;
import com.kstd.api.domain.lecture.request.LectureRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/lectures")
@Tag(name = "백오피스 강연 API")
public class LectureAdminController {
    private final LectureAdminService lectureAdminService;

    @GetMapping
    @Operation(summary = "강연 정보 목록 조회", description = "강연 정보의 목록을 페이징해서 조회한다.")
    public ApiResponse<Page<LectureDTO>> getLectureList(@Parameter(description = "페이지", example = "0") @RequestParam(defaultValue = "0") int page,
                                                        @Parameter(description = "사이즈", example = "30") @RequestParam(defaultValue = "30") int size) {
        return ApiResponse.success(lectureAdminService.findAllLecture(PageRequest.of(page, size)));
    }

    @GetMapping("/{lectureId}")
    @Operation(summary = "강연 정보 조회", description = "강연 정보 단건을 조회한다.")
    public ApiResponse<LectureDTO> getLecture(@Parameter(description = "강연 ID") @PathVariable Long lectureId) {
        LectureDTO lectureDTO = lectureAdminService.findLecture(lectureId);
        return ApiResponse.success(lectureDTO);
    }

    @PostMapping
    @Operation(summary = "강연 정보 저장", description = "강연 정보를 저장한다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "저장 성공"),
    })
    public ApiResponse<Void> createLecture(@Valid @RequestBody LectureRequest request) {
        lectureAdminService.saveLecture(request);
        return ApiResponse.success();
    }

    @PutMapping("/{lectureId}")
    @Operation(summary = "강연 정보 변경", description = "강연 정보를 변경한다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "정보 변경 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "강연 정보가 없습니다."),
    })
    public ApiResponse<Void> updateLecture(@PathVariable Long lectureId, @RequestBody LectureRequest request) {
        lectureAdminService.updateLecture(lectureId, request);
        return ApiResponse.success();
    }

    @DeleteMapping("/{lectureId}")
    @Operation(summary = "강연 정보 삭제", description = "강연 정보를 삭제한다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "강연 정보가 없습니다."),
    })
    public ApiResponse<Void> deleteLecture(@PathVariable Long lectureId) {
        lectureAdminService.deleteLecture(lectureId);
        return ApiResponse.success();
    }

    @GetMapping("/{lectureId}/registrations")
    @Operation(summary = "강연 신청자 목록", description = "강연 신청자 목고을 조회한다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "강연 정보가 없습니다")
    })
    public ApiResponse<LectureRegistrationsDTO> getLectureRegistrations(@PathVariable Long lectureId) {
        return ApiResponse.success(lectureAdminService.findLectureRegistrations(lectureId));
    }
}
