package com.smu.odowan.service;

import com.smu.odowan.dto.RankingRes;
import com.smu.odowan.entities.User;
import com.smu.odowan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RankingService {

    @Autowired
    private UserRepository userRepository;

//    // AchievementCount를 기준으로 랭킹 조회
//    public List<RankingRes.AllRankingRes> getRankingByAchievementCount() {
//        return userRepository.findByOrderByAchievementCountDesc();
//    }

    public List<RankingRes.AllRankingRes>   getRankingByAchievementCount() {
        List<User> rankedUsers = userRepository.findByOrderByChallengeDoneCountDesc();

        AtomicReference<Long> rankingNum = new AtomicReference<>(1L);

        return rankedUsers.stream().map(
                        user -> {
                            RankingRes.AllRankingRes allRankingRes = new RankingRes.AllRankingRes(rankingNum.getAndSet(rankingNum.get() + 1),user.getName(),user.getChallengeDoneCount());
                            return allRankingRes;
                        })
                .collect(Collectors.toList());
    }

}
