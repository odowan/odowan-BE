package com.smu.odowan.controller;

import com.smu.odowan.dto.QuizReq;
import com.smu.odowan.dto.QuizRes;
import com.smu.odowan.entities.Quiz;
import com.smu.odowan.global.BaseResponse;
import com.smu.odowan.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/submit")
    public BaseResponse<QuizRes.QuizSubmitRes> QuizSubmit(@RequestBody QuizReq.QuizSubmitReq request){

        return new BaseResponse<>(quizService.submitQuizAnswer(request.getUserIdx(), request.getChallengeIdx(), request.getChoice()));
    }

}
