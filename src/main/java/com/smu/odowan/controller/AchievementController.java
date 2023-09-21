package com.smu.odowan.controller;

import com.smu.odowan.dto.AchievementRes;
import com.smu.odowan.global.BaseResponse;
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

//    public BaseResponse<List<ReviewRes.ReviewListRes>> getReviewList(@PathVariable Long hospitalIdx, @PageableDefault(page = 0, size = 2) Pageable page) {
//        List<ReviewRes.ReviewListRes> reviewListRes = reviewService.getReviewList(hospitalIdx, page);
//        return new BaseResponse<>(reviewListRes);
//    }

    @GetMapping("")
    public BaseResponse<List<AchievementRes.AllAchievementRes>> AllAchievementList() {
        List<AchievementRes.AllAchievementRes> allAchievementRes = achievementService.getAllAchievementList();
        return new BaseResponse<>(allAchievementRes);
    }

}
