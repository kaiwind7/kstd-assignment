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
    List<LectureRegistration> findLectureRegistrationByUser(User user);

    Optional<LectureRegistration> findByLectureIdAndUserId(Long lectureId, Long userId);

    List<LectureRegistration> findByLectureId(Long lectureId);

    @Query("SELECT new com.kstd.api.domain.lecture.dto.PopularLectureDTO(l, COUNT(r.id)) " +
            "FROM Lecture l " +
            "JOIN LectureRegistration r " +
            "ON r.lecture.id = l.id " +
            "WHERE r.registrationTime >= :startDate " +
            "GROUP BY l " +
            "ORDER BY COUNT(r.id) DESC")
    List<PopularLectureDTO> findPopularLecturesByRegistrationCount(@Param("startDate") LocalDateTime startDate);
}
