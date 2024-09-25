package com.kstd.api.backoffice.service;

import com.kstd.api.common.enums.ErrorCode;
import com.kstd.api.common.enums.Status;
import com.kstd.api.common.exception.ServiceException;
import com.kstd.api.domain.lecture.dto.LectureDTO;
import com.kstd.api.domain.lecture.dto.LectureRegistrationsDTO;
import com.kstd.api.domain.lecture.entity.Lecture;
import com.kstd.api.domain.lecture.entity.LectureRegistration;
import com.kstd.api.domain.lecture.repository.LectureRegistrationRepository;
import com.kstd.api.domain.lecture.repository.LectureRepository;
import com.kstd.api.domain.lecture.request.LectureRequest;
import com.kstd.api.domain.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureAdminService {
    private final LectureRepository lectureRepository;
    private final LectureRegistrationRepository lectureRegistrationRepository;

    /**
     * 전체 강연 목록 조회
     *
     * @param pageable 페이지 정보
     * @return 페이징된 강연 목록
     */
    public Page<LectureDTO> findAllLecture(Pageable pageable) {
        Page<Lecture> lectures = lectureRepository.findAll(pageable);
        return lectures.map(LectureDTO::fromEntity);
    }

    /**
     * 강의 정보 조회
     *
     * @param lectureId 강연 ID
     * @return 강연 엔티티
     */
    private Lecture findLectureById(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ServiceException("강연 정보가 없습니다.", ErrorCode.NOT_FOUND_ENTITY));
    }

    /**
     * 강연 정보 조회 후 DTO 변환
     *
     * @param lectureId
     * @return LectureDTO
     */
    public LectureDTO findLecture(Long lectureId) {
        Lecture lecture = findLectureById(lectureId);
        return LectureDTO.fromEntity(lecture);
    }

    /**
     * 강연 정보 저장
     *
     * @param lectureRequest 강연 요청 정보
     * @return 저장된 강연 DTO
     */
    @Transactional
    public LectureDTO saveLecture(LectureRequest lectureRequest) {
        Lecture lecture = lectureRepository.save(Lecture.builder()
                .lecturer(lectureRequest.getLecturer())
                .place(lectureRequest.getPlace())
                .capacity(lectureRequest.getCapacity())
                .time(lectureRequest.getTime())
                .content(lectureRequest.getContent())
                .build());
        return LectureDTO.fromEntity(lecture);
    }

    /**
     * 강연 정보 변경
     *
     * @param lectureId      강연 ID
     * @param lectureRequest 강연 요청 정보
     * @return 변경된 강연 DTO
     */
    @Transactional
    public LectureDTO updateLecture(Long lectureId, LectureRequest lectureRequest) {
        Lecture lecture = findLectureById(lectureId);
        lecture.modifyLecture(lectureRequest);
        return LectureDTO.fromEntity(lecture);
    }

    /**
     * 강연 정보 삭제
     *
     * @param lectureId 강연 ID
     */
    @Transactional
    public void deleteLecture(Long lectureId) {
        Lecture lecture = findLectureById(lectureId);
        lectureRepository.delete(lecture);
    }

    /**
     * 강연 신청자 목록 조회
     *
     * @param lectureId 강연 ID
     * @return 강연 신청자 목록 DTO
     */
    public LectureRegistrationsDTO findLectureRegistrations(Long lectureId) {
        Lecture lecture = findLectureById(lectureId);
        List<LectureRegistration> lectureRegistrations = lectureRegistrationRepository.findByLectureIdAndStatus(lectureId, Status.CONFIRMED);

        return LectureRegistrationsDTO.builder()
                .lecture(LectureDTO.fromEntity(lecture))
                .users(lectureRegistrations.stream()
                        .map(lectureRegistration -> UserDTO.fromEntity(lectureRegistration.getUser()))
                        .toList())
                .build();
    }
}
