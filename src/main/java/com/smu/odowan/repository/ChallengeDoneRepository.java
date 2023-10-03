package com.smu.odowan.repository;

import com.smu.odowan.entities.ChallengeDone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChallengeDoneRepository extends JpaRepository<ChallengeDone, Long> {

    // userIdx에 해당하는 challengeIdx 목록을 가져오는 메서드
    @Query("SELECT cd.challenge.challengeIdx FROM ChallengeDone cd WHERE cd.user.userIdx = :userIdx")
    List<Long> findChallengeIdxByUserIdx(Long userIdx);
}
