package com.smu.odowan.controller;


import com.smu.odowan.dto.AchievementRes;
import com.smu.odowan.dto.RankingRes;
import com.smu.odowan.dto.UserReq;
import com.smu.odowan.dto.UserRes;
import com.smu.odowan.entities.Ranking;
import com.smu.odowan.entities.User;
import com.smu.odowan.global.BaseException;
import com.smu.odowan.global.BaseResponse;
import com.smu.odowan.service.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.smu.odowan.global.BaseResponseStatus.SUCCESS;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/ranking")
public class RankingController {

    @Autowired
    public RankingService rankingService;

    @GetMapping("")
    public BaseResponse<List<RankingRes.AllRankingRes>> getRankingByAchievementCount() {
        List<RankingRes.AllRankingRes> rankingList = rankingService.getRankingByAchievementCount();
        return new BaseResponse<>(rankingList);
    }
}
