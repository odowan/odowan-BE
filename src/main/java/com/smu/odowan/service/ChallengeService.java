package com.smu.odowan.service;

import com.smu.odowan.dto.ChallengeRes;
import com.smu.odowan.entities.Challenge;
import com.smu.odowan.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    public List<ChallengeRes.LocationChallengeRes> locationChallengeList(String localName) {
        // 로컬 네임에 "종로구"가 포함된 모든 Challenge 엔티티 가져오기
        List<Challenge> challenges = challengeRepository.findByLocalNameContaining(localName);

        // DTO로 변환해서 리턴
        return challenges.stream().map(
                challenge -> {
                    ChallengeRes.LocationChallengeRes locationChallengeRes = new ChallengeRes.LocationChallengeRes(challenge);
                    return locationChallengeRes;
                })
                .collect(Collectors.toList());

    }
}
