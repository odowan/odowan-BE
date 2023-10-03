package com.smu.odowan.dto;

import com.smu.odowan.entities.Achievement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class RankingRes {
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class AllRankingRes {
        private Long Ranking;
        private String Name;
        private Long challengeDoneCount;
    }
}
