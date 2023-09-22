package com.smu.odowan.repository;

import com.smu.odowan.entities.AchievementDone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementDoneRepository extends JpaRepository<AchievementDone, Long> {

    List<AchievementDone> findAllByUserUserIdx(Long userIdx);
}
