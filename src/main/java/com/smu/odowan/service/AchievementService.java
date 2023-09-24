package com.smu.odowan.service;

import com.smu.odowan.dto.AchievementRes;
import com.smu.odowan.entities.Achievement;
import com.smu.odowan.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;


    public List<AchievementRes.AllAchievementRes> getAllAchievementList(){
        List<Achievement> achievements = achievementRepository.findAll();

        return achievements.stream().map(
                achievement -> {
                    AchievementRes.AllAchievementRes allAchievementRes = new AchievementRes.AllAchievementRes(achievement);
                    return allAchievementRes;
                })
                .collect(Collectors.toList());

    }



}
