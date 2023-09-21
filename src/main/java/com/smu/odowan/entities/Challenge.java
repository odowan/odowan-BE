package com.smu.odowan.entities;

import com.smu.odowan.global.BaseEntity;
import lombok.*;

import javax.persistence.*;

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
    private String challangeName;

    @Column(name = "challangeDesc", nullable = false)
    private String challangeDesc;

    @Column(name = "trending", nullable = false)
    private Integer trending;

    @Column(name = "localName", nullable = false)
    private String localName;

    @Column(name = "quiz", nullable = false)
    private String quiz;

    @Column(name = "answer", nullable = false)
    private Integer answer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement")
    private Achievement achievement;

}
