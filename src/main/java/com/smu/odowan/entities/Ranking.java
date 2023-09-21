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
public class Ranking extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rankingIdx")
    private Long rankingIdx;

    @Column(name = "rankingNum", nullable = false)
    private Integer rankingNum;

    @OneToMany(mappedBy = "ranking", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<User> users = new ArrayList<User>();
}

