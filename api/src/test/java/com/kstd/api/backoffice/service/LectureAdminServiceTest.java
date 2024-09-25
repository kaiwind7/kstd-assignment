package com.kstd.api.backoffice.service;

import com.kstd.api.common.enums.Status;
import com.kstd.api.common.exception.ServiceException;
import com.kstd.api.domain.lecture.dto.LectureDTO;
import com.kstd.api.domain.lecture.dto.LectureRegistrationsDTO;
import com.kstd.api.domain.lecture.entity.Lecture;
import com.kstd.api.domain.lecture.entity.LectureRegistration;
import com.kstd.api.domain.lecture.repository.LectureRegistrationRepository;
import com.kstd.api.domain.lecture.repository.LectureRepository;
import com.kstd.api.domain.lecture.request.LectureRequest;
import com.kstd.api.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class LectureAdminServiceTest {
    @InjectMocks
    private LectureAdminService lectureAdminService;

    @Spy
    private LectureRepository lectureRepository;

    @Spy
    private LectureRegistrationRepository lectureRegistrationRepository;

    private Lecture lecture;
    private LectureRequest lectureRequest;

    @BeforeEach
    public void setup() {
        // 초기 데이터 설정
        lecture = Lecture.builder()
                .id(1L)
                .lecturer("Original Lecturer")
                .place("Original Place")
                .capacity(30)
                .time(LocalDateTime.of(2024, 9, 25, 14, 0))
                .content("Original Content")
                .build();

        lectureRequest = LectureRequest.builder()
                .lecturer("Test Lecturer")
                .place("Test Place")
                .capacity(10)
                .time(LocalDateTime.now())
                .content("Test Content")
                .build();
    }

    @Test
    public void testFindAllLecture_Success() {
        // Given: 페이징 요청 설정
        Pageable pageable = PageRequest.of(0, 10);
        Page<Lecture> lecturePage = new PageImpl<>(Arrays.asList(lecture));

        // When: Mock 설정 - lectureRepository.findAll()이 호출되면 강연 페이지 반환
        when(lectureRepository.findAll(pageable)).thenReturn(lecturePage);

        // Then: 강연 목록을 조회하고 결과를 검증
        Page<LectureDTO> result = lectureAdminService.findAllLecture(pageable);

        // 결과 검증
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(lecture.getId(), result.getContent().get(0).getId());
        verify(lectureRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testFindLecture_Success() {
        // Given: 특정 강연 ID로 강연 조회
        when(lectureRepository.findById(lecture.getId())).thenReturn(Optional.of(lecture));

        // When: 강연 조회
        LectureDTO result = lectureAdminService.findLecture(lecture.getId());

        // Then: 조회된 강연 정보가 예상과 일치하는지 검증
        assertNotNull(result);
        assertEquals(lecture.getId(), result.getId());
        verify(lectureRepository, times(1)).findById(lecture.getId());
    }

    @Test
    public void testFindLecture_NotFound() {
        // Given: 강연이 존재하지 않는 경우를 Mock 설정
        when(lectureRepository.findById(lecture.getId())).thenReturn(Optional.empty());

        // When: 강연을 조회할 때, 예외가 발생하는지 검증
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            lectureAdminService.findLecture(lecture.getId());
        });

        // Then: 예외 메시지가 예상과 일치하는지 확인
        assertEquals("강연 정보가 없습니다.", exception.getMessage());
        verify(lectureRepository, times(1)).findById(lecture.getId());
    }

    @Test
    public void testSaveLecture_Success() {
        // Given: 새로운 강연을 저장할 때 Mock 설정
        when(lectureRepository.save(any(Lecture.class))).thenReturn(lecture);

        // When: 강연을 저장
        LectureDTO result = lectureAdminService.saveLecture(lectureRequest);

        // Then: 저장된 강연 정보가 예상과 일치하는지 확인
        assertNotNull(result);
        assertEquals(lecture.getLecturer(), result.getLecturer());
        verify(lectureRepository, times(1)).save(any(Lecture.class));
    }

    @Test
    public void testUpdateLecture_Success() {
        // Given: 기존 강연 정보를 수정할 때 Mock 설정
        when(lectureRepository.findById(lecture.getId())).thenReturn(Optional.of(lecture));
        when(lectureRepository.save(any(Lecture.class))).thenReturn(lecture);

        // When: 강연 정보를 업데이트
        LectureDTO result = lectureAdminService.updateLecture(lecture.getId(), lectureRequest);

        // Then: 수정된 강연 정보가 예상과 일치하는지 확인
        assertNotNull(result);
        assertEquals(lecture.getLecturer(), result.getLecturer());
        verify(lectureRepository, times(1)).findById(lecture.getId());
    }

    @Test
    public void testDeleteLecture_Success() {
        // Given: 강연 삭제 시 Mock 설정
        when(lectureRepository.findById(lecture.getId())).thenReturn(Optional.of(lecture));

        // When: 강연 삭제
        lectureAdminService.deleteLecture(lecture.getId());

        // Then: 강연이 삭제되었는지 확인
        verify(lectureRepository, times(1)).findById(lecture.getId());
        verify(lectureRepository, times(1)).delete(lecture);
    }

    @Test
    public void testFindLectureRegistrations_Success() {
        // Given: 강연 신청자 목록 조회 시 Mock 설정
        User user = User.builder()
                .id(1L)
                .name("Test User").build();

        LectureRegistration registration = LectureRegistration.builder()
                .id(1L)
                .lecture(lecture)
                .user(user).build();

        // Mock 설정: findByLectureId 호출 시 강연 등록 내역 반환
        when(lectureRepository.findById(lecture.getId())).thenReturn(Optional.of(lecture));
        when(lectureRegistrationRepository.findByLectureIdAndStatus(lecture.getId(), Status.CONFIRMED)).thenReturn(Arrays.asList(registration));

        // When: 강연 신청자 목록 조회
        LectureRegistrationsDTO result = lectureAdminService.findLectureRegistrations(lecture.getId());

        // Then: 조회된 강연 신청자 목록이 예상과 일치하는지 검증
        assertNotNull(result);
        assertEquals(lecture.getId(), result.getLecture().getId());
        assertEquals(1, result.getUsers().size());
        assertEquals(user.getUserNo(), result.getUsers().get(0).getUserNo());

        // Mock 메서드 호출 검증
        verify(lectureRepository, times(1)).findById(lecture.getId());
        verify(lectureRegistrationRepository, times(1)).findByLectureIdAndStatus(lecture.getId(), Status.CONFIRMED);
    }

}
