package com.kstd.api.domain.lecture.repository;

import com.kstd.api.common.enums.Status;
import com.kstd.api.domain.lecture.entity.LectureRegistrationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LectureRegistrationLogRepository extends JpaRepository<LectureRegistrationLog, Long> {
    /**
     * 사용자 ID와 강연 ID에 해당하는 로그를 반환합니다.
     *
     * @param userId    사용자 ID
     * @param lectureId 강연 ID
     * @return 로그 엔티티
     */
    Optional<LectureRegistrationLog> findByUserIdAndLectureId(Long userId, Long lectureId);

    /**
     * 사용자 ID와 강연 ID, 상태 해당하는 로그를 반환합니다.
     *
     * @param userId    사용자 ID
     * @param lectureId 강연 ID
     * @param status    상태
     * @return 로그 엔티티
     */
    Optional<LectureRegistrationLog> findByUserIdAndLectureIdAndStatus(Long userId, Long lectureId, Status status);

    /**
     * 사용자 ID와 강연 ID, 상태 목록에 해당하는 로그가 존재하는지 확인합니다.
     *
     * @param userId    사용자 ID
     * @param lectureId 강연 ID
     * @param statuses  상태 목록
     * @return 존재 여부 (true: 존재함, false: 존재하지 않음)
     */
    boolean existsByUserIdAndLectureIdAndStatusIn(Long userId, Long lectureId, List<String> statuses);
}
