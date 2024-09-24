package com.kstd.api.domain.lecture.repository;

import com.kstd.api.domain.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    /**
     * 기간내 강연 정보 조회
     *
     * @param startTime
     * @param endTime
     * @return List<Lecture>
     */
    List<Lecture> findAllByTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

}
