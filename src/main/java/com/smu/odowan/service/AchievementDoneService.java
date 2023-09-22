package com.smu.odowan.service;

import com.smu.odowan.dto.AchievementRes;
import com.smu.odowan.entities.Achievement;
import com.smu.odowan.entities.AchievementDone;
import com.smu.odowan.repository.AchievementDoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AchievementDoneService {

    private final AchievementDoneRepository achievementDoneRepository;

    public List<AchievementDone> getAchievementDoneList(Long userIdx){
        List<AchievementDone> achievementDones = achievementDoneRepository.findAllByUserUserIdx(userIdx);
        return achievementDones;
    }

}
