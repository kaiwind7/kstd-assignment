package com.kstd.api.front.service;

import com.kstd.api.common.enums.ErrorCode;
import com.kstd.api.common.exception.ServiceException;
import com.kstd.api.domain.lecture.dto.LectureDTO;
import com.kstd.api.domain.lecture.dto.PopularLectureDTO;
import com.kstd.api.domain.lecture.entity.Lecture;
import com.kstd.api.domain.lecture.entity.LectureRegistration;
import com.kstd.api.domain.lecture.repository.LectureRegistrationRepository;
import com.kstd.api.domain.lecture.repository.LectureRepository;
import com.kstd.api.domain.user.dto.UserDTO;
import com.kstd.api.domain.user.dto.UserLectureRegistrationsDTO;
import com.kstd.api.domain.user.entity.User;
import com.kstd.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureUserService {
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final LectureRegistrationRepository lectureRegistrationRepository;

    /**
     * 강연 목록 조회(신청 가능한 시점부터 강연 시작 시간 1일 후까지 노출)
     *
     * @return List<LectureDTO>
     */
    @Transactional(readOnly = true)
    public List<LectureDTO> findLectureByScheduled() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusWeeks(1); // 현재 시간으로부터 1주일 전
        LocalDateTime end = now.plusDays(1);     // 현재 시간으로부터 1일 후
        List<Lecture> lectures = lectureRepository.findAllByTimeBetween(start, end);

        return lectures.stream()
                .map(LectureDTO::fromEntity)
                .toList();
    }

    /**
     * 강의 신청 내역 조회
     *
     * @param userNo
     * @return UserLectureRegistrationsDTO
     */
    @Transactional(readOnly = true)
    public UserLectureRegistrationsDTO findLectureRegistrationsByUserNo(Long userNo) {
        // 사용자 조회
        User user = userRepository.findByUserNo(userNo).orElseThrow(() -> new ServiceException("해당 사번의 사용자 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND_ENTITY));

        // 해당 사용자에 대한 강연 신청 내역 조회
        List<LectureRegistration> lectureRegistrations = lectureRegistrationRepository.findLectureRegistrationByUser(user);

        return UserLectureRegistrationsDTO.builder()
                .user(UserDTO.fromEntity(user))
                .lecture(lectureRegistrations.stream()
                        .map(registration -> LectureDTO.fromEntity(registration.getLecture()))
                        .toList())
                .build();
    }

    @Transactional
    public void cancelRegistration(Long lectureId, Long registrationId) {
        LectureRegistration registration = lectureRegistrationRepository.findById(registrationId)
                .orElseThrow(() -> new ServiceException("신청 정보를 찾을 수 없습니다.", ErrorCode.NOT_FOUND_ENTITY));

        // 신청된 강연 ID가 일치하는지 확인
        if (!registration.getLecture().getId().equals(lectureId)) {
            throw new ServiceException("강연 정보가 일치하지 않습니다.", ErrorCode.INVALID_REQUEST);
        }

        registration.cancelLecture();
    }

    /**
     * 3일간 가장 신청이 많은 강연순으로 노출
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<PopularLectureDTO> findPopularLectures() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusDays(3);
        List<PopularLectureDTO> popularLectureDTOS = lectureRegistrationRepository.findPopularLecturesByRegistrationCount(start);
        return popularLectureDTOS;
    }

}



