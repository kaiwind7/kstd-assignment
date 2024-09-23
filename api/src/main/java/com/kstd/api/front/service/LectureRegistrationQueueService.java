package com.kstd.api.front.service;

import com.kstd.api.common.enums.ErrorCode;
import com.kstd.api.common.exception.ServiceException;
import com.kstd.api.domain.lecture.entity.Lecture;
import com.kstd.api.domain.lecture.entity.LectureRegistration;
import com.kstd.api.domain.lecture.repository.LectureRegistrationRepository;
import com.kstd.api.domain.lecture.repository.LectureRepository;
import com.kstd.api.domain.lecture.request.LectureRegistrationRequest;
import com.kstd.api.domain.user.entity.User;
import com.kstd.api.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureRegistrationQueueService {
    private final BlockingQueue<LectureRegistrationRequest> registrationQueue = new LinkedBlockingQueue<>();
    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    private final LectureRegistrationRepository lectureRegistrationRepository;

    @PostConstruct
    private void init() {
        new Thread(() -> {
            while (true) {
                try {
                    LectureRegistrationRequest lectureRegistrationRequest = registrationQueue.take();
                    this.processRegistration(lectureRegistrationRequest);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }).start();
    }

    /**
     * 강의 신청 큐에 적재
     *
     * @param request
     * @return
     * @throws InterruptedException
     */
    public void addRegistrationToQueue(LectureRegistrationRequest request) throws InterruptedException {
        registrationQueue.put(request);
        System.out.println("강의 등록 요청이 큐에 추가되었습니다: " + request);
    }

    @Transactional
    public void processRegistration(LectureRegistrationRequest lectureRegistrationRequest) {
        Lecture lecture = lectureRepository.findById(lectureRegistrationRequest.getLectureId()).orElseThrow(() -> new ServiceException("강의가 존재하지 않습니다.", ErrorCode.NOT_FOUND_ENTITY));

        User user = userRepository.findById(lectureRegistrationRequest.getUserId()).orElseThrow(() -> new ServiceException("사용자가 존재하지 않습니다.", ErrorCode.NOT_FOUND_ENTITY));

        // 기존에 등록된 사용자인지 확인
        if (lectureRegistrationRepository.findByLectureIdAndUserId(lectureRegistrationRequest.getLectureId(), lectureRegistrationRequest.getUserId()).isPresent()) {
            throw new ServiceException("이미 등록된 사용자입니다.", ErrorCode.ALREADY_REGISTERED_USER);
        }

        // 수용 인원 초과 확인
        if (lectureRegistrationRepository.findByLectureId(lectureRegistrationRequest.getLectureId()).size() >= lecture.getCapacity()) {
            throw new ServiceException("강의 수용 인원을 초과했습니다.", ErrorCode.OVER_CAPACITY);
        }

        // 강의 등록 정보 생성 및 저장
        lectureRegistrationRepository.save(LectureRegistration.builder()
                .lecture(lecture)
                .user(user)
                .registrationTime(LocalDateTime.now())
                .status("CONFIRMED").build());

        System.out.println("등록완료");
    }
}
