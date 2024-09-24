package com.kstd.api.domain.lecture.repository;

import com.kstd.api.domain.lecture.dto.PopularLectureDTO;
import com.kstd.api.domain.lecture.entity.LectureRegistration;
import com.kstd.api.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LectureRegistrationRepository extends JpaRepository<LectureRegistration, Long> {

    /**
     * 특정 사용자의 모든 강연 등록 정보를 조회
     *
     * @param user 강연 등록 정보를 조회할 사용자
     * @return 해당 사용자가 등록한 모든 강연 등록 목록
     */
    List<LectureRegistration> findLectureRegistrationByUser(User user);

    /**
     * 특정 강연 ID와 사용자 ID로 강연 등록 정보를 조회
     *
     * @param lectureId 강연 ID
     * @param userId    사용자 ID
     * @return 강연 등록 정보 (존재하지 않으면 빈 Optional 반환)
     */
    Optional<LectureRegistration> findByLectureIdAndUserId(Long lectureId, Long userId);

    /**
     * 특정 강연에 대한 모든 강연 등록 정보를 조회
     *
     * @param lectureId 강연 ID
     * @return 해당 강연에 등록된 모든 강연 등록 정보 목록
     */
    List<LectureRegistration> findByLectureId(Long lectureId);

    /**
     * 날짜 이후의 강연 신청 수를 기준으로 가장 인기 있는 강연 목록을 조회
     *
     * @param startDate 등록 카운트를 집계할 시작 날짜
     * @return 인기 강연 목록을 포함하는 DTO 리스트 (등록 수에 따라 내림차순 정렬)
     */
    @Query("SELECT new com.kstd.api.domain.lecture.dto.PopularLectureDTO(l, COUNT(r.id)) " +
            "FROM Lecture l " +
            "JOIN LectureRegistration r " +
            "ON r.lecture.id = l.id " +
            "WHERE r.registrationTime >= :startDate " +
            "GROUP BY l " +
            "ORDER BY COUNT(r.id) DESC")
    List<PopularLectureDTO> findPopularLecturesByRegistrationCount(@Param("startDate") LocalDateTime startDate);
}
