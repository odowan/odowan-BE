package com.smu.odowan.controller;

import com.smu.odowan.dto.TokenDTO;
import com.smu.odowan.dto.UserReq;
import com.smu.odowan.dto.UserRes;
import com.smu.odowan.global.BaseException;
import com.smu.odowan.global.BaseResponse;
import com.smu.odowan.service.EmailService;
import com.smu.odowan.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.smu.odowan.global.BaseResponseStatus.SUCCESS;


@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    // 회원가입
    @PostMapping("/join")
    public BaseResponse<UserRes.UserJoinRes> join(@RequestBody UserReq.UserJoinReq user) throws Exception {
        userService.join(user);
        return new BaseResponse<>(SUCCESS);
    }
    // 로그인
    @PostMapping("/login")
    public BaseResponse<List<TokenDTO>> login(@RequestBody UserReq.UserLoginReq request) throws BaseException {
        System.out.println("controller: login함수 실행");
        return new BaseResponse<>(userService.login(request));
    }

    // 회원가입 시 이메일 중복 확인
    @PostMapping("/join/check-email")
    public BaseResponse<Boolean> checkEmail(@RequestBody UserReq.EmailCheckReq request) throws BaseException {
        return new BaseResponse<>(userService.emailCheck(request));
    }

    // 회원가입 시 닉네임 중복 확인
    @PostMapping("join/check-name")
    public BaseResponse<Boolean> checkEmail(@RequestBody UserReq.NameCheckReq request) throws BaseException {
        return new BaseResponse<>(userService.checkName(request));
    }
    // 임시 비밀번호 발급
    @PostMapping("/password")
    public BaseResponse<String> getEmPw(@RequestBody UserReq.EmailCheckReq emailCheckReq) throws Exception {
        return new BaseResponse<>(userService.getEmPw(emailCheckReq));
    }
    // 로그아웃
    @PostMapping("/logout")
    public BaseResponse<String> logout(HttpServletRequest request) throws BaseException {
        return new BaseResponse<>(userService.logout(request));
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public BaseResponse<List<TokenDTO>> reissue(HttpServletRequest request) throws BaseException {
        System.out.println("controller: reissue 함수 실행");
        return new BaseResponse<>(userService.reissue(request));
    }

    // 회원 탈퇴
    @DeleteMapping("/delete")
    public BaseResponse<String> deactivateUser(HttpServletRequest request) throws BaseException {
        return new BaseResponse<>(userService.deactivateUser(request));
    }
    @PostMapping("/email")
    public BaseResponse<String> mailCheck(@RequestBody UserReq.EmailCheckReq emailCheckReq) throws Exception {
        return new BaseResponse<>(emailService.sendSimpleMessage(emailCheckReq.getEmail(), false));
    }

    @PostMapping("/email/verify")
    public BaseResponse<String> verifyEmail(@RequestBody UserReq.EmailVerifyReq verify) throws Exception {
        boolean isValid = userService.Emailverify(verify.getEmail(), verify.getCode());
        if (isValid) {
            return new BaseResponse<>("인증이 완료되었습니다.");
        } else {
            return new BaseResponse<>("인증 번호가 틀립니다.");
        }
    }

    // 회원정보 조회
    @GetMapping("")
    public BaseResponse<UserRes.UserInfo> getUserInfo(HttpServletRequest request) throws BaseException {
        return new BaseResponse<>(userService.getUserInfo(request));
    }
    // 회원정보 수정
    @PostMapping("")
    public BaseResponse<String> updateMyInfo(@RequestBody UserReq.UserChangeReq user, HttpServletRequest request) throws Exception {
        userService.updateMyInfo(user, request);
        return new BaseResponse<>("정보가 수정되었습니다.");
    }

    @PostMapping("/validatepw")
    public BaseResponse<String> validatePw(@RequestBody Map<String, String> requestBody, HttpServletRequest request) throws Exception {
        if (userService.validatePw(requestBody.get("password"), request))
            return new BaseResponse<>("비밀번호 확인");
        return new BaseResponse<>("비밀번호 불일치");
    }
    @PostMapping("/changepw")
    public BaseResponse<String> updatePw(@RequestBody UserReq.PasswordChangeReq changePw, HttpServletRequest request) throws Exception {
        userService.changePw(changePw, request);
        return new BaseResponse<>("비밀번호가 수정되었습니다.");
    }


}