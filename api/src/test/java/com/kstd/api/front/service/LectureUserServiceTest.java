package com.kstd.api.front.service;

import com.kstd.api.common.enums.Status;
import com.kstd.api.common.exception.ServiceException;
import com.kstd.api.domain.lecture.dto.LectureDTO;
import com.kstd.api.domain.lecture.dto.LectureRegistrationLogDTO;
import com.kstd.api.domain.lecture.dto.PopularLectureDTO;
import com.kstd.api.domain.lecture.entity.Lecture;
import com.kstd.api.domain.lecture.entity.LectureRegistration;
import com.kstd.api.domain.lecture.entity.LectureRegistrationLog;
import com.kstd.api.domain.lecture.repository.LectureRegistrationLogRepository;
import com.kstd.api.domain.lecture.repository.LectureRegistrationRepository;
import com.kstd.api.domain.lecture.repository.LectureRepository;
import com.kstd.api.domain.user.dto.UserLectureRegistrationsDTO;
import com.kstd.api.domain.user.entity.User;
import com.kstd.api.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class LectureUserServiceTest {
    // 테스트할 LectureUserService 클래스에 필요한 Mock 객체를 주입
    @InjectMocks
    private LectureUserService lectureUserService;

    @Spy
    private UserRepository userRepository;

    @Spy
    private LectureRepository lectureRepository;

    @Spy
    private LectureRegistrationRepository lectureRegistrationRepository;

    @Spy
    private LectureRegistrationLogRepository lectureRegistrationLogRepository;

    private User user;
    private Lecture lecture;
    private LectureRegistration registration;

    @BeforeEach
    public void setup() {
        // 테스트 데이터 초기화
        // 사용자 데이터 설정
        user = User.builder()
                .id(1L)
                .name("Test User").build();

        lecture = Lecture.builder()
                .id(1L)
                .lecturer("Original Lecturer")
                .place("Original Place")
                .capacity(30)
                .time(LocalDateTime.of(2024, 9, 25, 14, 0))
                .content("Original Content")
                .build();

        registration = LectureRegistration.builder()
                .id(1L)
                .lecture(lecture)
                .user(user)
                .registrationTime(LocalDateTime.now())
                .status(Status.CONFIRMED).build();
    }

    @Test
    public void testFindLectureByScheduled_Success() {
        // Given: 강연 일정 조회 시 일정 내의 강연 목록을 반환하도록 Mock 설정
        when(lectureRepository.findAllByTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(lecture));

        // When: 강연 일정 조회 메서드 호출
        List<LectureDTO> lectures = lectureUserService.findLectureByScheduled();

        // Then: 조회된 강연 목록이 예상대로 반환되는지 검증
        assertNotNull(lectures);
        assertEquals(1, lectures.size());
        assertEquals(lecture.getId(), lectures.get(0).getId());

        // Mock 메서드 호출 검증
        verify(lectureRepository, times(1)).findAllByTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    public void testFindLectureRegistrationsByUserNo_Success() {
        // Given: 사용자 사번으로 강연 신청 내역 조회 시 Mock 설정
        when(userRepository.findByUserNo(user.getUserNo())).thenReturn(Optional.of(user));
        when(lectureRegistrationRepository.findLectureRegistrationByUserAndStatus(user, Status.CONFIRMED))
                .thenReturn(Arrays.asList(registration));

        // When: 사용자 사번으로 강연 신청 내역 조회
        UserLectureRegistrationsDTO result = lectureUserService.findLectureRegistrationsByUserNo(user.getUserNo());

        // Then: 조회된 강연 신청 내역이 예상과 일치하는지 검증
        assertNotNull(result);
        assertEquals(user.getUserNo(), result.getUser().getUserNo());
        assertEquals(1, result.getLecture().size());
        assertEquals(lecture.getId(), result.getLecture().get(0).getId());

        // Mock 메서드 호출 검증
        verify(userRepository, times(1)).findByUserNo(user.getUserNo());
        verify(lectureRegistrationRepository, times(1)).findLectureRegistrationByUserAndStatus(user, Status.CONFIRMED);
    }


    @Test
    public void testCancelRegistration_Success() {
        // Given: 강연 신청 취소 요청 시 Mock 설정
        when(lectureRegistrationRepository.findById(registration.getId())).thenReturn(Optional.of(registration));

        // When: 강연 신청 취소 처리
        lectureUserService.cancelRegistration(lecture.getId(), registration.getId());

        // Then: 강연 신청 상태가 'CANCELED'로 변경되었는지 확인
        verify(lectureRegistrationRepository, times(1)).findById(registration.getId());
        assertEquals(Status.CANCELED, registration.getStatus());
    }

    @Test
    public void testCancelRegistration_LectureIdMismatch() {
        // Given: 강연 신청 ID는 일치하지만 강연 ID가 일치하지 않는 경우 Mock 설정
        when(lectureRegistrationRepository.findById(registration.getId())).thenReturn(Optional.of(registration));

        // When: 강연 ID가 일치하지 않아 예외가 발생하는지 검증
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            lectureUserService.cancelRegistration(2L, registration.getId());
        });

        // Then: 예외 메시지가 예상과 일치하는지 확인
        assertEquals("강연 ID가 신청 정보와 일치하지 않습니다. 강연 ID: 2", exception.getMessage());
    }

    @Test
    public void testFindPopularLectures_Success() {
        // Given: 최근 3일간 가장 신청이 많은 강연을 조회할 때 Mock 설정
        PopularLectureDTO popularLectureDTO = new PopularLectureDTO(lecture, 10L);

        when(lectureRegistrationRepository.findPopularLecturesByRegistrationCount(any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(popularLectureDTO));

        // When: 인기 강연 조회
        List<PopularLectureDTO> popularLectures = lectureUserService.findPopularLectures();

        // Then: 조회된 인기 강연 목록이 예상과 일치하는지 확인
        assertNotNull(popularLectures);
        assertEquals(1, popularLectures.size());
        assertEquals(1L, popularLectures.get(0).getLecture().getId());

        // Mock 메서드 호출 검증
        verify(lectureRegistrationRepository, times(1)).findPopularLecturesByRegistrationCount(any(LocalDateTime.class));
    }

    @Test
    public void testFindLectureRegistrationLog_Success() {
        // Given: 특정 강연에 대한 신청 로그 조회 시 Mock 설정
        LectureRegistrationLog log = LectureRegistrationLog.builder()
                .id(1L)
                .userId(user.getId())
                .lectureId(lecture.getId())
                .status(Status.QUEUED).build();

        // Mock 설정: findByUserIdAndLectureId 메서드가 신청 로그를 반환하도록 설정
        when(lectureRegistrationLogRepository.findByUserIdAndLectureId(user.getId(), lecture.getId()))
                .thenReturn(Optional.of(log));

        // When: 강연 신청 로그 조회
        LectureRegistrationLogDTO result = lectureUserService.findLectureRegistrationLog(lecture.getId(), user.getId());

        // Then: 조회된 신청 로그가 예상과 일치하는지 확인
        assertNotNull(result);
        assertEquals(log.getUserId(), result.getUserId());
        assertEquals(log.getLectureId(), result.getLectureId());
        assertEquals(Status.QUEUED, result.getStatus());

        // Mock 메서드 호출 검증
        verify(lectureRegistrationLogRepository, times(1)).findByUserIdAndLectureId(user.getId(), lecture.getId());
    }

}
