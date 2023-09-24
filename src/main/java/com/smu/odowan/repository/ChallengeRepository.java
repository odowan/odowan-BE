package com.smu.odowan.repository;

import com.smu.odowan.entities.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Challenge findByChallengeIdx(Long challengeIdx);
}
