package com.smu.odowan.config.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserReq {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserJoinReq{ // 회원가입
        private String email;
        private String password;
        private String nickname;
        private String phone;
        private String local;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserLoginReq{ // 로그인
        private String email;
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserUpdateReq{ // 유저 정보 변경
        private String email;
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindPwReq{ // 비밀번호 찾기
        private String email;
        private String nickname;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindIdReq{ // 아이디 찾기
        private String nickname;
        private String phone;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerifyPwReq{ // 비밀번호 확인
        private String currentPassword;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePwReq{ // 비밀번호 변경
        private String newPassword;
        private String confirmPassword;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MailCheckReq{ // 이메일 인증
        private String email;
    }

}
