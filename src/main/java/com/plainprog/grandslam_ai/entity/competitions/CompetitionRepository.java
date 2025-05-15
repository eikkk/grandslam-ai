package com.plainprog.grandslam_ai.entity.competitions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    List<Competition> findAllByStatus(Competition.CompetitionStatus status);
}