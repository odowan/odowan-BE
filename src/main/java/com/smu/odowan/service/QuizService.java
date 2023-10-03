package com.smu.odowan.service;

import com.smu.odowan.dto.QuizRes;
import com.smu.odowan.entities.ChallengeDone;
import com.smu.odowan.entities.Quiz;
import com.smu.odowan.entities.User;
import com.smu.odowan.repository.ChallengeDoneRepository;
import com.smu.odowan.repository.ChallengeRepository;
import com.smu.odowan.repository.QuizRepository;
import com.smu.odowan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;

    private final UserRepository userRepository;

    private final ChallengeRepository challengeRepository;
    private final ChallengeDoneRepository challengeDoneRepository;

    public QuizRes.QuizSubmitRes submitQuizAnswer(Long userIdx, Long challengeIdx, Integer choice) {
        // 도전 과제의 퀴즈를 가져옵니다.
        Quiz quiz = quizRepository.findQuizByChallengeChallengeIdx(challengeIdx);

        User user = userRepository.findByUserIdx(userIdx);
        // 퀴즈의 정답과 사용자의 선택을 비교합니다.
        if (quiz != null && quiz.getAnswer().equals(choice)) {
            // 정답일 경우 도전 과제 완료 정보를 저장합니다.
            ChallengeDone challengeDone = new ChallengeDone();
            challengeDone.setUser(userRepository.findByUserIdx(userIdx));
            challengeDone.setChallenge(challengeRepository.findByChallengeIdx(challengeIdx));
            challengeDoneRepository.save(challengeDone);

            Long challengeDoneCount = user.getChallengeDoneCount();
            user.setChallengeDoneCount(challengeDoneCount + 1L); // challengeDoneCount 증가
            userRepository.save(user); // 사용자 엔티티 저장

            // 정답인 경우 응답을 생성하여 반환합니다.
            return new QuizRes.QuizSubmitRes(true);
        } else {
            // 오답일 경우 응답을 생성하여 반환합니다.
            return new QuizRes.QuizSubmitRes(false);
        }
    }
}
