package com.kstd.api.domain.user.repository;

import com.kstd.api.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 사번으로 사용자 정보 조회
     *
     * @param userNo
     * @return
     */
    Optional<User> findByUserNo(Long userNo);
}
