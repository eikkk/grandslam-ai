package com.plainprog.grandslam_ai.entity.competitions;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompetitionMatchRepository extends JpaRepository<CompetitionMatch, Long> {
    long countByCompetitionId(Long competitionId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM CompetitionMatch m WHERE m.id = :id")
    CompetitionMatch findByIdWithLock(@Param("id") Long id);

    @Query("""
    SELECT m FROM CompetitionMatch m
    INNER JOIN m.submission1 s1
    INNER JOIN m.submission2 s2
    WHERE m.winnerSubmission IS NULL
      AND s1.accountId <> :excludeAccountId
      AND s2.accountId <> :excludeAccountId
    ORDER BY m.startedAt ASC
    """)
    List<CompetitionMatch> findUnfinishedMatchesExcludingAccount(
            @Param("excludeAccountId") Long excludeAccountId,
            Pageable pageable
    );
}