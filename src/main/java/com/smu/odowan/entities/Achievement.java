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
public class Achievement extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="achievementIdx")
    private Long achievementIdx;

    @Column(name = "achievementName", nullable = false)
    private String achievementName;

    @OneToMany(mappedBy = "achievement", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AchievementDone> achievementDones = new ArrayList<>();
}
