package com.smu.odowan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserReq {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class UserJoinReq{
        private String email;
        private String password;
        private String name;
        private String phone;
        private String address;
    }

}
