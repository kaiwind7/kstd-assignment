package com.kstd.api.domain.lecture.entity;

import com.kstd.api.common.enums.Status;
import com.kstd.api.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lecture_registration")
public class LectureRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 강연과의 연관관계 설정
    @JoinColumn(referencedColumnName = "id", name = "lecture_id")
    private Lecture lecture;

    @ManyToOne(fetch = FetchType.LAZY) // 사용자와의 연관관계 설정
    @JoinColumn(referencedColumnName = "id", name = "user_id")
    private User user;

    @Column(name = "registration_time", nullable = false)
    private LocalDateTime registrationTime;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    public void cancelLecture() {
        this.status = Status.CANCELED;
    }
}
