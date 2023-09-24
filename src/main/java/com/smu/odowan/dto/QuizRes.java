package com.smu.odowan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class QuizRes {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class QuizSubmitRes {
        private boolean isCorrect;
    }

}
