package com.smu.odowan.service;

import com.smu.odowan.entities.AchievementDone;
import com.smu.odowan.entities.ChallengeDone;
import com.smu.odowan.repository.ChallengeDoneRepository;
import com.smu.odowan.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeDoneService {
    private final ChallengeDoneRepository challengeDoneRepository;

    public List<Long> getChallengeIdxList(Long userIdx) {
        List<Long> challengeIdxList = challengeDoneRepository.findChallengeIdxByUserIdx(userIdx);
        return challengeIdxList;
    }

}
