package com.kstd.api.front.service;

import com.kstd.api.common.exception.ServiceException;
import com.kstd.api.domain.lecture.entity.Lecture;
import com.kstd.api.domain.lecture.entity.LectureRegistration;
import com.kstd.api.domain.lecture.entity.LectureRegistrationLog;
import com.kstd.api.domain.lecture.repository.LectureRegistrationLogRepository;
import com.kstd.api.domain.lecture.repository.LectureRegistrationRepository;
import com.kstd.api.domain.lecture.repository.LectureRepository;
import com.kstd.api.domain.lecture.request.LectureRegistrationRequest;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class LectureRegistrationQueueServiceTest {
    @InjectMocks
    private LectureRegistrationQueueService lectureRegistrationQueueService;

    @Spy
    private LectureRepository lectureRepository;

    @Spy
    private UserRepository userRepository;

    @Spy
    private LectureRegistrationRepository lectureRegistrationRepository;

    @Spy
    private LectureRegistrationLogRepository lectureRegistrationLogRepository;

    private User user;
    private Lecture lecture;
    private LectureRegistrationRequest request;

    @BeforeEach
    public void setup() {
        // 테스트 데이터를 설정합니다.
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

        request = LectureRegistrationRequest.builder()
                .userId(user.getId())
                .lectureId(lecture.getId()).build();
    }

    @Test
    public void testAddRegistrationToQueue_Success() {
        // 강의 등록 요청이 정상적으로 처리되는지 테스트
        // 성공적인 등록 시 "강의 등록 요청이 정상적으로 처리되었습니다." 메시지가 반환되는지 확인
        String response = lectureRegistrationQueueService.addRegistrationToQueue(request);
        assertEquals("강의 등록 요청이 정상적으로 처리되었습니다.", response);
    }

    @Test
    public void testAddRegistrationToQueue_DuplicateInQueue() {
        // 중복된 강연 등록 요청을 처리할 때 예상대로 "이미 대기열에 등록된 요청입니다." 메시지를 반환하는지 테스트

        // Given: 중복된 강연 등록 로그를 설정
        LectureRegistrationLog log = LectureRegistrationLog.builder()
                .userId(request.getUserId())
                .lectureId(request.getLectureId())
                .status("QUEUED")
                .message("Initial Log")
                .build();

        // 중복된 강연 로그를 반환하도록 설정
        when(lectureRegistrationLogRepository.findByUserIdAndLectureId(request.getUserId(), request.getLectureId()))
                .thenReturn(Optional.of(log));

        // 중복된 상태로 존재하는지 확인하는 로직
        when(lectureRegistrationLogRepository.existsByUserIdAndLectureIdAndStatusIn(
                request.getUserId(), request.getLectureId(), Arrays.asList("QUEUED", "PROCESSING")))
                .thenReturn(true);  // 중복 요청으로 간주하도록 true를 반환

        // When: 강연 등록 요청 시 중복 상태 메시지 확인
        String response = lectureRegistrationQueueService.addRegistrationToQueue(request);
        assertEquals("이미 대기열에 등록된 요청입니다.", response);
    }

    @Test
    public void testProcessRegistration_Success() {
        // 성공적으로 강의 신청이 처리되는지 테스트
        // Given: 강연과 사용자 정보가 존재하고, 등록된 강연이 없는 경우
        when(lectureRepository.findById(request.getLectureId()))
                .thenReturn(Optional.of(lecture));

        when(userRepository.findById(request.getUserId()))
                .thenReturn(Optional.of(user));


        when(lectureRegistrationRepository.findByLectureIdAndUserId(request.getLectureId(), request.getUserId()))
                .thenReturn(Optional.empty());

        when(lectureRegistrationRepository.findByLectureId(request.getLectureId()))
                .thenReturn(Arrays.asList());

        // When: 강연 신청 처리
        lectureRegistrationQueueService.processRegistration(request);

        // Then: 강연 신청 정보가 저장되었는지 확인
        verify(lectureRegistrationRepository, times(1))
                .save(any(LectureRegistration.class));
    }


    @Test
    public void testProcessRegistration_OverCapacity() {
        // 강의 신청 시 수용 인원이 초과된 경우 예외 발생 테스트

        // Given: 강연 수용 인원이 꽉 찬 상태 설정
        when(lectureRepository.findById(request.getLectureId()))
                .thenReturn(Optional.of(lecture));

        when(userRepository.findById(request.getUserId()))
                .thenReturn(Optional.of(user));

        when(lectureRegistrationRepository.findByLectureId(request.getLectureId()))
                .thenReturn(Arrays.asList(new LectureRegistration[lecture.getCapacity()]));

        // When: 강연 신청 시 예외 발생 여부 확인
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            lectureRegistrationQueueService.processRegistration(request);
        });

        // Then: 예외 메시지가 예상대로 "강의 수용 인원을 초과했습니다."인지 확인
        assertEquals("강의 수용 인원을 초과했습니다.", exception.getMessage());
    }

    @Test
    public void testProcessRegistration_AlreadyRegisteredUser() {
        // 사용자가 이미 강의에 등록된 경우 예외 발생 테스트

        // Given: 강연과 사용자 정보가 존재하고, 기존에 사용자가 이미 등록된 상태 설정
        when(lectureRepository.findById(request.getLectureId()))
                .thenReturn(Optional.of(lecture));

        when(userRepository.findById(request.getUserId()))
                .thenReturn(Optional.of(user));

        when(lectureRegistrationRepository.findByLectureIdAndUserId(request.getLectureId(), request.getUserId()))
                .thenReturn(Optional.of(new LectureRegistration()));

        // When: 강연 신청 시 예외 발생 여부 확인
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            lectureRegistrationQueueService.processRegistration(request);
        });

        // Then: 예외 메시지가 예상대로 "이미 등록된 사용자입니다."인지 확인
        assertEquals("이미 등록된 사용자입니다.", exception.getMessage());
    }
}
