package com.smu.odowan.service;

import com.smu.odowan.dto.AchievementRes;
import com.smu.odowan.entities.Achievement;
import com.smu.odowan.entities.AchievementDone;
import com.smu.odowan.entities.User;
import com.smu.odowan.global.BaseException;
import com.smu.odowan.repository.AchievementRepository;
import com.smu.odowan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.smu.odowan.global.BaseResponseStatus.POST_USERS_NOT_FOUND_EMAIL;


@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final UserRepository userRepository;

    public List<AchievementRes.AllAchievementRes> getAllAchievementList(){
        List<Achievement> achievements = achievementRepository.findAll();

        return achievements.stream().map(
                achievement -> {
                    AchievementRes.AllAchievementRes allAchievementRes = new AchievementRes.AllAchievementRes(achievement);
                    return allAchievementRes;
                })
                .collect(Collectors.toList());

    }

    public User achieveAchievement(Long idx) throws BaseException {
        // 업적을 달성할 때마다 achievementCount 값을 증가시킴
        User user = userRepository.findByUserIdx(idx);

        if (user != null) {
            Long achievementCount = user.getChallengeDoneCount();
            long count = (achievementCount != null) ? achievementCount.longValue() : 0L; // 기본값은 0
            user.setChallengeDoneCount(count + 1L); // achievementCount 증가
            userRepository.save(user); // 사용자 엔티티 저장
        } else {
            throw new BaseException(POST_USERS_NOT_FOUND_EMAIL);
        }

        return user;
    }



}
