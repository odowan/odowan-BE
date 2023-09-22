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
public class Quiz extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="quizIdx")
    private Long quizIdx;

    @Column(name = "quizDesc", nullable = false)
    private String quizDesc;

    @Column(name = "one", nullable = false)
    private String one;

    @Column(name = "two", nullable = false)
    private String two;

    @Column(name = "three", nullable = false)
    private String three;

    @Column(name = "four", nullable = false)
    private String four;

    @Column(name = "answer", nullable = false)
    private Integer answer;
}
