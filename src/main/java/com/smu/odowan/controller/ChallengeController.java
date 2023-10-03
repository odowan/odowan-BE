package com.smu.odowan.controller;

import com.smu.odowan.dto.ChallengeRes;
import com.smu.odowan.global.BaseResponse;
import com.smu.odowan.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping("/location")
    public BaseResponse<List<ChallengeRes.LocationChallengeRes>> locationChallengeList(@RequestParam String localName) {
        List<ChallengeRes.LocationChallengeRes> locationChallengeList = challengeService.locationChallengeList(localName);
        return new BaseResponse<>(locationChallengeList);
    }

}