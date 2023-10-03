package com.smu.odowan.controller;


import com.smu.odowan.dto.QuizReq;
import com.smu.odowan.dto.QuizRes;
import com.smu.odowan.entities.AchievementDone;
import com.smu.odowan.global.BaseResponse;
import com.smu.odowan.service.AchievementDoneService;
import com.smu.odowan.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/achievementdones")
public class AchievementDoneController {
    private final AchievementDoneService achievementDoneService;

    @PostMapping("{userIdx}")
    public BaseResponse<AchievementDone> createAchievementDone(@PathVariable Long userIdx){
        return new BaseResponse<>(achievementDoneService.createAchievementDoneForUser(userIdx));
    }

}
