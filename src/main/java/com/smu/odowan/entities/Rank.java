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
public class Rank extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rankIdx")
    private Long rankIdx;

    @Column(name = "ranking", nullable = false)
    private Integer ranking;

    @OneToMany(mappedBy = "rank", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<User> bookings = new ArrayList<User>();
}

