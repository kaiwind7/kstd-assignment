package com.kstd.api.domain.lecture.repository;

import com.kstd.api.domain.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findAllByTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

}
