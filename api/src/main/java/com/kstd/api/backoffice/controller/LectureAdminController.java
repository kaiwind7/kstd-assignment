package com.kstd.api.backoffice.controller;

import com.kstd.api.backoffice.service.LectureAdminService;
import com.kstd.api.common.response.ApiResponse;
import com.kstd.api.domain.lecture.dto.LectureDTO;
import com.kstd.api.domain.lecture.request.LectureRequest;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/lectures")
public class LectureAdminController {

    private final LectureAdminService lectureService;

    @GetMapping
    @Operation(summary = "강의 목록 조회", description = "강의 목록 조회")
    public ApiResponse<Page<LectureDTO>> getLectureList(@RequestParam(defaultValue = "30") int size,
                                                        @RequestParam(defaultValue = "0") int page) {
        return ApiResponse.success(lectureService.findAllLecture(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ApiResponse<LectureDTO> getLecture(@PathVariable Long id) {
        LectureDTO lectureDTO = lectureService.findLecture(id);
        return ApiResponse.success(lectureDTO);
    }

    @PostMapping("")
    public ApiResponse createLecture(@RequestBody LectureRequest request) {
        lectureService.saveLecture(request);
        return ApiResponse.success();
    }

    @PutMapping("/{id}")
    public ApiResponse updateLecture(@PathVariable Long id, @RequestBody LectureRequest request) {
        lectureService.updateLecture(id, request);
        return ApiResponse.success();
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteLecture(@PathVariable Long id) {
        lectureService.deleteLecture(id);
        return ApiResponse.success();
    }

}
