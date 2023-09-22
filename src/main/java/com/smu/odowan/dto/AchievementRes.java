package com.smu.odowan.dto;

import com.smu.odowan.entities.Achievement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AchievementRes {
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class AllAchievementRes {
        private Long achievementIdx;
        private String achievementName;

        public AllAchievementRes(Achievement achievement) {
            this.achievementIdx = achievement.getAchievementIdx();
            this.achievementName = achievement.getAchievementName();
        }
    }
}
