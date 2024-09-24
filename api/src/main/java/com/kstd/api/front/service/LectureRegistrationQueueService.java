package com.kstd.api.front.service;

import com.kstd.api.common.enums.ErrorCode;
import com.kstd.api.common.exception.ServiceException;
import com.kstd.api.domain.lecture.entity.Lecture;
import com.kstd.api.domain.lecture.entity.LectureRegistration;
import com.kstd.api.domain.lecture.entity.LectureRegistrationLog;
import com.kstd.api.domain.lecture.repository.LectureRegistrationLogRepository;
import com.kstd.api.domain.lecture.repository.LectureRegistrationRepository;
import com.kstd.api.domain.lecture.repository.LectureRepository;
import com.kstd.api.domain.lecture.request.LectureRegistrationRequest;
import com.kstd.api.domain.user.entity.User;
import com.kstd.api.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureRegistrationQueueService {
    private final BlockingQueue<LectureRegistrationRequest> registrationQueue = new LinkedBlockingQueue<>();
    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    private final LectureRegistrationRepository lectureRegistrationRepository;
    private final LectureRegistrationLogRepository lectureRegistrationLogRepository;
    private ExecutorService executorService;

    @PostConstruct
    private void init() {
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(this::processQueue);
    }

    @PreDestroy
    public void shutdown() {
        log.info("LectureRegistrationQueueService 종료..");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    /**
     * 등록된 큐 진행
     */
    private void processQueue() {
        while (!Thread.currentThread().isInterrupted()) {
            LectureRegistrationRequest request = null;

            try {
                request = registrationQueue.take();
                updateRegistrationLogStatus(request, "PROCESSING", "강연 등록 요청을 처리 중");
                this.processRegistration(request);
                updateRegistrationLogStatus(request, "SUCCESS", "강연 등록 성공");
            } catch (InterruptedException e) {
                log.warn("강연 등록 처리 스레드가 인터럽트되었습니다.", e);
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("강연 등록 요청 처리 중 오류가 발생했습니다.", e);
                updateRegistrationLogStatus(request, "FAILED", e.getMessage());
                handleRegistrationFailure(request, e);
            }
        }
    }


    /**
     * 강연 등록 요청이 이미 큐에 있는지 또는 처리 중인지 확인합니다.
     *
     * @param userId    사용자 ID
     * @param lectureId 강연 ID
     * @return 중복 여부
     */
    public boolean isAlreadyQueuedOrProcessing(Long userId, Long lectureId) {
        return lectureRegistrationLogRepository.existsByUserIdAndLectureIdAndStatusIn(
                userId, lectureId, Arrays.asList("QUEUED", "PROCESSING"));
    }


    /**
     * 강의 신청 큐에 적재
     *
     * @param request 강의 신청 요청
     * @return String
     */
    public String addRegistrationToQueue(LectureRegistrationRequest request) {
        // 이미 사용자가 강연 신청했는지 확인
        checkLectureRegistration(request);

        // 큐에 넣기 전에 DB에서 중복 여부 확인
        if (isAlreadyQueuedOrProcessing(request.getUserId(), request.getLectureId())) {
            String message = "이미 대기열에 등록된 요청입니다.";
            updateRegistrationLogStatus(request, "DUPLICATE_IN_QUEUE", message);
            log.warn("대기열 중복 요청: 사용자 ID: {}, 강연 ID: {}", request.getUserId(), request.getLectureId());
            return message;
        }

        try {
            saveRegistrationLog(request, "QUEUED", "강의 등록 요청이 큐에 추가되었습니다.");

            if (!registrationQueue.offer(request, 2, TimeUnit.SECONDS)) {
                String message = "강의 신청 대기열이 가득 찼습니다. 나중에 다시 시도하세요.";
                log.warn(message);
                updateRegistrationLogStatus(request, "FAILED", message);
                return message;
            }

            log.info("강의 등록 요청이 큐에 추가되었습니다: {}", request);
            return "강의 등록 요청이 정상적으로 처리되었습니다.";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            String message = "큐에 추가하는 동안 인터럽트가 발생했습니다.";
            log.error(message, e);
            updateRegistrationLogStatus(request, "FAILED", message);
            return message;
        }
    }

    @Transactional
    public void processRegistration(LectureRegistrationRequest request) {
        log.info("강연 등록 진행중 - userId: {}, lectureId: {}",
                request.getUserId(), request.getLectureId());

        Lecture lecture = lectureRepository.findById(request.getLectureId())
                .orElseThrow(() -> new ServiceException("강의가 존재하지 않습니다.", ErrorCode.NOT_FOUND_ENTITY));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ServiceException("사용자가 존재하지 않습니다.", ErrorCode.NOT_FOUND_ENTITY));

        // 기존에 등록된 사용자인지 확인
        checkLectureRegistration(request);

        // 수용 인원 초과 확인
        if (lectureRegistrationRepository.findByLectureId(request.getLectureId()).size() >= lecture.getCapacity()) {
            throw new ServiceException("강의 수용 인원을 초과했습니다.", ErrorCode.OVER_CAPACITY);
        }

        // 강의 등록 정보 생성 및 저장
        lectureRegistrationRepository.save(LectureRegistration.builder()
                .lecture(lecture)
                .user(user)
                .registrationTime(LocalDateTime.now())
                .status("CONFIRMED").build());

        log.info("강연 등록 완료 - userId: {}", request.getUserId());
    }

    /**
     * 중복 등록 확인 메서드
     *
     * @param lectureRegistrationRequest 강연 등록 요청
     */
    private void checkLectureRegistration(LectureRegistrationRequest lectureRegistrationRequest) {
        if (lectureRegistrationRepository.findByLectureIdAndUserId(lectureRegistrationRequest.getLectureId(), lectureRegistrationRequest.getUserId()).isPresent()) {
            throw new ServiceException("이미 등록된 사용자입니다.", ErrorCode.ALREADY_REGISTERED_USER);
        }
    }

    /**
     * 로그 저장 메서드
     *
     * @param request 강의 신청 요청
     * @param status  상태 코드
     * @param message 메시지
     */
    private void saveRegistrationLog(LectureRegistrationRequest request, String status, String message) {
        LectureRegistrationLog log = LectureRegistrationLog.builder()
                .userId(request.getUserId())
                .lectureId(request.getLectureId())
                .status(status)
                .message(message)
                .build();
        lectureRegistrationLogRepository.save(log);
    }

    /**
     * 로그 상태 업데이트 메서드
     *
     * @param request 강의 신청 요청
     * @param status  상태 코드
     * @param message 메시지
     */
    private void updateRegistrationLogStatus(LectureRegistrationRequest request, String status, String message) {
        LectureRegistrationLog log = lectureRegistrationLogRepository.findByUserIdAndLectureId(request.getUserId(), request.getLectureId())
                .orElseThrow(() -> new ServiceException("등록 로그를 찾을 수 없습니다.", ErrorCode.NOT_FOUND_ENTITY));
        log.updateLog(status, message);
    }

    // 등록 실패 시 처리 로직
    private void handleRegistrationFailure(LectureRegistrationRequest request, Exception e) {
        // TODO 성공 실패시 별도 알림 처리
        String errorMessage = "강의 신청에 실패했습니다. 사유: " + e.getMessage();
        log.error("Lecture registration failed for userId: {}, lectureId: {}. Error: {}",
                request.getUserId(), request.getLectureId(), e.getMessage());
        //notificationService.notifyUser(request.getUserId(), errorMessage); // 실패 알림 전송
    }
}
