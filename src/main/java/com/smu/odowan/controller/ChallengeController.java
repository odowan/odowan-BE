package com.smu.odowan.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/local")
public class ChallengeController {

//    @Autowired
//    private ChanllengeService chanllengeService;

//    @GetMapping("{addressCode}")
//    public BaseResponse<List<Challenge>> AchievementDoneList(@PathVariable Long addressCode) {
//        List<Challenge> challenges = chanllengeService.getAddressDetailList(addressCode);
//        return new BaseResponse<>(challenges);
//    }

}