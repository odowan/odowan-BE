package com.smu.odowan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smu.odowan.global.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Challenge extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="challengeIdx")
    private Long challengeIdx;

    @Column(name = "challangeName", nullable = false)
    private String challengeName;

    @Column(name = "challangeDesc", nullable = false)
    private String challengeDesc;

    @Column(name = "trending", nullable = false)
    private Integer trending;

    @Column(name = "localName", nullable = false)
    private String localName;

//    @Column(name = "localDetail", nullable = false)
//    private String localDetail;

    @OneToOne(mappedBy = "challenge", cascade = CascadeType.ALL)
    @JsonIgnore
    private Quiz quiz;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ChallengeDone> challengeDones = new ArrayList<>();

}
