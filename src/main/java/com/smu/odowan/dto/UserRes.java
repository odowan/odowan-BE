package com.smu.odowan.dto;

import com.smu.odowan.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserRes {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserJoinRes{ // 회원가입
        private User user;

    }
}
