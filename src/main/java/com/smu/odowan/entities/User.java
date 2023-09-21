package com.smu.odowan.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smu.odowan.global.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="userIdx")
    private Long userIdx;

    @Column(name = "email", nullable = false, unique = true)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @Column(name = "password")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d|[^A-Za-z\\d]).{8,20}$", message = "비밀번호가 형식에 맞지 않습니다.")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    @Pattern(regexp = "^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$", message = "전화번호 형식이 맞지 않습니다.")
    private String phone;

    @Column(name = "nickname", nullable = false)
    @Size(max = 9)
    private String nickname;

    @Column(name = "address")
    private String address;

//        @ElementCollection
//        @Column(name = "challengeDone")
//        private List<Integer> challengeDone = new ArrayList<>();
//
//        @ElementCollection
//        @Column(name = "achievementDone")
//        private List<Integer> achievementDone = new ArrayList<>();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "rank")
    private Rank rank;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Challenge> challenges = new ArrayList<Challenge>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Achievement> achievements = new ArrayList<Achievement>();

}
