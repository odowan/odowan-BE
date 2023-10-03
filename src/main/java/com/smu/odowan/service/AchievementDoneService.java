package com.smu.odowan.service;

import com.smu.odowan.dto.AchievementRes;
import com.smu.odowan.entities.Achievement;
import com.smu.odowan.entities.AchievementDone;
import com.smu.odowan.repository.AchievementDoneRepository;
import com.smu.odowan.repository.AchievementRepository;
import com.smu.odowan.repository.ChallengeDoneRepository;
import com.smu.odowan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AchievementDoneService {
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final AchievementDoneRepository achievementDoneRepository;
    private final ChallengeDoneRepository challengeDoneRepository;

    public List<AchievementDone> getAchievementDoneList(Long userIdx){
        List<AchievementDone> achievementDones = achievementDoneRepository.findAllByUserUserIdx(userIdx);
        return achievementDones;
    }

    @Transactional
    public AchievementDone createAchievementDoneForUser(Long userIdx) {
        // userIdx에 해당하는 challengeIdx 배열을 가져옵니다.
        List<Long> challengeIdxs = challengeDoneRepository.findChallengeIdxByUserIdx(userIdx);

        // challengeIdx 배열에서 1, 2, 3 등 특정 조건에 맞는 것들을 찾아서 처리합니다.
        // 1,2,3 도전과제
        for (Long challengeIdx : challengeIdxs) {
            if (challengeIdx == 1L || challengeIdx == 2L || challengeIdx == 3L) {

                // Achievement에 해당하는 엔티티를 찾습니다.
                // 1번 업적
                Achievement achievement = achievementRepository.findByAchievementIdx(1L);

                // AchievementDone을 생성하고 연관 관계를 설정합니다.
                AchievementDone achievementDone = new AchievementDone();
                achievementDone.setUser(userRepository.findByUserIdx(userIdx));
                achievementDone.setAchievement(achievement);

                // AchievementDone을 저장합니다.
                achievementDone = achievementDoneRepository.save(achievementDone);

                return achievementDone;
            }
        }

        return null; // 특정 조건에 맞는 AchievementDone을 생성하지 못한 경우
    }


}
