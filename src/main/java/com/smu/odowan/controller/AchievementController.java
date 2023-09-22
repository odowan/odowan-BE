package com.smu.odowan.controller;

import com.smu.odowan.dto.AchievementRes;
import com.smu.odowan.entities.AchievementDone;
import com.smu.odowan.global.BaseResponse;
import com.smu.odowan.service.AchievementDoneService;
import com.smu.odowan.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/achievements")
public class AchievementController {

    private final AchievementService achievementService;

    private final AchievementDoneService achievementDoneService;

    @GetMapping("")
    public BaseResponse<List<AchievementRes.AllAchievementRes>> AllAchievementList() {
        List<AchievementRes.AllAchievementRes> allAchievementRes = achievementService.getAllAchievementList();
        return new BaseResponse<>(allAchievementRes);
    }

    @GetMapping("{userIdx}")
    public BaseResponse<List<AchievementDone>> AchievementDoneList(@PathVariable Long userIdx) {
        List<AchievementDone> achievementDones = achievementDoneService.getAchievementDoneList(userIdx);
        return new BaseResponse<>(achievementDones);
    }
}
