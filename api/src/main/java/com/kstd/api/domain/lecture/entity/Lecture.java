package com.kstd.api.domain.lecture.entity;

import com.kstd.api.domain.lecture.request.LectureRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lecture")
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lecturer;
    private String place;
    private int capacity;
    private LocalDateTime time;
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // 엔티티의 상태를 변경하는 메서드
    public void modifyLecture(LectureRequest request) {
        if (request.getLecturer() != null) this.lecturer = request.getLecturer();
        if (request.getPlace() != null) this.place = request.getPlace();
        if (request.getCapacity() > 0) this.capacity = request.getCapacity();
        if (request.getTime() != null) this.time = request.getTime();
        if (request.getContent() != null) this.content = request.getContent();
    }
}
