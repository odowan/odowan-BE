package com.smu.odowan.controller;

import com.smu.odowan.dto.UserDTO;
import com.smu.odowan.dto.UserReq;
import com.smu.odowan.dto.UserRes;
import com.smu.odowan.entities.User;
import com.smu.odowan.global.BaseResponse;
import com.smu.odowan.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class LoginController {

    @Autowired
    private LoginService loginService;


    // 회원가입
    @PostMapping("/join")
    public BaseResponse<UserRes.UserJoinRes> join(@RequestBody UserReq.UserJoinReq user) throws Exception {

        return new BaseResponse<>(loginService.join(user));
    }
}