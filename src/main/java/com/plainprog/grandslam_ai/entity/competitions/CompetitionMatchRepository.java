package com.plainprog.grandslam_ai.entity.competitions;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
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
      AND NOT EXISTS (
        SELECT v FROM MatchVote v 
        WHERE v.match = m 
        AND v.account.id = :excludeAccountId
      )
    ORDER BY m.startedAt ASC
    """)
    List<CompetitionMatch> findUnfinishedMatchesExcludingAccount(
            @Param("excludeAccountId") Long excludeAccountId,
            Pageable pageable
    );

    /**
     * Finds matches where:
     * 1. The voting deadline has passed
     * 2. No winner has been selected yet
     * 3. Both submissions are available
     */
    @Query("SELECT m FROM CompetitionMatch m " +
           "WHERE m.winnerSubmission IS NULL " +
           "AND m.submission1 IS NOT NULL " +
           "AND m.submission2 IS NOT NULL " +
           "AND m.voteDeadline < :currentTime " +
           "ORDER BY m.voteDeadline ASC")
    List<CompetitionMatch> findUnprocessedMatchesWithExpiredDeadlines(@Param("currentTime") Instant currentTime);
}
