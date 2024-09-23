package com.kstd.api.backoffice.service;

import com.kstd.api.common.enums.ErrorCode;
import com.kstd.api.common.exception.ServiceException;
import com.kstd.api.domain.lecture.dto.LectureDTO;
import com.kstd.api.domain.lecture.entity.Lecture;
import com.kstd.api.domain.lecture.repository.LectureRepository;
import com.kstd.api.domain.lecture.request.LectureRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LectureAdminService {
    private final LectureRepository lectureRepository;

    public Page<LectureDTO> findAllLecture(Pageable pageable) {
        Page<Lecture> lectures = lectureRepository.findAll(pageable);
        return lectures.map(LectureDTO::fromEntity);
    }

    private Lecture findLectureById(Long id) {
        return lectureRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.NOT_FOUND_ENTITY));
    }

    public LectureDTO findLecture(Long id) {
        Lecture lecture = findLectureById(id);
        return LectureDTO.fromEntity(lecture);
    }

    @Transactional
    public void saveLecture(LectureRequest lectureRequest) {
        Lecture lecture = lectureRepository.save(Lecture.builder()
                .lecturer(lectureRequest.getLecturer())
                .place(lectureRequest.getPlace())
                .capacity(lectureRequest.getCapacity())
                .time(lectureRequest.getTime())
                .content(lectureRequest.getContent())
                .build());
    }

    @Transactional
    public void updateLecture(Long id, LectureRequest lectureRequest) {
        Lecture lecture = findLectureById(id);
        lecture.modifyLecture(lectureRequest);
    }


    @Transactional
    public void deleteLecture(Long id) {
        Lecture lecture = findLectureById(id);
        lectureRepository.delete(lecture);
    }
}
