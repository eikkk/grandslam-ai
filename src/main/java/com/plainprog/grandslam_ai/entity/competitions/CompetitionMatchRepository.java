package com.plainprog.grandslam_ai.entity.competitions;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompetitionMatchRepository extends JpaRepository<CompetitionMatch, Long> {
    long countByCompetitionId(Long competitionId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM CompetitionMatch m WHERE m.id = :id")
    CompetitionMatch findByIdWithLock(@Param("id") Long id);
}