package com.smu.odowan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class QuizReq {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class QuizSubmitReq {
        private Long userIdx;
        private Long ChallengeIdx;
        private Integer choice;
    }
}
