package com.smu.odowan.dto;

import com.smu.odowan.entities.Achievement;
import com.smu.odowan.entities.Challenge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ChallengeRes {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class LocationChallengeRes{
        private Long challengeIdx;
        private String challengeName;
        private String localName;

        public LocationChallengeRes(Challenge challenge) {
            this.challengeIdx = challenge.getChallengeIdx();
            this.challengeName = challenge.getChallengeName();
            this.localName = challenge.getLocalName();
        }
    }
}
