package com.smu.odowan.controller;

import com.smu.odowan.entities.AchievementDone;
import com.smu.odowan.global.BaseResponse;
import com.smu.odowan.service.ChallengeDoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/challengedones")
public class ChallengeDoneController {
    private final ChallengeDoneService challengeDoneService;

    @GetMapping("{userIdx}")
    public BaseResponse<List<Long>> AchievementDoneList(@PathVariable Long userIdx) {
        List<Long> challengeIdxList = challengeDoneService.getChallengeIdxList(userIdx);
        return new BaseResponse<>(challengeIdxList);
    }

}
