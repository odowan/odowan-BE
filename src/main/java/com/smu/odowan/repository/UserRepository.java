package com.smu.odowan.repository;
import com.smu.odowan.dto.RankingRes;
import com.smu.odowan.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    User findByUserIdx(Long userIdx);

    List<User> findByOrderByChallengeDoneCountDesc();
}