package com.smu.odowan.repository;

import com.smu.odowan.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Quiz findQuizByChallengeChallengeIdx(Long ChallengeIdx);

}
